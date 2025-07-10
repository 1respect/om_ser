package cn.iocoder.yudao.module.monitor.job;

import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmInfoDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.AlertRuleDO;
import cn.iocoder.yudao.module.monitor.dal.mysql.alarm.AlarmInfoMapper;
import cn.iocoder.yudao.module.monitor.dal.mysql.template.AlertRuleMapper;
import cn.iocoder.yudao.module.monitor.service.notice.AlarmNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmMailRepeatJob {

    private final AlarmInfoMapper alarmInfoMapper;
    private final AlertRuleMapper alertRuleMapper;
    private final AlarmNoticeService alarmNoticeService;

    /**
     * 每分钟扫描一次
     */
    @Scheduled(cron = "0 * * * * ?")
    public void repeatSendAlarmMail() {
        // 查出所有状态未关闭且邮件发送次数未达到最大值的告警
        List<AlarmInfoDO> alarms = alarmInfoMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<AlarmInfoDO>()
                        .isNull(AlarmInfoDO::getStatus)
                        .or()
                        .ne(AlarmInfoDO::getStatus, 1) // 1=已处理
        );
        for (AlarmInfoDO alarm : alarms) {
            // *** 改为和主业务一致：用 monitorIndexId + templateId 匹配规则 ***
            AlertRuleDO rule = alertRuleMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<AlertRuleDO>()
                            .eq(AlertRuleDO::getMonitorIndexId, alarm.getMonitorIndexId())
                            .eq(AlertRuleDO::getTemplateId, alarm.getTemplateId())
                            .eq(AlertRuleDO::getAlertLevel, alarm.getAlarmLevelId())
                            .last("limit 1")
            );
            if (rule == null) {
                log.warn("[邮件重复发送JOB] 未找到报警规则: monitorIndexId={}, templateId={}, alertLevelId={}", alarm.getMonitorIndexId(), alarm.getTemplateId(),alarm.getAlarmLevelId());
                return;
            }

            Integer sendCount = alarm.getMailSendCount() == null ? 0 : alarm.getMailSendCount();
            Long maxCount = rule.getContinuedNumber() == null ? 1L : rule.getContinuedNumber();
            Long repeatedIntervalMin = rule.getRepeatedRuleTime() == null ? 0L : rule.getRepeatedRuleTime();

            if (sendCount >= maxCount) continue;
            if (alarm.getLastMailSendTime() != null && repeatedIntervalMin > 0) {
                long minutesSinceLast = Duration.between(alarm.getLastMailSendTime(), LocalDateTime.now()).toMinutes();
                if (minutesSinceLast < repeatedIntervalMin) continue;
            }

            // 满足条件则调用发送
            try {
                alarmNoticeService.sendAlarmNotice(alarm);
            } catch (Exception e) {
                log.error("[邮件重复发送JOB] 发送异常, alarmId={}", alarm.getId(), e);
            }
        }
    }
}