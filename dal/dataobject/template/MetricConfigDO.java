package cn.iocoder.yudao.module.monitor.dal.dataobject.template;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import org.apache.ibatis.type.JdbcType;

/**
 * 监控指标配置表(根据创建的监控对象层级结构 设置监控指标) DO
 *
 * @author 智能化运维
 */
@TableName("monitor_metric_config")
@KeySequence("monitor_metric_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricConfigDO{

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 监控对象类型ID
     */
    private Long objTypeId;
    /**
     * 监控对象类型父级ID
     */
    private Long parentObjTypeId;
    /**
     * 对象层级
     */
    private String level;
    /**
     * 指标名称
     */
    private String metrics;
    /**
     * 阈值范围
     */
    private String thresholdInterval;
    /**
     * 阈值范围的数值
     */
    private String value;
    /**
     * 数值单位
     */
    private String unit;
    /**
     * 备注
     */
    private String remark;
    /**
     * 外键，关联monitor_template
     */
    private Long templateId;
    /**
     * 创建者
     *
     */
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String creator;
    /**
     * 更新者
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String updater;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;

    private Long centerId;

    /**
     * 指标别名
     */
    private String metricsAlias;

    /**
     * 解析方式
     */
    private String analyticMethod;
}