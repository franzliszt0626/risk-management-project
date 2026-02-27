package gang.lu.riskmanagementproject.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/27 11:44
 * @description Lstm参数
 */
@Data
@Component
public class LstmProperty {
    /**
     * 预测未来步数（业务固定为 6）
     */
    @Value("${lstm.predict-steps}")
    private int predictSteps;
    /**
     * 参与训练最少历史记录条数
     */
    @Value("${lstm.min-history}")
    private int minHistory;
    /**
     * 参与训练最多历史记录条数
     */
    @Value("${lstm.max-history}")
    private int maxHistory;
    /**
     * 训练轮次
     */
    @Value("${lstm.epochs}")
    private int epochs;
    /**
     * Adam 学习率
     */
    @Value("${lstm.adam-learning-rate}")
    private double adamLearningRate;
    /**
     * LSTM 隐藏层神经元数
     */
    @Value("${lstm.hidden-size}")
    private int hiddenSize;
}
