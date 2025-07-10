package cn.iocoder.yudao.module.monitor.service.alarm;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.level.vo.*;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmLevelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface AlarmLevelService {
    Long createAlarmLevel(AlarmLevelCreateReqVO reqVO);

    void updateAlarmLevel(AlarmLevelUpdateReqVO reqVO);

    void deleteAlarmLevel(Long id);

    AlarmLevelDO getAlarmLevel(Long id);

    PageResult<AlarmLevelDO> getAlarmLevelPage(AlarmLevelPageReqVO reqVO);

    List<AlarmLevelDO> getAlarmLevelList();

    /**
     * 获取所有告警等级（一般用于下拉）
     */
    List<AlarmLevelDO> getSimpleList();

    /**
     * 批量根据ID获取告警等级
     */
    Map<Long, AlarmLevelDO> getMapByIds(Collection<Long> ids);
}
