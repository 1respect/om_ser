package cn.iocoder.yudao.module.monitor.controller.admin.template.metricconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 监控指标配置表(根据创建的监控对象层级结构 设置监控指标)新增/修改 Request VO")
@Data
public class MetricConfigSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "10164")
    private Long id;

    @Schema(description = "监控对象类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16351")
    @NotNull(message = "监控对象类型ID不能为空")
    private Long objTypeId;

    @Schema(description = "对象层级")
    private String level;

    @Schema(description = "指标名称")
    private String metrics;

    @Schema(description = "阈值范围")
    private String thresholdInterval;

    @Schema(description = "阈值范围的数值")
    private String value;

    @Schema(description = "数值单位")
    private String unit;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "外键，关联monitor_template", example = "16677")
    private Long templateId;

    private Long centerId;

    private String metricsAlias;

    private String analyticMethod;

}