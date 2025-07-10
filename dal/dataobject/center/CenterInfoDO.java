package cn.iocoder.yudao.module.monitor.dal.dataobject.center;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.apache.ibatis.type.JdbcType;

import java.time.LocalDateTime;

@TableName("monitor_center_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CenterInfoDO{

    private Long id;            // 主键
    private String name;        // 智算中心名称
    private Integer status;     // 状态 1=启用 0=禁用
    private String remark;      // 备注
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