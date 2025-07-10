package cn.iocoder.yudao.module.monitor.convert.asset;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.monitor.controller.admin.asset.vo.*;
import cn.iocoder.yudao.module.monitor.dal.dataobject.asset.MonitorEntityTempDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.*;
        import java.util.stream.Collectors;

@Mapper
public interface MonitorEntityTempConvert {

    MonitorEntityTempConvert INSTANCE = Mappers.getMapper(MonitorEntityTempConvert.class);

    MonitorEntityTempRespVO convert(MonitorEntityTempDO bean);

    List<MonitorEntityTempRespVO> convertList(List<MonitorEntityTempDO> list);

    MonitorEntityTempDO convert(MonitorEntityTempSaveReqVO bean);

    default List<MonitorEntityTempTreeVO> convertTree(List<MonitorEntityTempDO> list) {
        // 1. 将 DO 列表转换为 TreeVO 列表
        List<MonitorEntityTempTreeVO> treeVos = CollectionUtils.convertList(list, item -> {
            MonitorEntityTempTreeVO treeVo = new MonitorEntityTempTreeVO();
            treeVo.setId(item.getId());
            treeVo.setLabel(item.getName());
            treeVo.setTypeId(item.getTypeId());
            treeVo.setParentId(item.getParentId());
            treeVo.setRemark(item.getRemark());
            treeVo.setChildren(new ArrayList<>());
            return treeVo;
        });

        // 2. 构建树形结构
        return buildTree(treeVos, 0L);
    }

    default List<MonitorEntityTempTreeVO> buildTree(List<MonitorEntityTempTreeVO> treeVos, Long rootId) {
        // 创建一个 Map，key 为 nodeId，value 为对应的 TreeVO
        Map<Long, MonitorEntityTempTreeVO> treeMap = treeVos.stream()
                .collect(Collectors.toMap(MonitorEntityTempTreeVO::getId, treeVo -> treeVo));

        // 遍历所有节点，找到每个节点的父节点，并将其添加到父节点的 children 列表中
        for (MonitorEntityTempTreeVO treeVo : treeVos) {
            Long parentId = treeVo.getParentId();
            if (parentId == null || parentId.equals(rootId)) {
                // 如果是根节点，则直接添加到结果列表中
                continue;
            }
            MonitorEntityTempTreeVO parentTreeVo = treeMap.get(parentId);
            if (parentTreeVo != null) {
                parentTreeVo.getChildren().add(treeVo);
            }
        }

        // 返回根节点列表
        return treeVos.stream()
                .filter(treeVo -> treeVo.getParentId() == null || treeVo.getParentId().equals(rootId))
                .collect(Collectors.toList());
    }
}
