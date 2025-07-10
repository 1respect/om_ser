package cn.iocoder.yudao.module.monitor.config;

import cn.iocoder.yudao.module.monitor.dal.dataobject.template.AlertRuleDO;
import cn.iocoder.yudao.module.monitor.service.template.AlertRuleService;
import cn.iocoder.yudao.module.monitor.service.template.AlertRuleServiceImpl;
import cn.iocoder.yudao.module.system.dal.dataobject.monitor.MonitorTypeDO;
import cn.iocoder.yudao.module.system.dal.mysql.monitor.MonitorTypeMapper;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class AlertRuleCacheLoader implements ApplicationRunner {
    private final AlertRuleService alertRuleService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MonitorTypeMapper monitorTypeMapper;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 删除 Redis 中已存在的报警规则数据
        Set<String> keys = redisTemplate.keys("alarm:template:*");
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
        // 从数据库加载所有报警规则
        List<AlertRuleDO> allRules = alertRuleService.getAllAlertRules();

        for (AlertRuleDO rule : allRules) {
            String redisKey = String.format("alarm:template:%d:type:%d:metric:%d",
                    rule.getTemplateId(), rule.getMonitorTypeId(), rule.getMonitorIndexId());
            String hashKey = "rule:" + rule.getId();
            MonitorTypeDO monitorTypeDO = monitorTypeMapper.selectById(rule.getMonitorTypeId());
            if (monitorTypeDO.getTypeCode()!=null){
                rule.setTypeCode(monitorTypeDO.getTypeCode());
            }
            // 将规则写入 Redis Hash
            redisTemplate.opsForHash().put(redisKey, hashKey, rule);
        }
    }
}
