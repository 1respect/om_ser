package cn.iocoder.yudao.module.monitor.convert.template;

import cn.iocoder.yudao.module.monitor.controller.admin.template.metricconfig.vo.MetricConfigRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.template.metricconfig.vo.MetricConfigSaveReqVO;
import cn.iocoder.yudao.module.monitor.controller.admin.template.metricconfig.vo.MetricConfigSimpleVO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.MetricConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper
public interface MetricConfigConvert {
    MetricConfigConvert INSTANCE = Mappers.getMapper(MetricConfigConvert.class);
    MetricConfigDO convert(MetricConfigSaveReqVO bean);
    MetricConfigRespVO convert(MetricConfigDO bean);
    List<MetricConfigRespVO> convertList(List<MetricConfigDO> list);
    List<MetricConfigSimpleVO> convertSimpleList(List<MetricConfigDO> list);
}
