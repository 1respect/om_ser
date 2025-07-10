package cn.iocoder.yudao.module.monitor.controller.admin.asset.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Schema(description = "管理后台 - 监控对象层级结构新增/修改 Request VO")
@Data
public class MonitorEntityTempSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "23858")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "对象名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("对象名称")
    private String name;

    @Schema(description = "对象类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "24709")

    private Long typeId;

    @Schema(description = "父对象ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4304")

    private Long parentId;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    private String templateId;

    private Long fid;

    private String snCode;
}