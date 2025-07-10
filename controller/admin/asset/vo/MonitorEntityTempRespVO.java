package cn.iocoder.yudao.module.monitor.controller.admin.asset.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 监控对象层级结构 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MonitorEntityTempRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "23858")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "对象名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("对象名称")
    private String name;

    @Schema(description = "对象类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "24709")
    @ExcelProperty("对象类型")
    private Long typeId;

    @Schema(description = "父对象ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4304")
    @ExcelProperty("父对象ID")
    private Long parentId;

    @Schema(description = "备注", example = "你猜")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    private String templateId;

    // 新增字段，用于存储 type_name
    private String typeCode;

}