package cn.iocoder.yudao.module.monitor.service.notice.impl;

import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmLevelDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmInfoDO;
import cn.iocoder.yudao.module.monitor.service.notice.AlarmMailNoticeSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmMailNoticeSenderImpl implements AlarmMailNoticeSender {

    private final JavaMailSender mailSender;

    @Override
    public void sendAlarmMail(List<String> toEmails, AlarmLevelDO level, AlarmInfoDO alarm, String centerName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("service_noreply@shambala.cn");
            helper.setTo(toEmails.toArray(new String[0]));
            helper.setSubject(String.format("【%d级·%s】%s告警通知", level.getLevel(), level.getName(), centerName));
            helper.setText(buildContent(level, alarm, centerName), true);

            mailSender.send(message);
            log.info("[邮件告警] 邮件发送成功，to={}", toEmails);
        } catch (Exception e) {
            log.error("[邮件告警] 邮件发送失败", e);
        }
    }

    private String buildContent(AlarmLevelDO level, AlarmInfoDO alarm, String centerName) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>告警等级：").append(level.getLevel()).append("级（").append(level.getName()).append("）</h2>");
        sb.append("<p><b>智算中心：</b>").append(centerName != null ? centerName : "").append("</p>");
        sb.append("<p><b>告警描述：</b>").append(alarm.getDescription() != null ? alarm.getDescription() : "").append("</p>");
        sb.append("<p><b>触发条件：</b>").append(alarm.getAlertCondition() != null ? alarm.getAlertCondition() : "").append("</p>");
        sb.append("<p><b>发生时间：</b>").append(alarm.getAlertTime() != null ? alarm.getAlertTime() : alarm.getCreateTime()).append("</p>");
        return sb.toString();
    }
}