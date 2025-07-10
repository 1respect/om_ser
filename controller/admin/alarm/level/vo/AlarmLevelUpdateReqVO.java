package cn.iocoder.yudao.module.monitor.controller.admin.alarm.level.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AlarmLevelUpdateReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "等级", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer level;

    @Schema(description = "等级名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "颜色", requiredMode = Schema.RequiredMode.REQUIRED)
    private String color;

    @Schema(description = "邮件告警", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer mailNotice;

    @Schema(description = "短信告警", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer smsNotice;

    @Schema(description = "电话告警", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer phoneNotice;

    @Schema(description = "企业微信告警", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer wxNotice;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sort;
}
