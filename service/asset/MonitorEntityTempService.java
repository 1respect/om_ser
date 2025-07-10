package cn.iocoder.yudao.module.monitor.service.asset;

import java.util.*;
import cn.iocoder.yudao.module.monitor.controller.admin.asset.vo.*;
import cn.iocoder.yudao.module.monitor.dal.dataobject.asset.MonitorEntityTempDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.monitor.dal.dataobject.asset.MonitorEntityTempParentDO;

import javax.validation.Valid;

/**
 * 监控对象层级结构 Service 接口
 *
 * @author 智能化运维
 */
public interface MonitorEntityTempService {

    /**
     * 创建监控对象层级结构
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createEntityTemp(@Valid MonitorEntityTempSaveReqVO createReqVO);

    /**
     * 更新监控对象层级结构
     *
     * @param updateReqVO 更新信息
     */
    void updateEntityTemp(@Valid MonitorEntityTempSaveReqVO updateReqVO);

    /**
     * 删除监控对象层级结构
     *
     * @param id 编号
     */
    void deleteEntityTemp(Long id);

    /**
     * 获得监控对象层级结构
     *
     * @param id 编号
     * @return 监控对象层级结构
     */
    MonitorEntityTempDO getEntityTemp(Long id);

    /**
     * 获得监控对象层级结构分页
     *
     * @param pageReqVO 分页查询
     * @return 监控对象层级结构分页
     */
    PageResult<MonitorEntityTempDO> getEntityTempPage(MonitorEntityTempPageReqVO pageReqVO);


    /**
     * 获取监控对象层级结构树
     *
     * @return 树形结构数据
     */
    List<MonitorEntityTempDO> getEntityTempTree();

    /**
     * 根据父节点ID获取子节点数据
     *
     * @param parentId 父节点ID
     * @return 子节点数据列表
     */
    List<MonitorEntityTempNodeDO> getEntityTempChildren(Long parentId);

    /**
     * 根据子节点ID获取父节点数据
     * @param childId
     * @return
     */
    List< MonitorEntityTempParentDO> selectListByParentDetail(Long childId);

    /**
     * 根据类型ID获取监控对象层级结构数据
     *
     * @param typeId 类型ID
     * @return 监控对象层级结构数据列表
     */
    List<MonitorEntityTempNodeDO> getEntityTempByTypeId(Long typeId);


    /**
     * 批量设置模板到指定对象
     *
     * @param templateId 模板ID
     * @param entityIds 对象ID列表
     */
    void batchSetTemplateToEntities(Long templateId, List<Long> entityIds);

    List<MonitorEntityTempDO> getEntityTempByCenterId(Long centerId,Long typeId);
}
