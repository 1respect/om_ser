package cn.iocoder.yudao.module.monitor.controller.admin.asset.vo;

import lombok.Data;
import java.util.List;

/**
 * 监控对象层级结构树形视图对象
 */
@Data
public class MonitorEntityTempTreeVO {

    /**
     * 节点ID
     */
    private Long id;

    /**
     * 节点名称
     */
    private String label;

    /**
     * 节点类型ID
     */
    private Long typeId;

    /**
     * 父节点ID
     */
    private Long parentId;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 子节点列表
     */
    private List<MonitorEntityTempTreeVO> children;
}
