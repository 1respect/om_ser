package cn.iocoder.yudao.module.monitor.controller.admin.template.alertrule;

import cn.iocoder.yudao.module.monitor.controller.admin.template.alertrule.vo.AlertRulePageReqVO;
import cn.iocoder.yudao.module.monitor.controller.admin.template.alertrule.vo.AlertRuleRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.template.alertrule.vo.AlertRuleSaveReqVO;
import cn.iocoder.yudao.module.monitor.controller.admin.template.metricconfig.vo.MetricConfigRespVO;
import cn.iocoder.yudao.module.monitor.dal.mysql.center.CenterInfoMapper;
import cn.iocoder.yudao.module.system.dal.mysql.monitor.MonitorTypeMapper;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.monitor.dal.dataobject.template.AlertRuleDO;
import cn.iocoder.yudao.module.monitor.service.template.AlertRuleService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "管理后台 - 报警规则")
@RestController
@RequestMapping("/monitor/alert-rule")
@Validated
public class AlertRuleController {

    @Resource
    private AlertRuleService alertRuleService;

    @Resource
    private CenterInfoMapper centerInfoMapper;
    @Resource
    private MonitorTypeMapper monitorTypeMapper;

    @PostMapping("/create")
    @Operation(summary = "创建报警规则")
    @PreAuthorize("@ss.hasPermission('monitor:alert-rule:create')")
    public CommonResult<Long> createAlertRule(@Valid @RequestBody AlertRuleSaveReqVO createReqVO) {
        return success(alertRuleService.createAlertRule(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新报警规则")
    @PreAuthorize("@ss.hasPermission('monitor:alert-rule:update')")
    public CommonResult<Boolean> updateAlertRule(@Valid @RequestBody AlertRuleSaveReqVO updateReqVO) {
        alertRuleService.updateAlertRule(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除报警规则")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('monitor:alert-rule:delete')")
    public CommonResult<Boolean> deleteAlertRule(@RequestParam("id") Long id) {
        alertRuleService.deleteAlertRule(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得报警规则")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('monitor:alert-rule:query')")
    public CommonResult<AlertRuleRespVO> getAlertRule(@RequestParam("id") Long id) {
        AlertRuleDO alertRule = alertRuleService.getAlertRule(id);
        return success(BeanUtils.toBean(alertRule, AlertRuleRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得报警规则分页")
    @PreAuthorize("@ss.hasPermission('monitor:alert-rule:query')")
    public CommonResult<PageResult<AlertRuleRespVO>> getAlertRulePage(@Valid AlertRulePageReqVO pageReqVO) {
        return success(alertRuleService.getAlertRulePage(pageReqVO));
    }

    /** 不需要

    @GetMapping("/export-excel")
    @Operation(summary = "导出报警规则 Excel")
    @PreAuthorize("@ss.hasPermission('monitor:alert-rule:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAlertRuleExcel(@Valid AlertRulePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AlertRuleDO> list = alertRuleService.getAlertRulePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "报警规则.xls", "数据", AlertRuleRespVO.class,
                        BeanUtils.toBean(list, AlertRuleRespVO.class));
    }
    */

}