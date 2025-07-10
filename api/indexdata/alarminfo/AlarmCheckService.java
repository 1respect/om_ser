package cn.iocoder.yudao.module.monitor.api.indexdata.alarminfo;

import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmInfoDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmActionRecordDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.asset.MonitorEntityTempDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.AlertRuleDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.MetricConfigDO;
import cn.iocoder.yudao.module.monitor.dal.mysql.alarm.AlarmInfoMapper;
import cn.iocoder.yudao.module.monitor.dal.mysql.alarm.MonitorEntityTempMapper;
import cn.iocoder.yudao.module.monitor.dal.mysql.template.MetricConfigMapper;
import cn.iocoder.yudao.module.monitor.service.notice.AlarmNoticeService;
import cn.iocoder.yudao.module.monitor.service.template.AlertRuleService;
import cn.iocoder.yudao.module.monitor.dal.mysql.alarm.AlarmActionRecordMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 报警检查服务
 */
@Service
@Slf4j
public class AlarmCheckService {

    @Autowired
    private AlarmInfoMapper alarmInfoMapper;

    @Resource
    private AlarmActionRecordMapper alarmActionRecordMapper;

    @Resource
    private AlertRuleService alertRuleService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private AlarmNoticeService alarmNoticeService;

    @Resource
    private MonitorEntityTempMapper monitorEntityTempMapper;
    @Resource
    private MetricConfigMapper metricConfigMapper;


    // 使用 Jackson 支持 LocalDateTime 反序列化
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // Redis Key 模板
    private static final String ALERT_RULE_REDIS_KEY_TEMPLATE = "alarm:template:%d:type:%d:metric:%d";

    // 缓存报警规则
    private Map<String, AlertRuleDO> alertRuleCache = new ConcurrentHashMap<>();

    /**
     * 初始化方法，在 Bean 加载完成后执行
     */
    @PostConstruct
    public void init() {
        // refreshAlertRules(); // 初始化加载一次
    }

//    /**
//     * 刷新缓存中的报警策略
//     */
//    private void refreshAlertRules() {
//        List<AlertRuleDO> rules = alertRuleService.getAllAlertRules();
//
//        if (CollectionUtils.isEmpty(rules)) {
//            return;
//        }
//
//        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
//
//        for (AlertRuleDO rule : rules) {
//            Long templateId = rule.getTemplateId();
//            Long typeId = rule.getMonitorTypeId();
//            Long monitorIndexId = rule.getMonitorIndexId();
//
//            if (templateId == null || typeId == null || monitorIndexId == null) {
//                continue; // 忽略不完整的数据
//            }
//
//            String key = String.format(ALERT_RULE_REDIS_KEY_TEMPLATE, templateId, typeId, monitorIndexId);
//            String field = rule.getId().toString(); // 假设 field 来自 rule 的唯一标识
//
//            log.debug("============初次把告警规则加入到内存，key为：{}============", key);
//
//            try {
//                String json = OBJECT_MAPPER.writeValueAsString(rule);
//                hashOps.put(key, field, json); // 更新 Redis 缓存
//                alertRuleCache.put(field, rule); // 更新本地缓存
//            } catch (JsonProcessingException e) {
//                log.error("告警规则序列化失败，rule={}", rule, e);
//            }
//        }
//    }

    /**
     * 根据 templateId 和 typeId 获取告警规则并判断是否触发告警
     */
    public void checkAndGenerateAlarms(List<MonitorDataAndEntityVO> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        List<AlarmInfoDO> alarms = new ArrayList<>();
        List<AlarmActionRecordDO> actionRecords = new ArrayList<>();

        for (MonitorDataAndEntityVO record : dataList) {
            try {
                Double metricValue = Double.parseDouble(record.getMetricValue());

                Long tempid = record.getTemplateId();
                Long typeId = record.getTypeId();
                String key = String.format(ALERT_RULE_REDIS_KEY_TEMPLATE, tempid, typeId, record.getMetricId());
                List<AlertRuleDO> alertRules = loadHashFromRedis(key, AlertRuleDO.class);

                if (alertRules.isEmpty()) {
                    log.info("============没有匹配的告警规则，直接返回============");
                    return; // 没有匹配的告警规则，直接返回
                }

                // 遍历每个规则进行匹配
                for (AlertRuleDO rule : alertRules) {
                    if (!Objects.equals(rule.getMonitorIndexId(), record.getMetricId())) {
                        continue; // 跳过不匹配的规则
                    }

                    String expression = rule.getExpression();
                    Double upValue = rule.getUpValue().doubleValue();
                    Double downValue = rule.getDownValue() != null ? rule.getDownValue().doubleValue() : null;

                    boolean shouldAlarm = false;
                    String reason = "";

                    switch (expression) {
                        case "<":
                            if (metricValue < upValue) {
                                shouldAlarm = true;
                                reason = "低于阈值: " + upValue;
                            }
                            break;
                        case ">":
                            if (metricValue > upValue) {
                                shouldAlarm = true;
                                reason = "超过阈值: " + upValue;
                            }
                            break;
                        case "<=":
                            if (metricValue <= upValue) {
                                shouldAlarm = true;
                                reason = "小于等于阈值: " + upValue;
                            }
                            break;
                        case ">=":
                            if (metricValue >= upValue) {
                                shouldAlarm = true;
                                reason = "大于等于阈值: " + upValue;
                            }
                            break;
                        case "区间外":
                            if (downValue != null && upValue != null && (metricValue < downValue || metricValue > upValue)) {
                                shouldAlarm = true;
                                reason = "超出安全范围 [" + downValue + ", " + upValue + "]";
                            }
                            break;
                        default:
                            // 不支持的表达式类型
                            break;
                    }

                    if (shouldAlarm) {
                        AlarmInfoDO alarm = createAlarm(record, reason, upValue, rule);
                        alarms.add(alarm);
                    }
                }


            } catch (NumberFormatException e) {
                // 忽略无法转换的数值
            }
        }

        if (!alarms.isEmpty()) {
           alarmInfoMapper.insertBatch(alarms);
            for (AlarmInfoDO alarm : alarms) {
                alarmNoticeService.sendAlarmNotice(alarm);
            }
        }
    }



