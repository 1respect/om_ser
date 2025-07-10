package cn.iocoder.yudao.module.monitor.controller.admin.alarm.info;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo.*;
import cn.iocoder.yudao.module.monitor.convert.alarm.AlarmInfoConvert;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmInfoDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmLevelDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.asset.MonitorEntityTempDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.center.CenterInfoDO;
import cn.iocoder.yudao.module.monitor.dal.mysql.alarm.AlarmLevelMapper;
import cn.iocoder.yudao.module.monitor.dal.mysql.center.CenterInfoMapper;
import cn.iocoder.yudao.module.monitor.service.alarm.AlarmInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import cn.iocoder.yudao.module.monitor.dal.mysql.alarm.MonitorEntityTempMapper;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 告警信息")
@RestController
@RequestMapping("/alarm/alarm-info")
public class AlarmInfoController {

    @Resource
    private AlarmInfoService alarmInfoService;
    @Resource
    private MonitorEntityTempMapper monitorEntityTempMapper;
    @Resource
    private CenterInfoMapper centerInfoMapper;
    @Resource
    private AlarmLevelMapper alarmLevelMapper; // 或用 AlarmLevelService 批量查

    @PostMapping("/create")
    public CommonResult<Long> createAlarmInfo(@Valid @RequestBody AlarmInfoCreateReqVO reqVO) {
        return success(alarmInfoService.createAlarmInfo(reqVO));
    }

    @PutMapping("/update")
    public CommonResult<Boolean> updateAlarmInfo(@Valid @RequestBody AlarmInfoUpdateReqVO reqVO) {
        alarmInfoService.updateAlarmInfo(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteAlarmInfo(@RequestParam("id") Long id) {
        alarmInfoService.deleteAlarmInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    public CommonResult<AlarmInfoRespVO> getAlarmInfo(@RequestParam("id") Long id) {
        AlarmInfoDO info = alarmInfoService.getAlarmInfo(id);
        AlarmInfoRespVO vo = AlarmInfoConvert.INSTANCE.convert(info);
        // 查询监控对象名
        if (info != null && info.getMonitorObjId() != null) {
            MonitorEntityTempDO obj = monitorEntityTempMapper.selectById(info.getMonitorObjId());
            vo.setMonitorObjName(obj != null ? obj.getName() : null);
        }
        // 查询中心名称
        if (info != null && info.getCenterId() != null) {
            CenterInfoDO center = centerInfoMapper.selectById(info.getCenterId());
            vo.setCenterName(center != null ? center.getName() : null);
        }
        // 查询告警等级名和颜色
        if (info != null && info.getAlarmLevelId() != null) {
            AlarmLevelDO level = alarmLevelMapper.selectById(info.getAlarmLevelId());
            if (level != null) {
                vo.setAlarmLevelName(level.getLevel() + "级(" + level.getName() + ")");
                vo.setAlarmLevelColor(level.getColor());
            }
        }
        return success(vo);
    }

    @GetMapping("/page")
    public CommonResult<PageResult<AlarmInfoRespVO>> getAlarmInfoPage(@Valid AlarmInfoPageReqVO pageVO) {
        // 先根据监控对象名过滤出符合条件的所有id
        Set<Long> filterObjIds = null;
        if (pageVO.getMonitorObjName() != null && !pageVO.getMonitorObjName().isEmpty()) {
            List<MonitorEntityTempDO> objList = monitorEntityTempMapper.selectList(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MonitorEntityTempDO>()
                            .like(MonitorEntityTempDO::getName, pageVO.getMonitorObjName())
            );
            if (objList.isEmpty()) {
                return success(PageResult.empty());
            }
            filterObjIds = objList.stream().map(MonitorEntityTempDO::getId).collect(Collectors.toSet());
        }

        // 返回VO结果（service已组装分页结果）
        PageResult<AlarmInfoRespVO> pageResult = alarmInfoService.getAlarmInfoPage(pageVO, filterObjIds);

        // 批量查对象名
        Set<Long> objIds = pageResult.getList().stream()
                .map(AlarmInfoRespVO::getMonitorObjId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, String> objNameMap = new HashMap<>();
        if (!objIds.isEmpty()) {
            monitorEntityTempMapper.selectBatchIds(objIds).forEach(
                    obj -> objNameMap.put(obj.getId(), obj.getName())
            );
        }
        // 批量查中心名
        Set<Long> centerIds = pageResult.getList().stream()
                .map(AlarmInfoRespVO::getCenterId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, String> centerNameMap = new HashMap<>();
        if (!centerIds.isEmpty()) {
            centerInfoMapper.selectBatchIds(centerIds).forEach(
                    center -> centerNameMap.put(center.getId(), center.getName())
            );
        }
        // 批量查等级名和颜色
        Set<Long> levelIds = pageResult.getList().stream()
                .map(AlarmInfoRespVO::getAlarmLevelId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, AlarmLevelDO> levelMap = new HashMap<>();
        if (!levelIds.isEmpty()) {
            alarmLevelMapper.selectBatchIds(levelIds).forEach(
                    level -> levelMap.put(level.getId(), level)
            );
        }
        // 填充名称
        pageResult.getList().forEach(vo -> {
            vo.setMonitorObjName(objNameMap.get(vo.getMonitorObjId()));
            vo.setCenterName(centerNameMap.get(vo.getCenterId()));
            AlarmLevelDO level = levelMap.get(vo.getAlarmLevelId());
            if (level != null) {
                vo.setAlarmLevelName(level.getLevel() + "级(" + level.getName() + ")");
                vo.setAlarmLevelColor(level.getColor());
            }
        });
        return success(pageResult);
    }
}