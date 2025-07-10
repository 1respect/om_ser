package cn.iocoder.yudao.module.monitor.controller.admin.center.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CenterInfoRespVO {
    private Long id;
    private String name;
    private Integer status;
    private String remark;
    private String creator;
    private String updater;
    private Date createTime;
    private Date updateTime;
}