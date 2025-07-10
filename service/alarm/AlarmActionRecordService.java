package cn.iocoder.yudao.module.monitor.service.alarm;

import cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo.AlarmActionRecordCreateReqVO;

public interface AlarmActionRecordService {
    void createAndFinishAlarm(AlarmActionRecordCreateReqVO reqVO, Long userId);
}