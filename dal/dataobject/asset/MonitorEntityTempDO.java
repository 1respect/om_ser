package cn.iocoder.yudao.module.monitor.dal.dataobject.asset;

import lombok.*;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.ibatis.type.JdbcType;

/**
 * 监控对象层级结构 DO
 *
 * @author 智能化运维
 */
@TableName("monitor_entity_temp")
@KeySequence("monitor_entity_temp_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitorEntityTempDO  {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 对象名称
     */
    private String name;
    /**
     * 对象类型
     */
    private Long typeId;
    /**
     * 父对象ID
     */
    private Long parentId;
    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 创建者，目前使用 SysUser 的 id 编号
     *
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String creator;
    /**
     * 更新者，目前使用 SysUser 的 id 编号
     *
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String updater;

    private Long templateId;

    private String snCode;
    /**
     * 调用第三方 监控实时数据接口的pdi_index 数值
     */
    private Long pdiIndex;

    private Long centerId;

}