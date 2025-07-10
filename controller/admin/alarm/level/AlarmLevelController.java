package cn.iocoder.yudao.module.monitor.controller.admin.alarm.level;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.level.vo.AlarmLevelSimpleVO;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.level.vo.*;
import cn.iocoder.yudao.module.monitor.convert.alarm.AlarmInfoConvert;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmLevelDO;
import cn.iocoder.yudao.module.monitor.service.alarm.AlarmLevelService;
import cn.iocoder.yudao.module.monitor.convert.alarm.AlarmLevelConvert;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 告警等级")
@RestController
@RequestMapping("/alarm/alarm-level")
public class AlarmLevelController {

    @Resource
    private AlarmLevelService alarmLevelService;

    @PostMapping("/create")
    public CommonResult<Long> createAlarmLevel(@Valid @RequestBody AlarmLevelCreateReqVO reqVO) {
        return success(alarmLevelService.createAlarmLevel(reqVO));
    }

    @PutMapping("/update")
    public CommonResult<Boolean> updateAlarmLevel(@Valid @RequestBody AlarmLevelUpdateReqVO reqVO) {
        alarmLevelService.updateAlarmLevel(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteAlarmLevel(@RequestParam("id") Long id) {
        alarmLevelService.deleteAlarmLevel(id);
        return success(true);
    }

    @GetMapping("/get")
    public CommonResult<AlarmLevelRespVO> getAlarmLevel(@RequestParam("id") Long id) {
        AlarmLevelDO alarmLevel = alarmLevelService.getAlarmLevel(id);
        return success(AlarmLevelConvert.INSTANCE.convert(alarmLevel));
    }

    @GetMapping("/page")
    public CommonResult<PageResult<AlarmLevelRespVO>> getAlarmLevelPage(@Valid AlarmLevelPageReqVO pageVO) {
        PageResult<AlarmLevelDO> pageResult = alarmLevelService.getAlarmLevelPage(pageVO);
        return success(new PageResult<>(
                AlarmLevelConvert.INSTANCE.convertList(pageResult.getList()),
                pageResult.getTotal()
        ));
    }

    @GetMapping("/list")
    public CommonResult<List<AlarmLevelRespVO>> getAlarmLevelList() {
        List<AlarmLevelDO> list = alarmLevelService.getAlarmLevelList();
        return success(AlarmLevelConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/simple_list")
    public CommonResult<List<AlarmLevelSimpleVO>> getSimpleList() {
        List<AlarmLevelDO> levels = alarmLevelService.getSimpleList();
        return success(AlarmInfoConvert.INSTANCE.convertList2(levels));
    }
}