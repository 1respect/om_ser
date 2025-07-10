package cn.iocoder.yudao.module.monitor.dal.dataobject.asset;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class MonitorEntityTempParentDO extends MonitorEntityTempDO{

    private  Long parentId;
    private  String parentName;
    private  Long childId;
    private  String childName;

}
