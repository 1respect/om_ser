package cn.iocoder.yudao.module.monitor.controller.admin.template;

import cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo.AlarmInfoRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.template.metricconfig.vo.MetricConfigRespVO;
import cn.iocoder.yudao.module.monitor.convert.alarm.AlarmInfoConvert;
import cn.iocoder.yudao.module.monitor.convert.template.TemplateInfoConvert;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmLevelDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.asset.MonitorEntityTempDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.center.CenterInfoDO;
import cn.iocoder.yudao.module.monitor.dal.mysql.alarm.MonitorEntityTempMapper;
import cn.iocoder.yudao.module.monitor.dal.mysql.center.CenterInfoMapper;
import cn.iocoder.yudao.module.monitor.dal.mysql.template.MetricConfigMapper;
import cn.iocoder.yudao.module.monitor.service.template.MetricConfigService;
import cn.iocoder.yudao.module.system.dal.dataobject.monitor.MonitorTypeDO;
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

import cn.iocoder.yudao.module.monitor.controller.admin.template.vo.*;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.TemplateDO;
import cn.iocoder.yudao.module.monitor.service.template.TemplateService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "管理后台 -  监控模板")
@RestController
@RequestMapping("/monitor/template")
@Validated
public class TemplateController {

    @Resource
    private TemplateService templateService;
    @Resource
    private MonitorEntityTempMapper monitorEntityTempMapper;
    @Resource
    private CenterInfoMapper centerInfoMapper;
    @Resource
    private MetricConfigMapper metricConfigMapper;
    @Resource
    private MonitorTypeMapper monitorTypeMapper;
    @Resource
    private MetricConfigService metricConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建 监控模板")
    @PreAuthorize("@ss.hasPermission('monitor:template:create')")
    public CommonResult<Long> createTemplate(@Valid @RequestBody TemplateSaveReqVO createReqVO) {
        return success(templateService.createTemplate(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新 监控模板")
    @PreAuthorize("@ss.hasPermission('monitor:template:update')")
    public CommonResult<Boolean> updateTemplate(@Valid @RequestBody TemplateSaveReqVO updateReqVO) {
        templateService.updateTemplate(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除 监控模板")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('monitor:template:delete')")
    public CommonResult<Boolean> deleteTemplate(@RequestParam("id") Long id) {
        templateService.deleteTemplate(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得 监控模板")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('monitor:template:query')")
    public CommonResult<TemplateRespVO> getTemplate(@RequestParam("id") Long id) {
        TemplateDO template = templateService.getTemplate(id);
        TemplateRespVO vo = TemplateInfoConvert.INSTANCE.convert(template);

        // 查询监控对象名
        if (template != null && template.getTypeId() != null) {
            MonitorTypeDO type = monitorTypeMapper.selectById(template.getTypeId());
            vo.setTypeName(type != null ? type.getName() : null);
        }

        // 查询中心名称
        if (template != null && template.getCenterId() != null) {
            CenterInfoDO center = centerInfoMapper.selectById(template.getCenterId());
            vo.setCenterName(center != null ? center.getName() : null);
        }

        // 查询并设置所有指标
        if (template != null && template.getId() != null) {
            List<MetricConfigRespVO> metrics = metricConfigService.getMetricsByTemplateId(template.getId());
            vo.setMetrics(metrics);
        }

        return success(vo);
    }

    @GetMapping("/page")
    @Operation(summary = "获得 监控模板分页")
    @PreAuthorize("@ss.hasPermission('monitor:template:query')")
    public CommonResult<PageResult<TemplateRespVO>> getTemplatePage(@Valid TemplatePageReqVO pageReqVO) {
        PageResult<TemplateRespVO> pageResult = templateService.getTemplatePage(pageReqVO);

        // 批量查类型名
        Set<Long> typeIds = pageResult.getList().stream()
                .map(TemplateRespVO::getTypeId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, String> typeNameMap = new HashMap<>();
        if (!typeIds.isEmpty()) {
            monitorTypeMapper.selectBatchIds(typeIds).forEach(
                    obj -> typeNameMap.put(obj.getId(), obj.getName())
            );
        }
        // 批量查中心名
        Set<Long> centerIds = pageResult.getList().stream()
                .map(TemplateRespVO::getCenterId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, String> centerNameMap = new HashMap<>();
        if (!centerIds.isEmpty()) {
            centerInfoMapper.selectBatchIds(centerIds).forEach(
                    center -> centerNameMap.put(center.getId(), center.getName())
            );
        }

        // 填充名称
        pageResult.getList().forEach(vo -> {
            vo.setTypeName(typeNameMap.get(vo.getTypeId()));
            vo.setCenterName(centerNameMap.get(vo.getCenterId()));
        });
        return success(pageResult);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出 监控模板 Excel")
    @PreAuthorize("@ss.hasPermission('monitor:template:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportTemplateExcel(@Valid TemplatePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<TemplateRespVO> list = templateService.getTemplatePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, " 监控模板.xls", "数据", TemplateRespVO.class,
                        BeanUtils.toBean(list, TemplateRespVO.class));
    }

}