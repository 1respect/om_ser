package cn.iocoder.yudao.module.monitor.controller.admin.machine.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 物理机管理新增/修改 Request VO")
@Data
public class ServerInfoSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "20279")
    private Long id;

    @Schema(description = "服务器名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "服务器名称不能为空")
    private String serverName;

    @Schema(description = "其挂载到哪个对象下", requiredMode = Schema.RequiredMode.REQUIRED, example = "7853")
    @NotNull(message = "其挂载到哪个对象下不能为空")
    private Long monitorEntityTempId;

    @Schema(description = "品牌")
    private String brand;

    @Schema(description = "型号")
    private String model;

    @Schema(description = "功率")
    private String power;

    @Schema(description = "硬盘容量")
    private String hardDisk;

    @Schema(description = "网卡")
    private String netCard;

    @Schema(description = "cpu信息")
    private String cpu;

    @Schema(description = "gpu信息")
    private String gpu;

    @Schema(description = "内存容量")
    private String memory;

    @Schema(description = "MAC1地址")
    private String mac1;

    @Schema(description = "MAC2地址")
    private String mac2;

    @Schema(description = "编码")
    private String sno;

    @Schema(description = "机器状态：当前是运行，关机	0，关机 默认值；	1.运行中", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "机器使用状态：默认为0：空闲；1：已使用", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isused;

    @Schema(description = "ip地址")
    private String ip;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "U数", example = "24U")
    private String uNumber;

    @Schema(description = "OME厂商名称", example = "xxxx东软")
    private String serverOem;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "密码")
    private String password;

    private Integer[] powerSupply;

    private Integer[] cooling;
}