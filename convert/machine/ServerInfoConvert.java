package cn.iocoder.yudao.module.monitor.convert.machine;

import cn.iocoder.yudao.module.monitor.controller.admin.machine.vo.ServerInfoRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.machine.vo.ServerInfoSaveReqVO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.machine.ServerInfoDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;
@Mapper
public interface ServerInfoConvert {

    ServerInfoConvert INSTANCE = Mappers.getMapper(ServerInfoConvert.class);

    ServerInfoDO convert(ServerInfoSaveReqVO bean);

    ServerInfoRespVO convert(ServerInfoDO bean);

    List<ServerInfoRespVO> convertList(List<ServerInfoDO> list);

    List<ServerInfoRespVO> convertSimpleList(List<ServerInfoDO> list);
}