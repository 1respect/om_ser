package cn.iocoder.yudao.module.monitor.convert.alarm;

import cn.iocoder.yudao.module.monitor.controller.admin.alarm.level.vo.AlarmLevelCreateReqVO;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.level.vo.AlarmLevelRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.level.vo.AlarmLevelUpdateReqVO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmLevelDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AlarmLevelConvert {
    AlarmLevelConvert INSTANCE = Mappers.getMapper(AlarmLevelConvert.class);

    AlarmLevelDO convert(AlarmLevelCreateReqVO bean);

    AlarmLevelDO convert(AlarmLevelUpdateReqVO bean);

    AlarmLevelRespVO convert(AlarmLevelDO bean);

    List<AlarmLevelRespVO> convertList(List<AlarmLevelDO> list);
}