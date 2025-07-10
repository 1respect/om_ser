package cn.iocoder.yudao.module.monitor.service.alarm.impl;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.level.vo.*;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmLevelDO;
import cn.iocoder.yudao.module.monitor.dal.mysql.alarm.AlarmLevelMapper;
import cn.iocoder.yudao.module.monitor.service.alarm.AlarmLevelService;
import cn.iocoder.yudao.module.monitor.convert.alarm.AlarmLevelConvert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AlarmLevelServiceImpl implements AlarmLevelService {

    @Autowired
    private AlarmLevelMapper alarmLevelMapper;

    @Override
    public Long createAlarmLevel(AlarmLevelCreateReqVO reqVO) {
        AlarmLevelDO level = AlarmLevelConvert.INSTANCE.convert(reqVO);
        level.setCreateTime(LocalDateTime.now());
        level.setUpdateTime(LocalDateTime.now());
        alarmLevelMapper.insert(level);
        return level.getId();
    }

    @Override
    public void updateAlarmLevel(AlarmLevelUpdateReqVO reqVO) {
        AlarmLevelDO level = AlarmLevelConvert.INSTANCE.convert(reqVO);
        alarmLevelMapper.updateById(level);
    }

    @Override
    public void deleteAlarmLevel(Long id) {
        alarmLevelMapper.deleteById(id);
    }

    @Override
    public AlarmLevelDO getAlarmLevel(Long id) {
        return alarmLevelMapper.selectById(id);
    }

    @Override
    public PageResult<AlarmLevelDO> getAlarmLevelPage(AlarmLevelPageReqVO reqVO) {
        LambdaQueryWrapper<AlarmLevelDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(reqVO.getLevel() != null, AlarmLevelDO::getLevel, reqVO.getLevel())
                .like(reqVO.getName() != null, AlarmLevelDO::getName, reqVO.getName())
                .orderByAsc(AlarmLevelDO::getSort);
        return alarmLevelMapper.selectPage(reqVO, wrapper);
    }

    @Override
    public List<AlarmLevelDO> getAlarmLevelList() {
        return alarmLevelMapper.selectList(null);
    }


    @Override
    public List<AlarmLevelDO> getSimpleList() {
        return alarmLevelMapper.selectList(
                new LambdaQueryWrapper<AlarmLevelDO>()
                        .orderByAsc(AlarmLevelDO::getSort)
        );
    }

    @Override
    public Map<Long, AlarmLevelDO> getMapByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyMap();
        List<AlarmLevelDO> levels = alarmLevelMapper.selectBatchIds(ids);
        return levels.stream().collect(Collectors.toMap(AlarmLevelDO::getId, v -> v));
    }
}
