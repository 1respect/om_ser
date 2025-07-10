package cn.iocoder.yudao.module.monitor.controller.admin.template.alertrule.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 报警规则分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AlertRulePageReqVO extends PageParam {
    private Long id;

    @Schema(description = "报警名称", example = "芋艿")
    private String alertName;

    @Schema(description = "计算类型", example = "2")
    private String calculationType;

    @Schema(description = "监控的指标", example = "28391")
    private String monitorIndexId;


    @Schema(description = "统计周期")
    private String calculatioCycle;

    @Schema(description = "报警级别")
    private Integer alertLevel;

    @Schema(description = "告警内容模板")
    private String contentTemplate;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = " 创建者")
    private String creator;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "外键，关联monitor_template", example = "6065")
    private Long templateId;



}