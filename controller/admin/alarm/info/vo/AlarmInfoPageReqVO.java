package cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo;


import cn.iocoder.yudao.framework.common.pojo.PageParam;
import lombok.Data;

@Data
public class AlarmInfoPageReqVO extends PageParam {
    private Long centerId;
    private Integer status;
    private Long alarmLevelId;
    private String monitorObjName; // 支持模糊查询的监控对象名
}
