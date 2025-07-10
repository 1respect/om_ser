package cn.iocoder.yudao.module.monitor.api.indexdata.alarminfo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fhs.core.trans.vo.TransPojo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MonitorDataAndEntityVO implements Serializable{

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 设备的SN码(出厂序列号)，通过此字段调用第三方接口返回监控指标数据
     */
    private String deviceId;
    /**
     * 监控对象ID
     */
    private Long monitorObjId;
    /**
     * 父级监控对象ID
     */
    private Long parentMonObjId;
    /**
     * 监控对象类型ID
     */
    private Long typeId;
    /**
     * 指标名称
     */
    private String metricName;
    /**
     * 指标数值
     */
    private String metricValue;
    /**
     * 指标数据上报时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime reportTime;
    /**
     * 采集时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime collectionTime;
    /**
     * 服务器接收时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime serverTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
    /**
     * 备注
     */
    private String remark;

    private Long templateId;

    private Long metricId;
    private String metrics;


}
