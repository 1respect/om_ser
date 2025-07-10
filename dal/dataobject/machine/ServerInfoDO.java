package cn.iocoder.yudao.module.monitor.dal.dataobject.machine;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fhs.core.trans.vo.TransPojo;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.ibatis.type.JdbcType;

/**
 * 物理机管理 DO
 *
 * @author 智能化运维
 */
@TableName("monitor_server_info")
@InterceptorIgnore(tenantLine = "true")
@Data
@JsonIgnoreProperties(value = "transMap")
public class ServerInfoDO  implements Serializable, TransPojo  {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 服务器名称
     */
    private String serverName;
    /**
     * 其挂载到哪个对象下
     */
    private Long monitorEntityTempId;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 型号
     */
    private String model;
    /**
     * 功率
     */
    private String power;
    /**
     * 硬盘容量
     */
    private String hardDisk;
    /**
     * 网卡
     */
    private String netCard;
    /**
     * cpu信息
     */
    private String cpu;
    /**
     * gpu信息
     */
    private String gpu;
    /**
     * 内存容量
     */
    private String memory;
    /**
     * MAC1地址
     */
    private String mac1;
    /**
     * MAC2地址
     */
    private String mac2;
    /**
     * 编码
     */
    private String sno;
    /**
     * 机器状态：当前是运行，关机	0，关机 默认值；	1.运行中
     */
    private Integer status;
    /**
     * 机器使用状态：默认为0：空闲；1：已使用
     */
    private Integer isused;
    /**
     * ip地址
     */
    private String ip;

    private String uNumber;

    private String serverOem;
    /**
     * 备注
     */
    private String remark;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */

    private String password;
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


    private String powerSupply;

    private String cooling;

}
