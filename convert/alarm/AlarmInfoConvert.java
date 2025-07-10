package cn.iocoder.yudao.module.monitor.convert.alarm;

import cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo.AlarmInfoCreateReqVO;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo.AlarmInfoRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo.AlarmInfoUpdateReqVO;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.level.vo.AlarmLevelSimpleVO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmInfoDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmLevelDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AlarmInfoConvert {
    AlarmInfoConvert INSTANCE = Mappers.getMapper(AlarmInfoConvert.class);

    AlarmInfoDO convert(AlarmInfoCreateReqVO bean);
    AlarmInfoDO convert(AlarmInfoUpdateReqVO bean);
    AlarmInfoRespVO convert(AlarmInfoDO bean);
    List<AlarmInfoRespVO> convertList(List<AlarmInfoDO> list);
    AlarmLevelSimpleVO convert(AlarmLevelDO bean);
    List<AlarmLevelSimpleVO> convertList2(List<AlarmLevelDO> list);
}