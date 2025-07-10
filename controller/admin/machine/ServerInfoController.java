package cn.iocoder.yudao.module.monitor.controller.admin.machine;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.monitor.controller.admin.machine.dto.TestBmcConnectionDTO;
import cn.iocoder.yudao.module.monitor.controller.admin.machine.vo.*;
import cn.iocoder.yudao.module.monitor.convert.machine.ServerInfoConvert;
import cn.iocoder.yudao.module.monitor.dal.dataobject.machine.ServerInfoDO;
import cn.iocoder.yudao.module.monitor.service.machine.ImportExcelService;
import cn.iocoder.yudao.module.monitor.service.machine.ServerInfoService;
import com.alibaba.excel.EasyExcel;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.InputStream;
import java.util.*;
import java.io.IOException;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 物理机管理")
@RestController
@RequestMapping("/monitor/server-info")
@Validated
public class ServerInfoController {

    @Resource
    private ServerInfoService serverInfoService;
    @Resource
    ImportExcelService importExcelService;

    @PostMapping("/create")
    @Operation(summary = "创建物理机管理")
    @PreAuthorize("@ss.hasPermission('monitor:server-info:create')")
    public CommonResult<Long> createServerInfo(@Valid @RequestBody ServerInfoSaveReqVO createReqVO) {
        return success(serverInfoService.createServerInfo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新物理机管理")
    @PreAuthorize("@ss.hasPermission('monitor:server-info:update')")
    public CommonResult<Boolean> updateServerInfo(@Valid @RequestBody ServerInfoSaveReqVO updateReqVO) {
        serverInfoService.updateServerInfo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除物理机管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('monitor:server-info:delete')")
    public CommonResult<Boolean> deleteServerInfo(@RequestParam("id") Long id) {
        serverInfoService.deleteServerInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得物理机管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('monitor:server-info:query')")
    public CommonResult<ServerInfoRespVO> getServerInfo(@RequestParam("id") Long id) {
        ServerInfoDO serverInfoDO = serverInfoService.getServerInfo(id);
        return success(BeanUtils.toBean(serverInfoDO, ServerInfoRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得物理机管理分页")
    public CommonResult<PageResult<ServerInfoRespVO>> getServerInfoPage(@Valid ServerInfoPageReqVO pageReqVO) {
        PageResult<ServerInfoDO> pageResult = serverInfoService.getServerInfoPage(pageReqVO);
        return success(new PageResult<>(
                ServerInfoConvert.INSTANCE.convertList(pageResult.getList()),
                pageResult.getTotal()
        ));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获取监控对象类型全列表", description = "主要用于前端下拉选项")
    public CommonResult<List<ServerInfoRespVO>> getSimpleServerList() {
        List<ServerInfoDO> list = serverInfoService.getServerInfoList(null);
        return success(ServerInfoConvert.INSTANCE.convertSimpleList(list));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物理机管理 Excel")
    @PreAuthorize("@ss.hasPermission('monitor:server-info:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportServerInfoDOExcel(@Valid ServerInfoPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ServerInfoDO> list = serverInfoService.getServerInfoPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物理机管理.xls", "数据", ServerInfoRespVO.class,
                        BeanUtils.toBean(list, ServerInfoRespVO.class));
    }

    @PostMapping("/import")
    @Operation(summary = "导入设备")
    @PreAuthorize("@ss.hasPermission('monitor:server-info:import')")
    public CommonResult<ServerImportRespVO> importExcelData(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "updateSupport", required = false, defaultValue = "false") Boolean updateSupport) {
        try {
            List<ServerImportExcelVO> list = ExcelUtils.read(file, ServerImportExcelVO.class);
            return success(importExcelService.importDevice(list, updateSupport));
        } catch (Exception e) {
            // 建议自定义异常类型，这里简化处理
            throw new RuntimeException("Excel导入失败：" + e.getMessage(), e);
        }
    }


    @PostMapping("/importEntityTemp")
    @Operation(summary = "导入模板接口数据")
    @Parameters({
            @Parameter(name = "file", description = "Excel 文件", required = true),
            @Parameter(name = "updateSupport", description = "是否支持更新，默认为 false", example = "true")
    })
    @PreAuthorize("@ss.hasPermission('system:user:import')")
    public CommonResult<ServerImportRespVO> importExcel(@RequestParam("file") MultipartFile file,
                                                      @RequestParam(value = "updateSupport", required = false, defaultValue = "false") Boolean updateSupport) throws Exception {
        List<MonitorEntityTempExcelVO> list = ExcelUtils.read(file, MonitorEntityTempExcelVO.class);
        return success(importExcelService.importDataList(list, updateSupport));
    }


    @GetMapping("/get-import-template")
    @Operation(summary = "获得导入物理机模板")
    public void importTemplate(HttpServletResponse response) throws IOException {
        List<ServerImportExcelVO> list = Arrays.asList(
                ServerImportExcelVO.builder().servername("WULIJI001").brand("HUAWEI").model("A50").sno("IOOO02JJK291").monitorEntityTempId(5L).build(),
                ServerImportExcelVO.builder().servername("HECIii").brand("h3ce").model("150").sno("IO33302JJK891").monitorEntityTempId(6L).build()
        );
        ExcelUtils.write(response, "物理机导入模板.xls", "数据", ServerImportExcelVO.class, list);
    }


    @PutMapping("/bootable")
    @Operation(summary = "物理机开机")
    public CommonResult<Boolean> bootable(@RequestParam("id") Long id) {
        return  serverInfoService.bootable(id);
    }

    @PutMapping("/shutdown")
    @Operation(summary = "物理机关机")
    public CommonResult<Boolean> shutDown(@RequestParam("id") Long id) {
        return  serverInfoService.shutDown(id);
    }

    @PutMapping("/restart")
    @Operation(summary = "物理机重启")
    public CommonResult<Boolean> restart(@RequestParam("id") Long id) {
        return  serverInfoService.restart(id);
    }

    @PutMapping("/cdInstall")
    @Operation(summary = "物理机重装系统")
    public CommonResult<Boolean> cdInstall(@RequestParam("id") Long id) {
        return  serverInfoService.cdInstall(id);
    }

    @PostMapping("/testBmcConnection")
    @Operation(summary = "测试BMC连接并返回物理信息")
    public CommonResult<MachineInfoVO> testBmcConnection(@Valid @RequestBody TestBmcConnectionDTO testBmcConnectionDTO) {
        return  success(serverInfoService.testBmcConnection(testBmcConnectionDTO));
    }


}
