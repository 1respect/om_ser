package cn.iocoder.yudao.module.monitor.dal.mysql.template;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.monitor.controller.admin.template.alertrule.vo.AlertRulePageReqVO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.AlertRuleDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报警规则 Mapper
 *
 * @author 智能化运维
 */
@Mapper
public interface AlertRuleMapper extends BaseMapperX<AlertRuleDO> {

    default PageResult<AlertRuleDO> selectPage(AlertRulePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AlertRuleDO>()
                .likeIfPresent(AlertRuleDO::getAlertName, reqVO.getAlertName())
                .eqIfPresent(AlertRuleDO::getCalculationType, reqVO.getCalculationType())
                .eqIfPresent(AlertRuleDO::getMonitorIndexId, reqVO.getMonitorIndexId())
                .eqIfPresent(AlertRuleDO::getCalculatioCycle, reqVO.getCalculatioCycle())
                .eqIfPresent(AlertRuleDO::getAlertLevel, reqVO.getAlertLevel())
                .eqIfPresent(AlertRuleDO::getContentTemplate, reqVO.getContentTemplate())
                .eqIfPresent(AlertRuleDO::getRemark, reqVO.getRemark())
                .eqIfPresent(AlertRuleDO::getCreator, reqVO.getCreator())
                .betweenIfPresent(AlertRuleDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AlertRuleDO::getTemplateId, reqVO.getTemplateId())
                .orderByDesc(AlertRuleDO::getId));
    }




}