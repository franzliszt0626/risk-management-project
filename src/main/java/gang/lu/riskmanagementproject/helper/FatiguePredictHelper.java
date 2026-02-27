package gang.lu.riskmanagementproject.helper;

import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.property.LstmProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static gang.lu.riskmanagementproject.common.global.GlobalLogConstants.*;
import static gang.lu.riskmanagementproject.message.FailedMessages.LSTM_PREDICT_FAILED;

/**
 * LSTM 疲劳预测核心组件。
 * <p>
 * 每次调用 {@link #predict} 时执行以下流程：
 * <ol>
 *   <li>对输入序列做 Min-Max 归一化（基于训练集范围，防止量纲影响）；</li>
 *   <li>构建 many-to-one RNN 样本，将全序列送入单层 LSTM 训练；</li>
 *   <li>滚动推理 6 步，将上一步预测值追加到窗口；</li>
 *   <li>反归一化后返回原始量纲预测值列表，并截断到 [0, 100]。</li>
 * </ol>
 * <p>
 * <b>适用场景：</b>嵌入式、数据量小（≤ 200 条）的即时训练模式。
 * 若需提升性能，可改为离线训练 + 加载序列化模型文件的方式。
 *
 * @author Franz Liszt
 * @since 2026-02-27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FatiguePredictHelper {

    private final LstmProperty lstmProperty;

    /**
     * 训练 LSTM 并滚动推理未来6步的疲劳百分比。
     *
     * @param historyValues 历史疲劳百分比序列（时间正序，最旧在前，长度 ≥ 最小历史记录限制）
     * @return 未来 6 步预测值列表（原始量纲 %，精确到小数点后两位）
     * @throws BizException 模型训练或推理异常时抛出
     */
    public List<Double> predict(List<Double> historyValues) {
        int n = historyValues.size();
        log.info(LOG_LSTM_START_TRAINING,
                n, lstmProperty.getEpochs(), lstmProperty.getHiddenSize());

        try {
            // 1. Min-Max 归一化
            double min = historyValues.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
            double max = historyValues.stream().mapToDouble(Double::doubleValue).max().orElse(1.0);
            double range = (max - min) < 1e-6 ? 1.0 : (max - min);
            double[] normalized = historyValues.stream()
                    .mapToDouble(v -> (v - min) / range)
                    .toArray();
            // 2. 构建 RNN 训练样本（many-to-one）
            // shape: [batchSize=1, features=1, timeSteps=n-1]
            // 输入：t=0...n-2，标签：t=1...n-1（每步预测下一步）
            int timeSteps = n - 1;
            INDArray input = Nd4j.zeros(1, 1, timeSteps);
            INDArray labels = Nd4j.zeros(1, 1, timeSteps);
            for (int i = 0; i < timeSteps; i++) {
                input.putScalar(new int[]{0, 0, i}, normalized[i]);
                labels.putScalar(new int[]{0, 0, i}, normalized[i + 1]);
            }
            DataSet trainingData = new DataSet(input, labels);

            // 3. 构建并训练 LSTM 网络
            MultiLayerNetwork model = buildModel();
            // ScoreIterationListener 仅在最后一个 epoch 打印，减少日志噪声
            model.addListeners(new ScoreIterationListener(lstmProperty.getEpochs()));
            for (int epoch = 0; epoch < lstmProperty.getEpochs(); epoch++) {
                model.fit(trainingData);
            }
            log.info(LOG_LSTM_TRAINING_COMPLETE);

            // 4. 滚动推理：以末尾窗口滚动预测未来 6 步
            // 初始窗口 = 归一化后的最后 timeSteps 个值
            double[] window = new double[timeSteps];
            System.arraycopy(normalized, 0, window, 0, timeSteps);
            List<Double> predictions = new ArrayList<>(lstmProperty.getPredictSteps());
            for (int step = 0; step < lstmProperty.getPredictSteps(); step++) {
                // 每次推理前重置 RNN 状态，确保独立
                model.rnnClearPreviousState();
                INDArray inferInput = Nd4j.zeros(1, 1, timeSteps);
                for (int i = 0; i < timeSteps; i++) {
                    inferInput.putScalar(new int[]{0, 0, i}, window[i]);
                }
                // 取最后一个时间步的输出作为预测值
                INDArray output = model.output(inferInput);
                double predNorm = output.getDouble(0, 0, timeSteps - 1);
                double predOrigin = predNorm * range + min;
                // 截断到合法疲劳范围 [0, 100]
                predOrigin = Math.max(0.0, Math.min(100.0, predOrigin));
                // 保留两位小数
                predictions.add(Math.round(predOrigin * 100.0) / 100.0);
                // 滑动窗口：丢最旧一步，追加本步预测的归一化值
                System.arraycopy(window, 1, window, 0, timeSteps - 1);
                window[timeSteps - 1] = predNorm;
            }

            log.info(LOG_LSTM_REASON_COMPLETE, predictions);
            return predictions;

        } catch (Exception e) {
            log.error(LOG_LSTM_REASON_FAILED, e);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, LSTM_PREDICT_FAILED);
        }
    }

    // ======================== 内部方法 ========================

    /**
     * 构建单层 LSTM + RnnOutputLayer 网络并完成初始化。
     *
     * @return 已初始化的 {@link MultiLayerNetwork}
     */
    private MultiLayerNetwork buildModel() {
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(42)
                .weightInit(WeightInit.XAVIER)
                .updater(new Adam(lstmProperty.getAdamLearningRate()))
                .list()
                .layer(new LSTM.Builder()
                        .nIn(1)
                        .nOut(lstmProperty.getHiddenSize())
                        .activation(Activation.TANH)
                        .build())
                .layer(new RnnOutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .activation(Activation.IDENTITY)
                        .nIn(lstmProperty.getHiddenSize())
                        .nOut(1)
                        .build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        return model;
    }
}