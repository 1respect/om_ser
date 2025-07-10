package cn.iocoder.yudao.module.monitor.controller.admin.asset.vo;

import cn.iocoder.yudao.module.monitor.dal.dataobject.asset.MonitorEntityTempDO;
import lombok.*;

@Data
@ToString(callSuper = true)
public class MonitorEntityTempNodeDO extends MonitorEntityTempDO {
    // 新增字段，用于存储 type_name
    private String typeCode;

}
