package cn.iocoder.yudao.module.monitor.controller.admin.alarm.level.vo;

import lombok.Data;

@Data
public class AlarmLevelSimpleVO {
    private Long id;
    private Integer level;
    private String name;
    private String color;
}
