package cn.iocoder.yudao.module.monitor.dal.mysql.template;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.TemplateDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.monitor.controller.admin.template.vo.*;

/**
 *  监控模板 Mapper
 *
 * @author 智能化运维
 */
@Mapper
public interface TemplateMapper extends BaseMapper<TemplateDO> {

}