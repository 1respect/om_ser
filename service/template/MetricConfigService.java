package cn.iocoder.yudao.module.monitor.service.template;

import java.util.*;
import cn.iocoder.yudao.module.monitor.controller.admin.template.metricconfig.vo.*;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.MetricConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

import javax.validation.Valid;

/**
 * 监控指标配置表(根据创建的监控对象层级结构 设置监控指标) Service 接口
 *
 * @author 智能化运维
 */
public interface MetricConfigService {

    /**
     * 创建监控指标配置表(根据创建的监控对象层级结构 设置监控指标)
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createMetricConfig(@Valid MetricConfigSaveReqVO createReqVO);

    /**
     * 更新监控指标配置表(根据创建的监控对象层级结构 设置监控指标)
     *
     * @param updateReqVO 更新信息
     */
    void updateMetricConfig(@Valid MetricConfigSaveReqVO updateReqVO);

    /**
     * 删除监控指标配置表(根据创建的监控对象层级结构 设置监控指标)
     *
     * @param id 编号
     */
    void deleteMetricConfig(Long id);

    /**
     * 获得监控指标配置表(根据创建的监控对象层级结构 设置监控指标)
     *
     * @param id 编号
     * @return 监控指标配置表(根据创建的监控对象层级结构 设置监控指标)
     */
    MetricConfigDO getMetricConfig(Long id);

    /**
     * 获得监控指标配置表(根据创建的监控对象层级结构 设置监控指标)分页
     *
     * @param pageReqVO 分页查询
     * @return 监控指标配置表(根据创建的监控对象层级结构 设置监控指标)分页
     */
    PageResult<MetricConfigRespVO> getMetricConfigPage(MetricConfigPageReqVO pageReqVO);

    List<MetricConfigRespVO> getMetricsByTemplateId(Long templateId);

    List<MetricConfigDO> getSimpleList(Long id);
}