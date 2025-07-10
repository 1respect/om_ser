package cn.iocoder.yudao.module.monitor.controller.admin.asset;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.monitor.controller.admin.asset.vo.*;
import cn.iocoder.yudao.module.monitor.convert.asset.MonitorEntityTempConvert;
import cn.iocoder.yudao.module.monitor.dal.dataobject.asset.MonitorEntityTempDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.asset.MonitorEntityTempParentDO;
import cn.iocoder.yudao.module.monitor.service.asset.MonitorEntityTempService;
import cn.iocoder.yudao.module.system.controller.admin.monitor.vo.MonitorTypeSimpleRespVO;
import cn.iocoder.yudao.module.system.convert.monitor.MonitorTypeConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.monitor.MonitorTypeDO;
import cn.iocoder.yudao.module.system.service.monitor.MonitorTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 监控对象层级结构")
@RestController
@RequestMapping("/monitor/information-info")
public class AssetInfoController {
    @Resource
    private MonitorEntityTempService entityTempService;
    @Resource
    private MonitorTypeService monitorTypeService;
    @PostMapping("/create")
    @Operation(summary = "创建监控对象层级结构")
    public CommonResult<Long> createEntityTemp(@Valid @RequestBody MonitorEntityTempSaveReqVO createReqVO) {
        return success(entityTempService.createEntityTemp(createReqVO));
    }

    @GetMapping("/get-all")
    @Operation(summary = "获取监控对象类型全列表", description = "主要用于前端下拉选项")
    public CommonResult<List<MonitorTypeSimpleRespVO>> getSimpleMonitorTypeList() {
        List<MonitorTypeDO> list = monitorTypeService.getMonitorTypeList(null);
        list.sort(Comparator.comparing(MonitorTypeDO::getId));
        return success(MonitorTypeConvert.INSTANCE.convertSimpleList(list));
    }

    @PutMapping("/update")
    @Operation(summary = "更新监控对象层级结构")
    public CommonResult<Boolean> updateEntityTemp(@Valid @RequestBody MonitorEntityTempSaveReqVO updateReqVO) {
        entityTempService.updateEntityTemp(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除监控对象层级结构")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteEntityTemp(@RequestParam("id") Long id) {
        entityTempService.deleteEntityTemp(id);
        return success(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得监控对象层级结构分页")
    public CommonResult<PageResult<MonitorEntityTempRespVO>> getEntityTempPage(@Valid MonitorEntityTempPageReqVO pageReqVO) {
        PageResult<MonitorEntityTempDO> pageResult = entityTempService.getEntityTempPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MonitorEntityTempRespVO.class));
    }

    @GetMapping("/tree")
    @Operation(summary = "获得监控对象层级结构树")
    public CommonResult<List<MonitorEntityTempTreeVO>> getEntityTempTree() {
        List<MonitorEntityTempDO> entityTempDOList = entityTempService.getEntityTempTree();
        List<MonitorEntityTempTreeVO> tree = MonitorEntityTempConvert.INSTANCE.convertTree(entityTempDOList);
        return success(tree);
    }

    @GetMapping("/children")
    @Operation(summary = "获得监控对象层级结构子节点数据")
    @PreAuthorize("@ss.hasPermission('asset:entity-temp:query')")
    public CommonResult<List<MonitorEntityTempRespVO>> getEntityTempChildren(@RequestParam("id") Long parentId) {
        List<MonitorEntityTempNodeDO> children = entityTempService.getEntityTempChildren(parentId);
        return success(BeanUtils.toBean(children, MonitorEntityTempRespVO.class));
    }



    @GetMapping("/by-type-id")
    @Operation(summary = "根据监控类型获得监控对象")
    public CommonResult<List<MonitorEntityTempRespVO>> getEntityTempByTypeId(@RequestParam("id") Long typeId) {
        List<MonitorEntityTempNodeDO> entityTempDOList = entityTempService.getEntityTempByTypeId(typeId);
        return success(BeanUtils.toBean(entityTempDOList, MonitorEntityTempRespVO.class));
    }

    @PostMapping("/batch-set-template")
    @Operation(summary = "批量设置对象模板ID")
    public CommonResult<Boolean> batchSetTemplateToEntities(
            @RequestBody MonitorEntityBatchSetTemplateReqVO reqVO) {
        entityTempService.batchSetTemplateToEntities(reqVO.getTemplateId(), reqVO.getEntityIds());
        return success(true);
    }
    @GetMapping("/parent")
    @Operation(summary = "获得监控对象父级数据")
    @PreAuthorize("@ss.hasPermission('asset:entity-temp:query')")
    public CommonResult<List<MonitorEntityTempParentVO>> getParentDetali(@RequestParam("id") Long childId) {
        List<MonitorEntityTempParentDO> children = entityTempService.selectListByParentDetail(childId);
        return success(BeanUtils.toBean(children, MonitorEntityTempParentVO.class));
    }


    @GetMapping("/getEntityTempByCenterId")
    public CommonResult<List<MonitorEntityTempRespVO>> getEntityTempByCenterId(
            @RequestParam("id") Long centerId,@RequestParam("typeId") Long typeId) {
        return success(BeanUtils.toBean(entityTempService.getEntityTempByCenterId(centerId,typeId), MonitorEntityTempRespVO.class));
    }

}
