package cn.iocoder.yudao.module.monitor.controller.admin.template.metricconfig.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;


@Schema(description = "管理后台 - 监控指标配置表(根据创建的监控对象层级结构 设置监控指标)分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MetricConfigPageReqVO extends PageParam {

    @Schema(description = "监控对象类型ID", example = "16351")
    private Long objTypeId;

    @Schema(description = "外键，关联monitor_template", example = "16677")
    private Long templateId;

    private Long centerId;

    private String metricsAlias;

    private String analyticMethod;
}