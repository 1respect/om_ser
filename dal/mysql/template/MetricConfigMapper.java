package cn.iocoder.yudao.module.monitor.dal.mysql.template;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.MetricConfigDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.monitor.controller.admin.template.metricconfig.vo.*;

/**
 * 监控指标配置表(根据创建的监控对象层级结构 设置监控指标) Mapper
 *
 * @author 智能化运维
 */
@Mapper
public interface MetricConfigMapper extends BaseMapper<MetricConfigDO> {

}