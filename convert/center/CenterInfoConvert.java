package cn.iocoder.yudao.module.monitor.convert.center;

import cn.iocoder.yudao.module.monitor.controller.admin.center.vo.CenterInfoRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.center.vo.CenterInfoSimpleVO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.center.CenterInfoDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CenterInfoConvert {
    CenterInfoConvert INSTANCE = Mappers.getMapper(CenterInfoConvert.class);

    CenterInfoRespVO convert(CenterInfoDO bean);
    CenterInfoSimpleVO convertSimple(CenterInfoDO bean);
    List<CenterInfoRespVO> convertList(List<CenterInfoDO> list);
    List<CenterInfoSimpleVO> convertSimpleList(List<CenterInfoDO> list);
}