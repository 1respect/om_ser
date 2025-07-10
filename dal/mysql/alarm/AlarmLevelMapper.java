package cn.iocoder.yudao.module.monitor.dal.mysql.alarm;


import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmLevelDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmLevelMapper extends BaseMapperX<AlarmLevelDO> {
}