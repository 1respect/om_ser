package cn.iocoder.yudao.module.monitor.service.alarm.impl;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo.*;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmInfoDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmLevelDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.asset.MonitorEntityTempDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.center.CenterInfoDO;
import cn.iocoder.yudao.module.monitor.dal.mysql.alarm.AlarmInfoMapper;
import cn.iocoder.yudao.module.monitor.dal.mysql.alarm.AlarmLevelMapper;
import cn.iocoder.yudao.module.monitor.dal.mysql.alarm.MonitorEntityTempMapper;
import cn.iocoder.yudao.module.monitor.dal.mysql.center.CenterInfoMapper;
import cn.iocoder.yudao.module.monitor.service.alarm.AlarmInfoService;
import cn.iocoder.yudao.module.monitor.convert.alarm.AlarmInfoConvert;
import cn.iocoder.yudao.module.monitor.service.notice.AlarmNoticeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlarmInfoServiceImpl implements AlarmInfoService {

    @Resource
    private AlarmInfoMapper alarmInfoMapper;
    @Resource
    private CenterInfoMapper centerInfoMapper;
    @Resource
    private MonitorEntityTempMapper monitorEntityTempMapper;
    @Resource
    private AlarmLevelMapper alarmLevelMapper;
    @Resource
    private AlarmNoticeService alarmNoticeService;

    @Override
    public Long createAlarmInfo(AlarmInfoCreateReqVO reqVO) {
        AlarmInfoDO info = AlarmInfoConvert.INSTANCE.convert(reqVO);
        info.setCreateTime(LocalDateTime.now());
        info.setUpdateTime(LocalDateTime.now());
        info.setAlertTime(LocalDateTime.now());
        alarmInfoMapper.insert(info);
        //alarmNoticeService.sendAlarmNotice(info.getId());
        return info.getId();
    }

    @Override
    public void updateAlarmInfo(AlarmInfoUpdateReqVO reqVO) {
        AlarmInfoDO info = AlarmInfoConvert.INSTANCE.convert(reqVO);
        info.setUpdateTime(LocalDateTime.now());
        alarmInfoMapper.updateById(info);
    }

    @Override
    public void deleteAlarmInfo(Long id) {
        alarmInfoMapper.deleteById(id);
    }

    @Override
    public AlarmInfoDO getAlarmInfo(Long id) {
        return alarmInfoMapper.selectById(id);
    }

    /**
     * 分页查询，返回 AlarmInfoRespVO，包含 centerName、monitorObjName、alarmLevelName、alarmLevelColor 字段
     */
    @Override
    public PageResult<AlarmInfoRespVO> getAlarmInfoPage(AlarmInfoPageReqVO pageReqVO, Set<Long> filterObjIds) {
        LambdaQueryWrapper<AlarmInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(pageReqVO.getCenterId() != null, AlarmInfoDO::getCenterId, pageReqVO.getCenterId());
        wrapper.eq(pageReqVO.getAlarmLevelId() != null, AlarmInfoDO::getAlarmLevelId, pageReqVO.getAlarmLevelId());
        wrapper.eq(pageReqVO.getStatus() != null, AlarmInfoDO::getStatus, pageReqVO.getStatus());
        if (filterObjIds != null) {
            wrapper.in(!filterObjIds.isEmpty(), AlarmInfoDO::getMonitorObjId, filterObjIds);
        }
        wrapper.orderByDesc(AlarmInfoDO::getCreateTime);

        IPage<AlarmInfoDO> mpPage = alarmInfoMapper.selectPage(
                new Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize()),
                wrapper
        );
        List<AlarmInfoDO> list = mpPage.getRecords();

        // 批量查询centerName
        Set<Long> centerIds = list.stream()
                .map(AlarmInfoDO::getCenterId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, String> centerNameMap = centerIds.isEmpty() ? Collections.emptyMap() :
                centerInfoMapper.selectBatchIds(centerIds)
                        .stream()
                        .collect(Collectors.toMap(CenterInfoDO::getId, CenterInfoDO::getName));

        // 批量查询监控对象名
        Set<Long> objIds = list.stream().map(AlarmInfoDO::getMonitorObjId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, String> objNameMap = objIds.isEmpty() ? Collections.emptyMap() :
                monitorEntityTempMapper.selectBatchIds(objIds)
                        .stream().collect(Collectors.toMap(MonitorEntityTempDO::getId, MonitorEntityTempDO::getName));

        // 批量查等级
        Set<Long> levelIds = list.stream().map(AlarmInfoDO::getAlarmLevelId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, AlarmLevelDO> levelMap = levelIds.isEmpty() ? Collections.emptyMap() :
                alarmLevelMapper.selectBatchIds(levelIds)
                        .stream().collect(Collectors.toMap(AlarmLevelDO::getId, v -> v));

        // 转换并补全字段
        List<AlarmInfoRespVO> voList = list.stream().map(alarm -> {
            AlarmInfoRespVO vo = AlarmInfoConvert.INSTANCE.convert(alarm);
            vo.setCenterName(centerNameMap.get(alarm.getCenterId()));
            vo.setMonitorObjName(objNameMap.get(alarm.getMonitorObjId()));
            AlarmLevelDO level = levelMap.get(alarm.getAlarmLevelId());
            if (level != null) {
                vo.setAlarmLevelName(level.getLevel() + "级(" + level.getName() + ")");
                vo.setAlarmLevelColor(level.getColor());
            }
            return vo;
        }).collect(Collectors.toList());

        return new PageResult<>(voList, mpPage.getTotal());
    }

}