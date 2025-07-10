package cn.iocoder.yudao.module.monitor.service.template;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.monitor.convert.template.MetricConfigConvert;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmLevelDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.center.CenterInfoDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.AlertRuleDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.TemplateDO;
import cn.iocoder.yudao.module.monitor.dal.mysql.alarm.MonitorEntityTempMapper;
import cn.iocoder.yudao.module.monitor.dal.mysql.center.CenterInfoMapper;
import cn.iocoder.yudao.module.monitor.dal.mysql.template.AlertRuleMapper;
import cn.iocoder.yudao.module.monitor.dal.mysql.template.TemplateMapper;
import cn.iocoder.yudao.module.system.dal.dataobject.monitor.MonitorTypeDO;
import cn.iocoder.yudao.module.system.dal.mysql.monitor.MonitorTypeMapper;
import cn.iocoder.yudao.module.system.service.monitor.MonitorTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.monitor.controller.admin.template.metricconfig.vo.*;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.MetricConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.monitor.dal.mysql.template.MetricConfigMapper;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.MONITOR_METRIC_NOT_FOUND;

/**
 * 监控指标配置表(根据创建的监控对象层级结构 设置监控指标) Service 实现类
 *
 * @author 智能化运维
 */
@Service
@Validated
public class MetricConfigServiceImpl implements MetricConfigService {

    @Resource
    private MetricConfigMapper metricConfigMapper;
    @Resource
    private CenterInfoMapper centerInfoMapper;
    @Resource
    private MonitorTypeMapper monitorTypeMapper;
    @Resource
    private TemplateMapper templateMapper;
    @Resource
    private AlertRuleMapper alertRuleMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Long createMetricConfig(MetricConfigSaveReqVO createReqVO) {
        // 插入
        MetricConfigDO metricConfig = BeanUtils.toBean(createReqVO, MetricConfigDO.class);

        //xc 新增判断一个模板下只能有一个指标名称
        LambdaQueryWrapper<MetricConfigDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MetricConfigDO::getTemplateId,metricConfig.getTemplateId());
        lambdaQueryWrapper.eq(MetricConfigDO::getObjTypeId,metricConfig.getObjTypeId());
        List<MetricConfigDO> metricConfigDOList = metricConfigMapper.selectList(lambdaQueryWrapper);
        if (!metricConfigDOList.isEmpty()){
            metricConfigDOList.forEach(metricConfigDO -> {
                if (Objects.equals(metricConfigDO.getMetrics(), metricConfig.getMetrics())){
                    throw new ServiceException(400,"该指标同名名称已存在");
                }
            });
        }


        // 根据 typeId 查 parentId
        if (metricConfig.getObjTypeId()!=null){
            MonitorTypeDO monitorType = monitorTypeMapper.selectById(metricConfig.getObjTypeId());
            if (monitorType != null){
                metricConfig.setParentObjTypeId(monitorType.getParentId());
            }
        }

        metricConfig.setCreateTime(LocalDateTime.now());
        metricConfig.setUpdateTime(LocalDateTime.now());

        metricConfigMapper.insert(metricConfig);
        // 返回
        return metricConfig.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMetricConfig(MetricConfigSaveReqVO updateReqVO) {
        // 校验存在
        validateMetricConfigExists(updateReqVO.getId());

        // 更新
        MetricConfigDO updateObj = BeanUtils.toBean(updateReqVO, MetricConfigDO.class);

        //xc 更新的时候验证是否创建过同一名称的指标
        LambdaQueryWrapper<MetricConfigDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MetricConfigDO::getTemplateId,updateObj.getTemplateId());
        lambdaQueryWrapper.eq(MetricConfigDO::getObjTypeId,updateObj.getObjTypeId());

        // 排除自己
        lambdaQueryWrapper.ne(MetricConfigDO::getId, updateObj.getId());
        lambdaQueryWrapper.eq(MetricConfigDO::getMetrics, updateObj.getMetrics());
        MetricConfigDO metricConfigDO = metricConfigMapper.selectOne(lambdaQueryWrapper);
        if (metricConfigDO!= null&& Objects.equals(metricConfigDO.getId(), updateObj.getId())) {
            throw new ServiceException(400, "该指标同名名称已存在");
        }

