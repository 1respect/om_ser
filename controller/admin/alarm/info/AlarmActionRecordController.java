package cn.iocoder.yudao.module.monitor.controller.admin.alarm.info;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo.AlarmActionRecordCreateReqVO;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo.AlarmActionRecordRespVO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmActionRecordDO;
import cn.iocoder.yudao.module.monitor.dal.mysql.alarm.AlarmActionRecordMapper;
import cn.iocoder.yudao.module.monitor.service.alarm.AlarmActionRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;

@Tag(name = "管理后台 - 告警处理信息")
@RestController
@RequestMapping("/alarm/alarm-action-record")
public class AlarmActionRecordController {

    @Resource
    private AlarmActionRecordService actionRecordService;
    @Resource
    private AlarmActionRecordMapper recordMapper;

    @PostMapping("/create")
    public CommonResult<Boolean> createRecord(@Valid @RequestBody AlarmActionRecordCreateReqVO reqVO) {
        Long userId = cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId();
        actionRecordService.createAndFinishAlarm(reqVO, userId);
        return CommonResult.success(true);
    }

    /**
     * 获取指定报警的最近一条处理记录
     */
    @GetMapping("/get-latest")
    public CommonResult<AlarmActionRecordRespVO> getLatestRecord(@RequestParam("alertRecordId") Long alertRecordId) {
        AlarmActionRecordDO record = recordMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<AlarmActionRecordDO>()
                        .eq(AlarmActionRecordDO::getAlertRecordId, alertRecordId)
                        .orderByDesc(AlarmActionRecordDO::getProcessTime)
                        .last("limit 1")
        );
        if (record == null) {
            return CommonResult.success(null);
        }
        AlarmActionRecordRespVO vo = new AlarmActionRecordRespVO();
        vo.setId(record.getId());
        vo.setAlertRecordId(record.getAlertRecordId());
        vo.setUserId(record.getUserId());
        vo.setProcessType(record.getProcessType());
        vo.setProcessTime(record.getProcessTime());
        vo.setDescription(record.getDescription());
        vo.setRemark(record.getRemark());
        vo.setCreateTime(record.getCreateTime());
        return CommonResult.success(vo);
    }
}