package cn.iocoder.yudao.module.monitor.dal.mysql.asset;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.monitor.api.indexdata.alarminfo.DeviceWithMetricConfigVO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.asset.MonitorEntityTempDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.asset.MonitorEntityTempParentDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.monitor.controller.admin.asset.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 监控对象层级结构 Mapper
 *
 * @author 智能化运维
 */
@Mapper
public interface AssetMapper extends BaseMapperX<MonitorEntityTempDO> {

    default PageResult<MonitorEntityTempDO> selectPage(MonitorEntityTempPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MonitorEntityTempDO>()
                .likeIfPresent(MonitorEntityTempDO::getName, reqVO.getName())
                .eqIfPresent(MonitorEntityTempDO::getTypeId, reqVO.getTypeId())
                .eqIfPresent(MonitorEntityTempDO::getParentId, reqVO.getParentId())
                .eqIfPresent(MonitorEntityTempDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(MonitorEntityTempDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MonitorEntityTempDO::getId));
    }

    @Select({
            "<script>",
            "SELECT t1.*,  t2.type_code ",
            "FROM monitor_entity_temp t1 LEFT JOIN  monitor_type t2 ON t1.type_id = t2.id",
            "WHERE t1.parent_id = #{parentId}",
            "</script>"
    })
    List<MonitorEntityTempNodeDO> selectListByParentId(@Param("parentId")Long parentId);



    @Select({
            "<script>",
            " SELECT child.id AS child_id, child.name AS child_name, parent.id AS parent_id,parent.name AS parent_name",
            "FROM monitor_entity_temp child LEFT JOIN   monitor_entity_temp parent  ON  child.parent_id = parent.id",
            "WHERE child.id = #{childId}",
            "</script>"
    })
    List<MonitorEntityTempParentDO> selectListByParentDetail(@Param("childId")Long childId);


    @Select({
            "<script>",
            "SELECT *",
            "FROM monitor_entity_temp ",
            "WHERE type_id = #{typeId}",
            "</script>"
    })
    List<MonitorEntityTempNodeDO> selectListByTypeId(Long typeId);


}