        updateObj.setUpdateTime(LocalDateTime.now());
        metricConfigMapper.updateById(updateObj);
        List<AlertRuleDO> alertRuleDOS = alertRuleMapper.selectList(new LambdaQueryWrapper<AlertRuleDO>().eq(AlertRuleDO::getMonitorIndexId, updateObj.getId()));
        if (!alertRuleDOS.isEmpty()){
            // 修改指标时同步修改告警规则和缓存
            alertRuleDOS.forEach(alertRule -> {
                alertRule.setMonitorIndexName(updateObj.getMetrics());
                alertRuleMapper.updateById(alertRule);
                MonitorTypeDO monitorTypeDO = monitorTypeMapper.selectById(alertRule.getMonitorTypeId());
                if (monitorTypeDO.getTypeCode()!=null) {
                    alertRule.setTypeCode(monitorTypeDO.getTypeCode());
                }
                redisTemplate.opsForHash().put("alarm:template:"+alertRule.getTemplateId()+":type:"+alertRule.getMonitorTypeId()+":metric:"+alertRule.getMonitorIndexId(),"rule:"+alertRule.getId(),alertRule);
            });
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMetricConfig(Long id) {
        // 校验存在
        validateMetricConfigExists(id);
        // 删除
        if (metricConfigMapper.deleteById(id)>0){
            List<AlertRuleDO> alertRuleDOS = alertRuleMapper.selectList(new LambdaQueryWrapper<AlertRuleDO>().eq(AlertRuleDO::getMonitorIndexId,id));
            if (!alertRuleDOS.isEmpty()){
                alertRuleDOS.forEach(alertRule -> {
                    redisTemplate.opsForHash().delete("alarm:template:"+alertRule.getTemplateId()+":type:"+alertRule.getMonitorTypeId()+":metric:"+alertRule.getMonitorIndexId(),"rule:"+alertRule.getId());
                });
            }
        }

    }

    private void validateMetricConfigExists(Long id) {
        if (metricConfigMapper.selectById(id) == null) {
            throw exception(MONITOR_METRIC_NOT_FOUND);
        }
    }

    @Override
    public MetricConfigDO getMetricConfig(Long id) {
        return metricConfigMapper.selectById(id);
    }

    @Override
    public PageResult<MetricConfigRespVO> getMetricConfigPage(MetricConfigPageReqVO pageReqVO) {
        LambdaQueryWrapper<MetricConfigDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(pageReqVO.getCenterId()!=null, MetricConfigDO::getCenterId, pageReqVO.getCenterId());
        wrapper.eq(pageReqVO.getObjTypeId()!=null, MetricConfigDO::getObjTypeId, pageReqVO.getObjTypeId());
        wrapper.eq(pageReqVO.getTemplateId()!=null, MetricConfigDO::getTemplateId, pageReqVO.getTemplateId());

        IPage<MetricConfigDO> myPage = metricConfigMapper.selectPage(
                new Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize()),
                wrapper
        );

        List<MetricConfigDO> list = myPage.getRecords();
        Set<Long> centerIds = list.stream()
                .map(MetricConfigDO::getCenterId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, String> centerNameMap = centerIds.isEmpty() ? Collections.emptyMap() :
                centerInfoMapper.selectBatchIds(centerIds)
                        .stream()
                        .collect(Collectors.toMap(CenterInfoDO::getId, CenterInfoDO::getName));

        Set<Long> objTypeIds = list.stream()
                .map(MetricConfigDO::getObjTypeId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, String> objTypeNameMap = objTypeIds.isEmpty() ? Collections.emptyMap() :
                monitorTypeMapper.selectBatchIds(objTypeIds)
                        .stream().collect(Collectors.toMap(MonitorTypeDO::getId, MonitorTypeDO::getName));

        List<MetricConfigRespVO> voList = list.stream().map(metricConfig -> {
            MetricConfigRespVO vo = MetricConfigConvert.INSTANCE.convert(metricConfig);
            vo.setCenterName(centerNameMap.get(metricConfig.getCenterId()));
            vo.setObjTypeName(objTypeNameMap.get(metricConfig.getObjTypeId()));
            return vo;
        }).collect(Collectors.toList());

        return  new PageResult<>(voList, myPage.getTotal());
    }

    @Override
    public List<MetricConfigRespVO> getMetricsByTemplateId(Long templateId) {
        List<MetricConfigDO> list = metricConfigMapper.selectList(
                new QueryWrapper<MetricConfigDO>().eq("template_id", templateId)
        );
        return MetricConfigConvert.INSTANCE.convertList(list);
    }

    @Override
    public List<MetricConfigDO> getSimpleList(Long id) {
        LambdaQueryWrapper<MetricConfigDO> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.eq(MetricConfigDO::getTemplateId,id);
        return metricConfigMapper.selectList(
                objectLambdaQueryWrapper
        );
    }
}