package cn.iocoder.yudao.module.monitor.controller.admin.machine.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 物理机管理 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ServerInfoRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "20279")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "服务器名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("服务器名称")
    private String serverName;

    @Schema(description = "其挂载到哪个对象下", requiredMode = Schema.RequiredMode.REQUIRED, example = "7853")
    @ExcelProperty("其挂载到哪个对象下")
    private Long monitorEntityTempId;

    @Schema(description = "品牌")
    @ExcelProperty("品牌")
    private String brand;

    @Schema(description = "型号")
    @ExcelProperty("型号")
    private String model;

    @Schema(description = "功率")
    @ExcelProperty("功率")
    private String power;

    @Schema(description = "硬盘容量")
    @ExcelProperty("硬盘容量")
    private String hardDisk;

    @Schema(description = "网卡")
    @ExcelProperty("网卡")
    private String netCard;

    @Schema(description = "cpu信息")
    @ExcelProperty("cpu信息")
    private String cpu;

    @Schema(description = "gpu信息")
    @ExcelProperty("gpu信息")
    private String gpu;

    @Schema(description = "内存容量")
    @ExcelProperty("内存容量")
    private String memory;

    @Schema(description = "MAC1地址")
    @ExcelProperty("MAC1地址")
    private String mac1;

    @Schema(description = "MAC2地址")
    @ExcelProperty("MAC2地址")
    private String mac2;

    @Schema(description = "编码")
    @ExcelProperty("编码")
    private String sno;

    @Schema(description = "机器状态：当前是运行，关机	0，关机 默认值；	1.运行中", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("机器状态：当前是运行，关机	0，关机 默认值；	1.运行中")
    private Integer status;

    @Schema(description = "机器使用状态：默认为0：空闲；1：已使用", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("机器使用状态：默认为0：空闲；1：已使用")
    private Integer isused;

    @Schema(description = "ip地址")
    @ExcelProperty("ip地址")
    private String ip;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;


    @Schema(description = "U数", example = "24U")
    @ExcelProperty("server_oem")
    private String uNumber;

    @Schema(description = "OME厂商名称", example = "xxxx东软")
    @ExcelProperty("server_oem")
    private String serverOem;

}