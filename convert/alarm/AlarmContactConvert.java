package cn.iocoder.yudao.module.monitor.convert.alarm;

import cn.iocoder.yudao.module.monitor.controller.admin.alarm.contact.vo.AlarmContactCreateReqVO;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.contact.vo.AlarmContactPageRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.contact.vo.AlarmContactRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.contact.vo.AlarmContactUpdateReqVO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmContactDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AlarmContactConvert {

    AlarmContactConvert INSTANCE = Mappers.getMapper(AlarmContactConvert.class);

    AlarmContactDO convert(AlarmContactCreateReqVO bean);

    AlarmContactDO convert(AlarmContactUpdateReqVO bean);

    AlarmContactRespVO convert(AlarmContactDO bean);

    List<AlarmContactPageRespVO> convertList(List<AlarmContactDO> list);
}
