package cn.iocoder.yudao.module.monitor.controller.admin.template.metricconfig;

import cn.iocoder.yudao.module.monitor.controller.admin.alarm.level.vo.AlarmLevelSimpleVO;
import cn.iocoder.yudao.module.monitor.convert.alarm.AlarmInfoConvert;
import cn.iocoder.yudao.module.monitor.convert.template.MetricConfigConvert;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmLevelDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.center.CenterInfoDO;
import cn.iocoder.yudao.module.monitor.dal.mysql.center.CenterInfoMapper;
import cn.iocoder.yudao.module.monitor.dal.mysql.template.MetricConfigMapper;
import cn.iocoder.yudao.module.system.dal.dataobject.monitor.MonitorTypeDO;
import cn.iocoder.yudao.module.system.dal.mysql.monitor.MonitorTypeMapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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

import cn.iocoder.yudao.module.monitor.controller.admin.template.metricconfig.vo.*;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.MetricConfigDO;
import cn.iocoder.yudao.module.monitor.service.template.MetricConfigService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "管理后台 - 监控指标配置表(根据创建的监控对象层级结构 设置监控指标)")
@RestController
@RequestMapping("/monitor/metric-config")
@Validated
public class MetricConfigController {

    @Resource
    private MetricConfigService metricConfigService;
    @Resource
    private CenterInfoMapper centerInfoMapper;
    @Resource
    private MonitorTypeMapper monitorTypeMapper;

    @PostMapping("/create")
    @Operation(summary = "创建监控指标配置表(根据创建的监控对象层级结构 设置监控指标)")
    @PreAuthorize("@ss.hasPermission('monitor:metric-config:create')")
    public CommonResult<Long> createMetricConfig(@Valid @RequestBody MetricConfigSaveReqVO createReqVO) {
        return success(metricConfigService.createMetricConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新监控指标配置表(根据创建的监控对象层级结构 设置监控指标)")
    @PreAuthorize("@ss.hasPermission('monitor:metric-config:update')")
    public CommonResult<Boolean> updateMetricConfig(@Valid @RequestBody MetricConfigSaveReqVO updateReqVO) {
        metricConfigService.updateMetricConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除监控指标配置表(根据创建的监控对象层级结构 设置监控指标)")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('monitor:metric-config:delete')")
    public CommonResult<Boolean> deleteMetricConfig(@RequestParam("id") Long id) {
        metricConfigService.deleteMetricConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得监控指标配置表(根据创建的监控对象层级结构 设置监控指标)")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('monitor:metric-config:query')")
    public CommonResult<MetricConfigRespVO> getMetricConfig(@RequestParam("id") Long id) {
        MetricConfigDO metricConfig = metricConfigService.getMetricConfig(id);
        MetricConfigRespVO vo = MetricConfigConvert.INSTANCE.convert(metricConfig);

        if (metricConfig != null && metricConfig.getCenterId()!=null){
            CenterInfoDO center = centerInfoMapper.selectById(metricConfig.getCenterId());
            vo.setCenterName(center != null ? center.getName() : null);
        }

        if (metricConfig != null && metricConfig.getObjTypeId()!=null){
            MonitorTypeDO monitorType = monitorTypeMapper.selectById(metricConfig.getObjTypeId());
            vo.setObjTypeName(monitorType != null ? monitorType.getName() : null);
        }
        return success(vo);
    }

    @GetMapping("/page")
    @Operation(summary = "获得监控指标配置表(根据创建的监控对象层级结构 设置监控指标)分页")
    @PreAuthorize("@ss.hasPermission('monitor:metric-config:query')")
    public CommonResult<PageResult<MetricConfigRespVO>> getMetricConfigPage(@Valid MetricConfigPageReqVO pageReqVO) {
        PageResult<MetricConfigRespVO> pageResult = metricConfigService.getMetricConfigPage(pageReqVO);

        Set< Long> centerIds = pageResult.getList().stream()
                .map(MetricConfigRespVO::getCenterId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, String> centerNameMap = new HashMap<>();
        if (!centerIds.isEmpty()) {
            centerInfoMapper.selectBatchIds(centerIds)
                    .forEach(center -> centerNameMap.put(center.getId(), center.getName()));
        }

        Set< Long> objTypeIds = pageResult.getList().stream()
                .map(MetricConfigRespVO::getObjTypeId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, String> objTypeNameMap = new HashMap<>();
        if (!objTypeIds.isEmpty()) {
            monitorTypeMapper.selectBatchIds(objTypeIds)
                    .forEach(monitorType -> objTypeNameMap.put(monitorType.getId(), monitorType.getName()));
        }

        pageResult.getList().forEach(vo -> {
            vo.setCenterName(centerNameMap.get(vo.getCenterId()));
            vo.setObjTypeName(objTypeNameMap.get(vo.getObjTypeId()));
        });
        return success(pageResult);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出监控指标配置表(根据创建的监控对象层级结构 设置监控指标) Excel")
    @PreAuthorize("@ss.hasPermission('monitor:metric-config:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMetricConfigExcel(@Valid MetricConfigPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MetricConfigRespVO> list = metricConfigService.getMetricConfigPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "监控指标配置表(根据创建的监控对象层级结构 设置监控指标).xls", "数据", MetricConfigRespVO.class,
                        BeanUtils.toBean(list, MetricConfigRespVO.class));
    }

    @GetMapping("/page-by-template")
    @Operation(summary = "根据模板ID分页查询监控指标")
    @PreAuthorize("@ss.hasPermission('monitor:metric-config:query')")
    public CommonResult<PageResult<MetricConfigRespVO>> getMetricConfigPageByTemplate(
            @RequestParam("templateId") Long templateId,
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        MetricConfigPageReqVO pageReqVO = new MetricConfigPageReqVO();
        pageReqVO.setTemplateId(templateId);
        pageReqVO.setPageNo(pageNo);
        pageReqVO.setPageSize(pageSize);
        return getMetricConfigPage(pageReqVO);
    }

    //根据templateId查询指标
    @GetMapping("/simple_list")
    public CommonResult<List<MetricConfigSimpleVO>> getSimpleList(@RequestParam("id") Long id) {
        List<MetricConfigDO> levels = metricConfigService.getSimpleList(id);
        return success(MetricConfigConvert.INSTANCE.convertSimpleList(levels));
    }
}