package cn.iocoder.yudao.module.monitor.dal.mysql.machine;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.monitor.dal.dataobject.machine.ServerInfoDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.monitor.controller.admin.machine.vo.*;
import java.util.Collection;
import java.util.List;

/**
 * 物理机管理 Mapper
 *
 * @author 智能化运维
 */
@Mapper
public interface ServerInfoMapper extends BaseMapperX<ServerInfoDO> {

    default List<ServerInfoDO> selectListByIds(Collection<Long> ids) {
        return selectBatchIds(ids);
    }

    default List<ServerInfoDO> selectListByName(String name) {
        return selectList(new LambdaQueryWrapperX<ServerInfoDO>()
                .likeIfPresent(ServerInfoDO::getServerName, name));
    }


    default List<ServerInfoDO> selectListByEntityTempId(String entityTempId) {
        return selectList(new LambdaQueryWrapperX<ServerInfoDO>()
                .likeIfPresent(ServerInfoDO::getMonitorEntityTempId, entityTempId));
    }

    default cn.iocoder.yudao.framework.common.pojo.PageResult<ServerInfoDO> selectPage(ServerInfoPageReqVO reqVO) {

        String isused ="";
        if (null!=reqVO &&  null!=reqVO.getIsused() ) {
            isused = reqVO.getIsused().toString();
        }
        return selectPage(reqVO, new LambdaQueryWrapperX<ServerInfoDO>()
                .likeIfPresent(ServerInfoDO:: getSno, reqVO.getSno())
                .likeIfPresent(ServerInfoDO::getServerName, reqVO.getServername())
                .likeIfPresent(ServerInfoDO::getModel, reqVO.getModel())
                .likeIfPresent(ServerInfoDO::getIsused, isused)
                .orderByDesc(ServerInfoDO::getId));
    }
}

