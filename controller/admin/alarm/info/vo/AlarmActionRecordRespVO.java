package cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlarmActionRecordRespVO {
    private Long id;
    private Long alertRecordId;
    private Long userId;
    private String processType;
    private LocalDateTime processTime;
    private String description;
    private String remark;
    private LocalDateTime createTime;
}
