package cn.iocoder.yudao.module.monitor.service.asset;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.monitor.dal.dataobject.asset.MonitorEntityTempParentDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import cn.iocoder.yudao.module.monitor.controller.admin.asset.vo.*;
import cn.iocoder.yudao.module.monitor.dal.dataobject.asset.MonitorEntityTempDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.monitor.dal.mysql.asset.AssetMapper;
import javax.annotation.Resource;
import java.util.*;


/**
 * 监控对象层级结构 Service 实现类
 *
 * @author 智能化运维
 */
@Service
@Validated
public class MonitorEntityTempServiceImpl implements MonitorEntityTempService {

    @Resource
    private AssetMapper entityTempMapper;

    @Override
    public Long createEntityTemp(MonitorEntityTempSaveReqVO createReqVO) {
        // 插入
        MonitorEntityTempDO  po= new MonitorEntityTempDO();
        po.setParentId(createReqVO.getFid());
        po.setTypeId(createReqVO.getTypeId());
        po.setRemark(createReqVO.getRemark());
        po.setName(createReqVO.getName());
       // MonitorEntityTempDO entityTemp = BeanUtils.toBean(vo, MonitorEntityTempDO.class);
        entityTempMapper.insert(po);
        // 返回
        return po.getId();
    }

    @Override
    public void updateEntityTemp(MonitorEntityTempSaveReqVO updateReqVO) {
        // 校验存在
        validateEntityTempExists(updateReqVO.getId());
        // 更新
        MonitorEntityTempDO updateObj = BeanUtils.toBean(updateReqVO, MonitorEntityTempDO.class);
        entityTempMapper.updateById(updateObj);
    }

    @Override
    public void deleteEntityTemp(Long id) {
        // 校验存在
        validateEntityTempExists(id);
        // 删除
        entityTempMapper.deleteById(id);
    }

    private void validateEntityTempExists(Long id) {
        if (entityTempMapper.selectById(id) == null) {
            throw new RuntimeException();
        }
    }

    @Override
    public MonitorEntityTempDO getEntityTemp(Long id) {
        return entityTempMapper.selectById(id);
    }

    @Override
    public PageResult<MonitorEntityTempDO> getEntityTempPage(MonitorEntityTempPageReqVO pageReqVO) {
        return entityTempMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MonitorEntityTempDO> getEntityTempTree() {
        // 查询所有数据
       return  this.entityTempMapper.selectList();
    }

    @Override
    public List<MonitorEntityTempNodeDO> getEntityTempChildren(Long parentId) {
        // 根据 parentId 查询子节点数据
        return this.entityTempMapper.selectListByParentId(parentId);
    }

    @Override
    public List<MonitorEntityTempParentDO> selectListByParentDetail(Long childId) {
        // 根据 parentId 查询子节点数据
        return this.entityTempMapper.selectListByParentDetail(childId);
    }

    @Override
    public List<MonitorEntityTempNodeDO> getEntityTempByTypeId(Long typeId) {
        return this.entityTempMapper.selectListByTypeId(typeId);
    }

    @Override
    public void batchSetTemplateToEntities(Long templateId, List<Long> entityIds) {
        // 1. 先将所有属于该模板类型、但不在 entityIds 中的对象的 templateId 置为 null 或 0
        // 2. 再将 entityIds 中的对象 templateId 设置为指定 templateId

        // 假设需要 typeId，可以通过 entityIds 查询 typeId，或者方法参数中传入 typeId
        // 这里只对 entityIds 进行设置，未选中的对象可不处理或置为null

        if (entityIds == null) entityIds = new ArrayList<>();
        // 1. 清空当前模板已绑定但未被勾选的对象的templateId
        // 查询所有已绑定到该模板的对象
        List<MonitorEntityTempDO> alreadyBound = entityTempMapper.selectList(
                new LambdaQueryWrapper<MonitorEntityTempDO>().eq(MonitorEntityTempDO::getTemplateId, templateId)
        );
        for (MonitorEntityTempDO entity : alreadyBound) {
            if (!entityIds.contains(entity.getId())) {
                entity.setTemplateId(0L); // 或 0，看你的数据库设计
                entityTempMapper.updateById(entity);
            }
        }
        // 2. 设置本次被勾选的对象templateId
        if (!entityIds.isEmpty()) {
            entityTempMapper.update(
                    null,
                    new LambdaUpdateWrapper<MonitorEntityTempDO>()
                            .in(MonitorEntityTempDO::getId, entityIds)
                            .set(MonitorEntityTempDO::getTemplateId, templateId)
            );
        }
    }

    @Override
    public List<MonitorEntityTempDO> getEntityTempByCenterId(Long centerId,Long typeId) {
        List<MonitorEntityTempDO> monitorEntityTempDOS = entityTempMapper.selectList(new LambdaQueryWrapper<MonitorEntityTempDO>().eq(MonitorEntityTempDO::getCenterId, centerId).eq(MonitorEntityTempDO::getTypeId, typeId));
        if (monitorEntityTempDOS != null && !monitorEntityTempDOS.isEmpty()) {
            return monitorEntityTempDOS;
        }else {
            throw new ServiceException(400,"当前智算中心下不存在可选的设备列表");
        }
    }


}