package gang.lu.riskmanagementproject.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gang.lu.riskmanagementproject.domain.po.RiskHistory;
import gang.lu.riskmanagementproject.mapper.RiskHistoryMapper;
import gang.lu.riskmanagementproject.service.RiskHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 历史风险数据表 服务实现类
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Service
public class RiskHistoryServiceImpl extends ServiceImpl<RiskHistoryMapper, RiskHistory> implements RiskHistoryService {

}
