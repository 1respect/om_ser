package cn.iocoder.yudao.module.monitor.controller.admin.template.alertrule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 报警规则 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AlertRuleRespVO {

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
    private Integer alertLevel;
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

    //监控的指标别名
    private String monitorIndexName;

    //告警等级名称
    private String alertLevelName;

    //告警等级颜色
    private String alertLevelColor;

    private String centerName;

    private String objTypeName;




}