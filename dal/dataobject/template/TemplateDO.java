package cn.iocoder.yudao.module.monitor.dal.dataobject.template;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import org.apache.ibatis.type.JdbcType;

/**
 *  监控模板 DO
 *
 * @author 智能化运维
 */
@TableName("monitor_template")
@KeySequence("monitor_template_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateDO{

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 模版名称
     */
    private String templateName;
    /**
     * 智算中心ID
     */
    private Long centerId;
    /**
     * 监控对象类型ID
     */
    private Long typeId;
    /**
     * 协议（HTTP/HTTPS/其他）
     */
    private String protocol;
    /**
     * 请求方式（GET/POST）
     */
    private String requestMethod;
    /**
     * 请求URL
     */
    private String url;
    /**
     * 监控周期(秒)
     */
    private Integer period;
    /**
     * 超时设置(秒)
     */
    private Integer timeout;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 备注
     */
    private String remark;
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

    private Integer sourceType; // 监控对象来源类型，1=上行，2=下行

}