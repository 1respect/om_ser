package cn.iocoder.yudao.module.monitor.api.indexdata.alarminfo;

import cn.iocoder.yudao.module.monitor.dal.dataobject.asset.MonitorEntityTempDO;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class DeviceWithMetricConfigVO extends MonitorEntityTempDO implements Serializable {
    private Long metricId;
    private String metrics;
}
