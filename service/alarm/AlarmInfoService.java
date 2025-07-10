package cn.iocoder.yudao.module.monitor.service.alarm;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo.AlarmInfoCreateReqVO;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo.AlarmInfoPageReqVO;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo.AlarmInfoRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo.AlarmInfoUpdateReqVO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmInfoDO;

import java.util.Set;

public interface AlarmInfoService {
    Long createAlarmInfo(AlarmInfoCreateReqVO reqVO);
    void updateAlarmInfo(AlarmInfoUpdateReqVO reqVO);
    void deleteAlarmInfo(Long id);
    AlarmInfoDO getAlarmInfo(Long id);
    PageResult<AlarmInfoRespVO> getAlarmInfoPage(AlarmInfoPageReqVO pageReqVO, Set<Long> filterObjIds);
}