package cn.iocoder.yudao.module.monitor.service.alarm.impl;

import cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo.AlarmActionRecordCreateReqVO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmInfoDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmActionRecordDO;
import cn.iocoder.yudao.module.monitor.dal.mysql.alarm.AlarmInfoMapper;
import cn.iocoder.yudao.module.monitor.dal.mysql.alarm.AlarmActionRecordMapper;
import cn.iocoder.yudao.module.monitor.service.alarm.AlarmActionRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class AlarmActionRecordServiceImpl implements AlarmActionRecordService {

    @Resource
    private AlarmActionRecordMapper recordMapper;
    @Resource
    private AlarmInfoMapper alarmInfoMapper;

    @Override
    @Transactional
    public void createAndFinishAlarm(AlarmActionRecordCreateReqVO reqVO, Long userId) {
        // 1. 新增处理记录
        AlarmActionRecordDO record = new AlarmActionRecordDO();
        record.setAlertRecordId(reqVO.getAlertRecordId());
        record.setUserId(userId);
        record.setProcessType(reqVO.getProcessType());
        record.setProcessTime(LocalDateTime.now());
        record.setDescription(reqVO.getDescription());
        record.setRemark(reqVO.getRemark());
        record.setCreateTime(LocalDateTime.now());
        recordMapper.insert(record);

        // 2. 更新告警为已处理
        AlarmInfoDO alarm = new AlarmInfoDO();
        alarm.setId(reqVO.getAlertRecordId());
        alarm.setStatus(1); // 已处理
        alarmInfoMapper.updateById(alarm);
    }
}