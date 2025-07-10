package cn.iocoder.yudao.module.monitor.controller.admin.alarm.contact.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AlarmContactPageReqVO extends PageParam {

    @Schema(description = "智算中心")
    private Long centerId;

    @Schema(description = "联系人姓名", example = "张三")
    private String name;

    @Schema(description = "手机号", example = "13800112233")
    private String phone;
}