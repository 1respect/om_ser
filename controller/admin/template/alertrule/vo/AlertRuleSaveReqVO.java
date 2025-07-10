package cn.iocoder.yudao.module.monitor.controller.admin.template.alertrule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 报警规则新增/修改 Request VO")
@Data
public class AlertRuleSaveReqVO {

    private Long id;
    @Schema(description = "报警名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "报警名称不能为空")
    private String alertName;

    @Schema(description = "计算类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotEmpty(message = "计算类型不能为空")
    private String calculationType;

    @Schema(description = "监控的指标", requiredMode = Schema.RequiredMode.REQUIRED, example = "28391")
    @NotEmpty(message = "监控的指标不能为空")
    private String monitorIndexId;

    @Schema(description = "表达式", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "表达式不能为空")
    private String expression;

    @Schema(description = "统计周期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "统计周期不能为空")
    private String calculatioCycle;

    @NotEmpty(message = "数值不能为空")
    private String upValue;

    private String downValue;

    @Schema(description = "报警级别", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "报警级别不能为空")
    private String alertLevel;

    @Schema(description = "告警内容模板", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "告警内容模板不能为空")
    private String contentTemplate;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = " 创建者")
    private String creator;

    @Schema(description = "外键，关联monitor_template", example = "6065")
    private Long templateId;


    /**
     * 连续发生次数
     */
    @NotNull(message = "连续发生次数不能为空")
    private Integer continuedNumber;

    /**
     *
     * 重复规则，连续发生多少次以后多少分钟不能发送
     */
    @NotNull(message = "重复规则不能为空")
    private Integer repeatedRuleTime;


    /**
     *监控类型ID
     */
    @NotNull(message = "监控类型ID不能为空")
    private Long monitorTypeId;

    @NotEmpty(message = "监控指标名称不能为空")
    private String monitorIndexName;

    private AlertLevelInfo alertLevelInfo; // 新增内部类

    @NotNull(message = "中心ID不能为空")
    private Long centerId;

    @Data
    public static class AlertLevelInfo {
        private Long id;
        private Integer level;
        private String name;
        private String color;
    }

}