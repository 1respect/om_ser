package cn.iocoder.yudao.module.monitor.controller.admin.alarm.contact.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlarmContactRespVO {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "智算中心ID")
    private Long centerId;

    @Schema(description = "联系人姓名")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    private String centerName;
}