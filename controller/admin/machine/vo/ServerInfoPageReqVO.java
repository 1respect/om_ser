package cn.iocoder.yudao.module.monitor.controller.admin.machine.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 物理机管理 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerInfoPageReqVO extends PageParam {

    @Schema(description = "型号")
    private String model;

    @Schema(description = "序列号")
    private String sno;

    @Schema(description = "机器使用状态：默认为0：空闲；1：已使用")
    private Integer isused;

    @Schema(description = "servername")
    private String servername;



}