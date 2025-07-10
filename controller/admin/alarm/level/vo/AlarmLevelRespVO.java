package cn.iocoder.yudao.module.monitor.controller.admin.alarm.level.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlarmLevelRespVO {
    private Long id;
    private Integer level;
    private String name;
    private String color;
    private Integer mailNotice;
    private Integer smsNotice;
    private Integer phoneNotice;
    private Integer wxNotice;
    private Integer sort;
    private LocalDateTime createTime;
}
