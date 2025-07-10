package cn.iocoder.yudao.module.monitor.service.template;

import java.util.*;
import cn.iocoder.yudao.module.monitor.controller.admin.template.vo.*;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.TemplateDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

import javax.validation.Valid;

/**
 *  监控模板 Service 接口
 *
 * @author 智能化运维
 */
public interface TemplateService {

    /**
     * 创建 监控模板
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createTemplate(@Valid TemplateSaveReqVO createReqVO);

    /**
     * 更新 监控模板
     *
     * @param updateReqVO 更新信息
     */
    void updateTemplate(@Valid TemplateSaveReqVO updateReqVO);

    /**
     * 删除 监控模板
     *
     * @param id 编号
     */
    void deleteTemplate(Long id);

    /**
     * 获得 监控模板
     *
     * @param id 编号
     * @return  监控模板
     */
    TemplateDO getTemplate(Long id);

    /**
     * 获得 监控模板分页
     *
     * @param pageReqVO 分页查询
     * @return  监控模板分页
     */
    PageResult<TemplateRespVO> getTemplatePage(TemplatePageReqVO pageReqVO);

}