    /**
     * 从 Redis 中加载指定 key 的告警规则
     */
    private <T> List<T> loadHashFromRedis(String key, Class<T> clazz) {
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();
        Map<String, Object> entries = hashOps.entries(key);

        List<T> result = new ArrayList<>();

        if (entries != null && !entries.isEmpty()) {
            for (Map.Entry<String, Object> entry : entries.entrySet()) {
                try {
                    if (entry.getValue() instanceof String) {
                        T value = OBJECT_MAPPER.readValue((String) entry.getValue(), clazz);
                        result.add(value);
                    } else if (clazz.isInstance(entry.getValue())) {
                        result.add(clazz.cast(entry.getValue()));
                    } else {
                        log.warn("Redis Hash 中字段 {} 的值类型不匹配，期望 {}, 实际 {}", entry.getKey(), clazz.getSimpleName(), entry.getValue().getClass().getName());
                    }
                } catch (Exception e) {
                    log.error("反序列化 Redis 中的 {} 失败，key={}, field={}", clazz.getSimpleName(), key, entry.getKey(), e);
                }
            }
        }

        return result;
    }

    /**
     * 创建报警信息对象
     */
    private AlarmInfoDO createAlarm(MonitorDataAndEntityVO record, String reason, Double threshold, AlertRuleDO rule) {
        AlarmInfoDO alarm = new AlarmInfoDO();
        alarm.setMonitorObjId(record.getMonitorObjId());
        alarm.setMonitorIndexId(rule.getMonitorIndexId());
        alarm.setAlertCondition(rule.getExpression());
        alarm.setCalculatioCycle(rule.getCalculatioCycle());
        alarm.setAlarmLevelId(rule.getAlertLevel());
        alarm.setTriggerValue(record.getMetricValue());
        alarm.setTemplateId(rule.getTemplateId());

        // 查监控对象名称
        String monitorObjName = null;
        if (record.getMonitorObjId() != null) {
            MonitorEntityTempDO obj = monitorEntityTempMapper.selectById(record.getMonitorObjId());
            monitorObjName = obj != null ? obj.getName() : "";
        }

        alarm.setDescription(replaceContentPlaceholders(rule.getContentTemplate(), record,rule,monitorObjName));
        alarm.setCreateTime(LocalDateTime.now());
        alarm.setCenterId(rule.getCenterId());
        alarm.setAlertTime(record.getCollectionTime());
        alarm.setTemplateId(record.getTemplateId());
        return alarm;
    }

    /**
     * 替换告警内容中的占位符（如 ${metricValue}, ${collectionTime} 等）
     */
    private String replaceContentPlaceholders(String contentTemplate, MonitorDataAndEntityVO data, AlertRuleDO rule,String monitorObjName) {
        if (contentTemplate == null || contentTemplate.isEmpty()) {
            return "";
        }

        // 查单位
        String unit = "";
        if (data.getMetricId() != null) {
            MetricConfigDO metricConfig = metricConfigMapper.selectById(data.getMetricId());
            unit = metricConfig != null && metricConfig.getUnit() != null ? metricConfig.getUnit() : "";
        }

        contentTemplate = contentTemplate.replace("{{实际值}}", data.getMetricValue());
        //contentTemplate = contentTemplate.replace("{{阈值}}", rule.getUpValue() != null ? rule.getUpValue().toString() : "");
        contentTemplate = contentTemplate.replace("{{指标单位}}", unit);
        contentTemplate = contentTemplate.replace("{{监控对象}}", monitorObjName);
        // 可扩展更多字段...

        return contentTemplate;
    }

}
