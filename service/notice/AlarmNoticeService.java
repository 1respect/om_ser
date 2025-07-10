package cn.iocoder.yudao.module.monitor.service.notice;

import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmInfoDO;

/**
 * 告警通知统一入口
 */
public interface AlarmNoticeService {
    /**
     * 告警通知主入口（后续可扩展短信、电话、企微等）
     * @param alarmInfoId 告警记录ID
     */
    void sendAlarmNotice(AlarmInfoDO alarmInfoId);
}