package cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo;


import lombok.Data;
import javax.validation.constraints.*;

@Data
public class AlarmActionRecordCreateReqVO {
    @NotNull(message = "报警记录ID不能为空")
    private Long alertRecordId;
    @NotBlank(message = "处理结果类型不能为空")
    private String processType;
    @NotBlank(message = "处理结果描述不能为空")
    private String description;
    private String remark;
}
