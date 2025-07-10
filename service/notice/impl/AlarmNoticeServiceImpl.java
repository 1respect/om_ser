package cn.iocoder.yudao.module.monitor.service.notice.impl;

import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmInfoDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmLevelDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.center.CenterInfoDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.AlertRuleDO;
import cn.iocoder.yudao.module.monitor.dal.mysql.alarm.AlarmInfoMapper;
import cn.iocoder.yudao.module.monitor.dal.mysql.center.CenterInfoMapper;
import cn.iocoder.yudao.module.monitor.dal.mysql.template.AlertRuleMapper;
import cn.iocoder.yudao.module.monitor.service.alarm.AlarmContactService;
import cn.iocoder.yudao.module.monitor.service.alarm.AlarmLevelService;
import cn.iocoder.yudao.module.monitor.service.notice.AlarmNoticeService;
import cn.iocoder.yudao.module.monitor.service.notice.AlarmMailNoticeSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmNoticeServiceImpl implements AlarmNoticeService {

    private final AlarmInfoMapper alarmInfoMapper;
    private final AlarmLevelService alarmLevelService;
    private final AlarmContactService alarmContactService;
    private final AlarmMailNoticeSender alarmMailNoticeSender;
    private final AlertRuleMapper alertRuleMapper;
    private final CenterInfoMapper centerInfoMapper;

    @Override
    public void sendAlarmNotice(AlarmInfoDO alarm) {
        // 1. 获取告警记录
       // AlarmInfoDO alarm = alarmInfoMapper.selectById(alarmInfoId);
        if (alarm == null) {
            log.warn("[邮件告警] 未找到告警记录: id={}", alarm.getId());
            return;
        }

        // 2. 获取报警规则
        //用 monitorIndexId templateId alertLevelId作为规则关联字段
        AlertRuleDO rule = alertRuleMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<AlertRuleDO>()
                        .eq(AlertRuleDO::getMonitorIndexId, alarm.getMonitorIndexId())
                        .eq(AlertRuleDO::getTemplateId, alarm.getTemplateId())
                        .eq(AlertRuleDO::getAlertLevel, alarm.getAlarmLevelId())
                        .last("limit 1")
        );
        if (rule == null) {
            log.warn("[邮件告警] 未找到报警规则: monitorIndexId={}, templateId={}, alarmLevelId={}", alarm.getMonitorIndexId(), alarm.getTemplateId(),alarm.getAlarmLevelId());
            return;
        }

        // 3. 邮件重复发送逻辑
        Integer sendCount = alarm.getMailSendCount() == null ? 0 : alarm.getMailSendCount();
        Long maxCount = rule.getContinuedNumber() == null ? 1L : rule.getContinuedNumber();
        Long repeatedIntervalMin = rule.getRepeatedRuleTime() == null ? 0L : rule.getRepeatedRuleTime();

        if (sendCount >= maxCount) {
            log.info("[邮件告警] 邮件已达最大重复次数,id={}, sendCount={}, maxCount={}",alarm.getId(), sendCount, maxCount);
            return;
        }
        if (alarm.getLastMailSendTime() != null && repeatedIntervalMin > 0) {
            long minutesSinceLast = Duration.between(alarm.getLastMailSendTime(), LocalDateTime.now()).toMinutes();
            if (minutesSinceLast < repeatedIntervalMin) {
                log.info("[邮件告警] 距离上次发送未到间隔, id={}, minutesSinceLast={}, repeatedIntervalMin={}",alarm.getId(), minutesSinceLast, repeatedIntervalMin);
                return;
            }
        }

        // 4. 获取告警等级（含邮件开关）
        AlarmLevelDO level = alarmLevelService.getAlarmLevel(alarm.getAlarmLevelId());
        if (level == null) {
            log.warn("[邮件告警] 未找到告警等级: id={}", alarm.getAlarmLevelId());
            return;
        }
        if (level.getMailNotice() == null || level.getMailNotice() != 1) {
            log.info("[邮件告警] 邮件开关关闭，不发送邮件。levelId={}", level.getId());
            return;
        }

        // 5. 获取该智算中心的所有紧急联系人邮箱
        List<String> toEmails = alarmContactService.getEmailsByCenterId(alarm.getCenterId());
        if (toEmails == null || toEmails.isEmpty()) {
            log.warn("[邮件告警] 未找到联系人邮箱，centerId={}", alarm.getCenterId());
            return;
        }

        // 查中心名
        String centerName = "";
        if (alarm.getCenterId() != null) {
            CenterInfoDO center = centerInfoMapper.selectById(alarm.getCenterId());
            centerName = center != null ? center.getName() : "";
        }



        // 6. 调用邮件发送器
        alarmMailNoticeSender.sendAlarmMail(toEmails, level, alarm,centerName);

        // 7. 更新邮件发送次数和最后发送时间
        alarm.setMailSendCount(sendCount + 1);
        alarm.setLastMailSendTime(LocalDateTime.now());
        alarmInfoMapper.updateById(alarm);
    }
}