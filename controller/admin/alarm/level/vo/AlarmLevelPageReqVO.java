package cn.iocoder.yudao.module.monitor.controller.admin.alarm.level.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import lombok.Data;

@Data
public class AlarmLevelPageReqVO extends PageParam {
    private Integer level;
    private String name;
}