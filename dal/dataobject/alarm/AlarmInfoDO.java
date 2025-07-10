package cn.iocoder.yudao.module.monitor.dal.dataobject.alarm;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.trans.vo.TransPojo;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("monitor_alert_record")
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmInfoDO implements Serializable, TransPojo {
    private Long id;
    private Long monitorObjId;
    private Long monitorIndexId;
    private LocalDateTime alertTime;
    private String alertCondition;
    private String calculatioCycle;
    private String triggerValue;
    private Long alarmLevelId;
    private String description;
    private String remark;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;
    private Integer status;
    private Long centerId;

    /**
     * 邮件发送次数
     */
    private  Integer mailSendCount;
    /**
     * 最后一次邮件发送时间
     */
    private LocalDateTime lastMailSendTime;
    /**
     * 模板ID
     */
    private Long templateId;
}
