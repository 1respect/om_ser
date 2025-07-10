package cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlarmInfoRespVO {
    private Long id;
    private Long centerId;
    private Long monitorObjId;
    private Long monitorIndexId;
    private String monitorObjName;// 监控对象名，展示用
    private LocalDateTime alertTime;
    private String condition;
    private String calculatioCycle;
    private String triggerValue;
    private Long alarmLevelId;
    private String alarmLevelName;   // <--- 由后端组装
    private String alarmLevelColor;  // <--- 由后端组装
    private String description;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer status;
    private String centerName;//智算中心名称，展示用
}