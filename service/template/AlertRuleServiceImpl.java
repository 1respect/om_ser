package cn.iocoder.yudao.module.monitor.service.template;

import cn.iocoder.yudao.module.monitor.controller.admin.template.alertrule.vo.AlertRulePageReqVO;
import cn.iocoder.yudao.module.monitor.controller.admin.template.alertrule.vo.AlertRuleRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.template.alertrule.vo.AlertRuleSaveReqVO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.center.CenterInfoDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.TemplateDO;
import cn.iocoder.yudao.module.monitor.dal.mysql.center.CenterInfoMapper;
import cn.iocoder.yudao.module.monitor.dal.mysql.template.TemplateMapper;
import cn.iocoder.yudao.module.system.dal.dataobject.monitor.MonitorTypeDO;
import cn.iocoder.yudao.module.system.dal.mysql.monitor.MonitorTypeMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.monitor.dal.dataobject.template.AlertRuleDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.monitor.dal.mysql.template.AlertRuleMapper;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.MONITOR_RULE_NOT_FOUND;

/**
 * 报警规则 Service 实现类
 *
 * @author 智能化运维
 */
@Service
@Validated
public class AlertRuleServiceImpl implements AlertRuleService {

    @Resource
    private AlertRuleMapper alertRuleMapper;
    @Resource
    private TemplateMapper templateMapper;
    @Resource
    private CenterInfoMapper centerInfoMapper;
    @Resource
    private MonitorTypeMapper monitorTypeMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Long createAlertRule(AlertRuleSaveReqVO createReqVO) {
        // 插入
        AlertRuleDO alertRule = BeanUtils.toBean(createReqVO, AlertRuleDO.class);
        alertRule.setAlertLevelColor(createReqVO.getAlertLevelInfo().getColor());
        alertRule.setAlertLevelName(createReqVO.getAlertLevelInfo().getName());
        alertRule.setCreateTime(LocalDateTime.now());
        int insert = alertRuleMapper.insert(alertRule);
        if (insert==1){
            //为缓存新增type_code属性
            MonitorTypeDO monitorTypeDO = monitorTypeMapper.selectById(alertRule.getMonitorTypeId());
            if (monitorTypeDO.getTypeCode()!=null) {
                alertRule.setTypeCode(monitorTypeDO.getTypeCode());
            }
            redisTemplate.opsForHash().put("alarm:template:"+alertRule.getTemplateId()+":type:"+alertRule.getMonitorTypeId()+":metric:"+alertRule.getMonitorIndexId(),"rule:"+alertRule.getId(),alertRule);
        }
        // 返回
        return alertRule.getId();
    }

    @Override
    public void updateAlertRule(AlertRuleSaveReqVO updateReqVO) {
        // 校验存在
        validateAlertRuleExists(updateReqVO.getId());
        // 更新
        AlertRuleDO updateObj = BeanUtils.toBean(updateReqVO, AlertRuleDO.class);
        updateObj.setAlertLevelColor(updateReqVO.getAlertLevelInfo().getColor());
        updateObj.setAlertLevelName(updateReqVO.getAlertLevelInfo().getName());
        int i = alertRuleMapper.updateById(updateObj);
        if (i==1){
            MonitorTypeDO monitorTypeDO = monitorTypeMapper.selectById(updateObj.getMonitorTypeId());
            // 动态添加字段
            if (monitorTypeDO.getTypeCode()!=null) {
                updateObj.setTypeCode(monitorTypeDO.getTypeCode());
            }
            redisTemplate.opsForHash().put("alarm:template:"+updateObj.getTemplateId()+":type:"+updateObj.getMonitorTypeId()+":metric:"+updateObj.getMonitorIndexId(),"rule:"+updateObj.getId(),updateObj);
        }
    }

    @Override
    public void deleteAlertRule(Long id) {
        // 校验存在
        validateAlertRuleExists(id);
        AlertRuleDO alertRuleDO = alertRuleMapper.selectById(id);
        // 删除
        int i = alertRuleMapper.deleteById(id);

        if (i==1){
            String redisKey = "alarm:template:" + alertRuleDO.getTemplateId() +
                    ":type:" + alertRuleDO.getMonitorTypeId()+":metric:"+alertRuleDO.getMonitorIndexId();
            if (redisTemplate.hasKey(redisKey)) {
                redisTemplate.opsForHash().delete(redisKey, "rule:"+id);
            }
        }

    }

    private void validateAlertRuleExists(Long id) {
        if (alertRuleMapper.selectById(id) == null) {
            throw exception(MONITOR_RULE_NOT_FOUND);
        }
    }

    @Override
    public AlertRuleDO getAlertRule(Long id) {
        return alertRuleMapper.selectById(id);
    }

    @Override
    public PageResult<AlertRuleRespVO> getAlertRulePage(AlertRulePageReqVO pageReqVO) {
        PageResult<AlertRuleDO> page = alertRuleMapper.selectPage(pageReqVO);
        //数据库未存入计算中心和监控类型的名称，所以要根据外键id做一步查询，我个人认为是可以加字段存入的来避免多一步查询数据库
        if (page.getTotal()!=0){
            TemplateDO templateDO = templateMapper.selectById(pageReqVO.getTemplateId());
            String centerName = centerInfoMapper.selectById(templateDO.getCenterId()).getName();
            String objTypeName  = monitorTypeMapper.selectById(templateDO.getTypeId()).getName();

            List<AlertRuleRespVO> enrichedList = page.getList().stream()
                    .map(alertRule -> {
                        AlertRuleRespVO alertRuleRespVO = BeanUtils.toBean(alertRule,AlertRuleRespVO.class);
                        alertRuleRespVO.setCenterName(centerName);
                        alertRuleRespVO.setObjTypeName(objTypeName);
                        return alertRuleRespVO;
                    })
                    .collect(Collectors.toList());
            PageResult<AlertRuleRespVO> result =  new PageResult<>(
                    page.getTotal()
            );
            result.setList(enrichedList);
            return result;
        }
        return null;

    }

    /**
     * 获得所有报警规则列表
     *
     * @return 所有报警规则列表
     */
  public   List<AlertRuleDO> getAllAlertRules(){
        return alertRuleMapper.selectList();
    }

}