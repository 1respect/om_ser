package cn.iocoder.yudao.module.monitor.service.notice;

import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmLevelDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmInfoDO;

import java.util.List;

/**
 * 邮件告警发送器
 */
public interface AlarmMailNoticeSender {
    /**
     * 发送告警邮件
     * @param toEmails 收件人列表
     * @param level 告警等级
     * @param alarm 告警信息
     */
    void sendAlarmMail(List<String> toEmails, AlarmLevelDO level, AlarmInfoDO alarm, String centerName);
}