package cn.iocoder.yudao.module.monitor.dal.mysql.alarm;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.monitor.api.indexdata.alarminfo.DeviceWithMetricConfigVO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.asset.MonitorEntityTempDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MonitorEntityTempMapper extends BaseMapperX<MonitorEntityTempDO> {
    /**
     * 查询需要监控的设备
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Select({
            "<script>",
            "SELECT met.id AS id,",
            "       met.name AS name,",
            "       met.type_id AS type_id,",
            "       met.parent_id AS parent_id,",
            "       met.remark AS remark,",
            "       met.sn_code AS sn_code,",
            "       met.template_id AS template_id,",
            "       mmc.id AS metricId,",
            "       mmc.metrics AS metrics",
            "FROM monitor_entity_temp met",
            "INNER JOIN monitor_metric_config mmc ON met.type_id = mmc.obj_type_id",
            "WHERE met.template_id IS NOT NULL",
            "  AND met.sn_code IS NOT NULL",
            "ORDER BY met.id",
            "LIMIT #{pageNum}, #{pageSize}",
            "</script>"
    })
    List<DeviceWithMetricConfigVO> selectDeviceWithMetricConfig(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);


    /**
     * 查询符合条件的设备总数
     */
    @Select({
            "<script>",
            "SELECT COUNT(*)",
            "FROM monitor_entity_temp met",
            "INNER JOIN monitor_metric_config mmc ON met.type_id = mmc.obj_type_id",
            "WHERE met.template_id IS NOT NULL ",
            "  AND met.sn_code IS NOT NULL ",
            "</script>"
    })
    int countDeviceWithMetricConfig();


}