package cn.iocoder.yudao.module.monitor.service.template;

import cn.iocoder.yudao.module.monitor.controller.admin.template.alertrule.vo.AlertRulePageReqVO;
import cn.iocoder.yudao.module.monitor.controller.admin.template.alertrule.vo.AlertRuleRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.template.alertrule.vo.AlertRuleSaveReqVO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.machine.ServerInfoDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.AlertRuleDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.validation.Valid;
import java.util.List;

/**
 * 报警规则 Service 接口
 *
 * @author 智能化运维
 */
public interface AlertRuleService {

    /**
     * 创建报警规则
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAlertRule(@Valid AlertRuleSaveReqVO createReqVO);

    /**
     * 更新报警规则
     *
     * @param updateReqVO 更新信息
     */
    void updateAlertRule(@Valid AlertRuleSaveReqVO updateReqVO);

    /**
     * 删除报警规则
     *
     * @param id 编号
     */
    void deleteAlertRule(Long id);

    /**
     * 获得报警规则
     *
     * @param id 编号
     * @return 报警规则
     */
    AlertRuleDO getAlertRule(Long id);

    /**
     * 获得报警规则分页
     *
     * @param pageReqVO 分页查询
     * @return 报警规则分页
     */
    PageResult<AlertRuleRespVO> getAlertRulePage(AlertRulePageReqVO pageReqVO);

    /**
     * 获得所有报警规则列表
     *
     * @return 所有报警规则列表
     */
    List<AlertRuleDO> getAllAlertRules();
}