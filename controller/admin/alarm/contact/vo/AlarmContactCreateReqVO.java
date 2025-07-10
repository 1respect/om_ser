package cn.iocoder.yudao.module.monitor.controller.admin.alarm.contact.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AlarmContactCreateReqVO {

    @Schema(description = "智算中心ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long centerId;

    @Schema(description = "联系人姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
}