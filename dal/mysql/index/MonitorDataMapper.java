package cn.iocoder.yudao.module.monitor.dal.mysql.index;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.monitor.dal.dataobject.index.MonitorDataDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
@Mapper
public interface MonitorDataMapper extends BaseMapperX<MonitorDataDO> {

}
