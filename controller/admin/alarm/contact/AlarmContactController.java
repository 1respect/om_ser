package cn.iocoder.yudao.module.monitor.controller.admin.alarm.contact;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.contact.vo.*;
import cn.iocoder.yudao.module.monitor.convert.template.TemplateInfoConvert;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmContactDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.center.CenterInfoDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.TemplateDO;
import cn.iocoder.yudao.module.monitor.dal.mysql.center.CenterInfoMapper;
import cn.iocoder.yudao.module.monitor.service.alarm.AlarmContactService;
import cn.iocoder.yudao.module.monitor.convert.alarm.AlarmContactConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.monitor.MonitorTypeDO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - 紧急联系人")
@RestController
@RequestMapping("/alarm/alarm-contact")
public class AlarmContactController {

    @Resource
    private AlarmContactService alarmContactService;
    @Resource
    private CenterInfoMapper centerInfoMapper;


    // 创建
    @PostMapping("/create")
    public CommonResult<Long> createAlarmContact(@Valid @RequestBody AlarmContactCreateReqVO reqVO) {
        return success(alarmContactService.createAlarmContact(reqVO));
    }

    // 修改
    @PutMapping("/update")
    public CommonResult<Boolean> updateAlarmContact(@Valid @RequestBody AlarmContactUpdateReqVO reqVO) {
        alarmContactService.updateAlarmContact(reqVO);
        return success(true);
    }

    // 删除
    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteAlarmContact(@RequestParam("id") Long id) {
        alarmContactService.deleteAlarmContact(id);
        return success(true);
    }

    // 单条查询
    @GetMapping("/get")
    public CommonResult<AlarmContactRespVO> getAlarmContact(@RequestParam("id") Long id) {
        AlarmContactDO alarmContact = alarmContactService.getAlarmContact(id);
        AlarmContactRespVO vo = AlarmContactConvert.INSTANCE.convert(alarmContact);


        // 查询中心名称
        if (alarmContact != null && alarmContact.getCenterId() != null) {
            CenterInfoDO center = centerInfoMapper.selectById(alarmContact.getCenterId());
            vo.setCenterName(center != null ? center.getName() : null);
        }

        return success(vo);
    }

    // 分页查询
    @GetMapping("/page")
    public CommonResult<PageResult<AlarmContactRespVO>> getAlarmContactPage(@Valid AlarmContactPageReqVO pageVO) {
        PageResult<AlarmContactRespVO> pageResult = alarmContactService.getAlarmContactPage(pageVO);

        // 批量查中心名
        Set<Long> centerIds = pageResult.getList().stream()
                .map(AlarmContactRespVO::getCenterId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, String> centerNameMap = new HashMap<>();
        if (!centerIds.isEmpty()) {
            centerInfoMapper.selectBatchIds(centerIds).forEach(
                    center -> centerNameMap.put(center.getId(), center.getName())
            );
        }

        // 填充名称
        pageResult.getList().forEach(vo -> {
            vo.setCenterName(centerNameMap.get(vo.getCenterId()));
        });
        return success(pageResult);
    }

    // 列表（不分页，通常用于下拉等）
    @GetMapping("/list")
    public CommonResult<List<AlarmContactPageRespVO>> getAlarmContactList() {
        List<AlarmContactDO> list = alarmContactService.getAlarmContactList();
        return success(AlarmContactConvert.INSTANCE.convertList(list));
    }


}