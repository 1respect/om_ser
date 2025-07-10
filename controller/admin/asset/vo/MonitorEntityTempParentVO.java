package cn.iocoder.yudao.module.monitor.controller.admin.asset.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 监控对象上一级层级结构新增/修改 Request VO")
@Data
public class MonitorEntityTempParentVO {
    private  Long parentId;
    private  String parentName;
    private  Long childId;
    private  String childName;

}
