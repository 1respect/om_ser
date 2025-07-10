package cn.iocoder.yudao.module.monitor.controller.admin.template.metricconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 监控指标配置表(根据创建的监控对象层级结构 设置监控指标) Response VO")
@Data
@ExcelIgnoreUnannotated
public class MetricConfigRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "10164")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "监控对象类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16351")
    @ExcelProperty("监控对象类型ID")
    private Long objTypeId;

    @Schema(description = "监控对象类型父级ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15436")
    @ExcelProperty("监控对象类型父级ID")
    private Long parentObjTypeId;

    @Schema(description = "对象层级")
    @ExcelProperty("对象层级")
    private String level;

    @Schema(description = "指标名称")
    @ExcelProperty("指标名称")
    private String metrics;

    @Schema(description = "阈值范围")
    @ExcelProperty("阈值范围")
    private String thresholdInterval;

    @Schema(description = "阈值范围的数值")
    @ExcelProperty("阈值范围的数值")
    private String value;

    @Schema(description = "数值单位")
    @ExcelProperty("数值单位")
    private String unit;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "外键，关联monitor_template", example = "16677")
    @ExcelProperty("外键，关联monitor_template")
    private Long templateId;

    private Long centerId;

    private String metricsAlias;

    private String analyticMethod;
    private String centerName;
    private String objTypeName;

}
