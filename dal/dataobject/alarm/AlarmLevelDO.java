package cn.iocoder.yudao.module.monitor.dal.dataobject.alarm;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.apache.ibatis.type.JdbcType;

import java.time.LocalDateTime;

@TableName("monitor_alert_level")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmLevelDO {

    private Long id;

    /** 等级，1/2/3/4 */
    private Integer level;

    /** 等级名称，如“严重”、“警告” */
    private String name;

    /** 颜色，前端样式用 */
    private String color;

    /** 邮件告警（1=启用 0=禁用） */
    private Integer mailNotice;

    /** 短信告警 */
    private Integer smsNotice;

    /** 电话告警 */
    private Integer phoneNotice;

    /** 企业微信告警 */
    private Integer wxNotice;

    /** 排序 */
    private Integer sort;
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
}