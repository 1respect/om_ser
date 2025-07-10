package cn.iocoder.yudao.module.monitor.dal.mysql.alarm;

import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmActionRecordDO;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmActionRecordMapper extends BaseMapperX<AlarmActionRecordDO> {
}