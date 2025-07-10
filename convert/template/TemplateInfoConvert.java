package cn.iocoder.yudao.module.monitor.convert.template;

import cn.iocoder.yudao.module.monitor.controller.admin.template.vo.TemplateRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.template.vo.TemplateSaveReqVO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.TemplateDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper
public interface TemplateInfoConvert {
    TemplateInfoConvert INSTANCE = Mappers.getMapper(TemplateInfoConvert.class);

    TemplateDO convert(TemplateSaveReqVO bean);

    TemplateRespVO convert(TemplateDO bean);

    List<TemplateRespVO> convertList(List<TemplateDO> list);

    List<TemplateRespVO> convertSimpleList(List<TemplateDO> list);

}
