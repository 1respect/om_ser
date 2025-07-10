package cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class AlarmInfoUpdateReqVO {

    @NotNull
    @Schema(description = "主键")
    private Long id;

    @NotNull
    @Schema(description = "监控对象ID")
    private Long monitorObjId;

    @NotNull
    @Schema(description = "报警指标ID")
    private Long monitorIndexId;

    @Schema(description = "告警时间")
    private LocalDateTime alertTime;

    @Schema(description = "表达式")
    private String condition;

    @Schema(description = "计算周期")
    private String calculatioCycle;

    @Schema(description = "实际数值")
    private String triggerValue;

    @Schema(description = "告警级别ID")
    @NotNull private Long alarmLevelId;

    @Schema(description = "报警描述")
    private String description;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "智算中心ID")
    private Long centerId;

    @Schema(description = "状态 0=未处理，1=已处理")
    private Integer status;
}