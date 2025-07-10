package cn.iocoder.yudao.module.monitor.dal.dataobject.template;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;

/**
 * 报警规则 DO
 *
 * @author 智能化运维
 */
@TableName("monitor_alert_rule")
@KeySequence("monitor_alert_rule_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertRuleDO{

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 报警名称
     */
    private String alertName;
    /**
     * 计算类型
     */
    private String calculationType;
    /**
     * 监控的指标
     */
    private Long monitorIndexId;
    /**
     * 表达式
     */
    private String expression;
    /**
     * 统计周期
     */
    private String calculatioCycle;
    /**
     * 阈值上限
     */
    private Integer upValue;
    /**
     * 阈值下限
     */
    private Integer downValue;
    /**
     * 报警级别
     */
    private Long alertLevel;
    /**
     * 告警内容模板
     */
    private String contentTemplate;
    /**
     * 备注
     */
    private String remark;
    /**
     *  创建者
     */
    private String creator;
    /**
     * 外键，关联monitor_template
     */
    private Long templateId;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 连续发生次数
     */
    private Integer continuedNumber;

    /**
     *
     * 重复规则，连续发生多少次以后多少分钟不能发送
     */
    private Integer repeatedRuleTime;


    /**
     *监控类型ID
     */
    private Long monitorTypeId;

    //告警等级名称
    private String alertLevelName;

    //告警等级颜色
    private String alertLevelColor;

    //指标别名
    private String monitorIndexName;

    //外键关联monitor_center_info
    private Long centerId;

    //加入缓存是带的属性不需要持久化
    @TableField(exist = false)
    private String typeCode;

}