package cn.iocoder.yudao.module.monitor.service.template;

import cn.iocoder.yudao.module.monitor.controller.admin.alarm.info.vo.AlarmInfoRespVO;
import cn.iocoder.yudao.module.monitor.convert.alarm.AlarmInfoConvert;
import cn.iocoder.yudao.module.monitor.convert.template.TemplateInfoConvert;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmInfoDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmLevelDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.asset.MonitorEntityTempDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.center.CenterInfoDO;
import cn.iocoder.yudao.module.monitor.dal.mysql.alarm.MonitorEntityTempMapper;
import cn.iocoder.yudao.module.monitor.dal.mysql.center.CenterInfoMapper;
import cn.iocoder.yudao.module.system.dal.dataobject.monitor.MonitorTypeDO;
import cn.iocoder.yudao.module.system.dal.mysql.monitor.MonitorTypeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.monitor.controller.admin.template.vo.*;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.TemplateDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.monitor.dal.mysql.template.TemplateMapper;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.MONITOR_TEMPLATE_NOT_FOUND;

/**
 *  监控模板 Service 实现类
 *
 * @author 智能化运维
 */
@Service
@Validated
public class TemplateServiceImpl implements TemplateService {

    @Resource
    private TemplateMapper templateMapper;
    @Resource
    private CenterInfoMapper centerInfoMapper;
    @Resource
    private MonitorEntityTempMapper monitorEntityTempMapper;
    @Resource
    private MonitorTypeMapper monitorTypeMapper;

    @Override
    public Long createTemplate(TemplateSaveReqVO createReqVO) {
        // 插入
        TemplateDO template = BeanUtils.toBean(createReqVO, TemplateDO.class);
        template.setCreateTime(LocalDateTime.now());
        template.setUpdateTime(LocalDateTime.now());
        templateMapper.insert(template);
        // 返回
        return template.getId();
    }

    @Override
    public void updateTemplate(TemplateSaveReqVO updateReqVO) {
        // 校验存在
        validateTemplateExists(updateReqVO.getId());
        // 更新
        TemplateDO updateObj = BeanUtils.toBean(updateReqVO, TemplateDO.class);
        updateObj.setUpdateTime(LocalDateTime.now());
        templateMapper.updateById(updateObj);
    }

    @Override
    public void deleteTemplate(Long id) {
        // 校验存在
        validateTemplateExists(id);
        // 删除
        templateMapper.deleteById(id);
    }

    private void validateTemplateExists(Long id) {
        if (templateMapper.selectById(id) == null) {
            throw exception(MONITOR_TEMPLATE_NOT_FOUND);
        }
    }

    @Override
    public TemplateDO getTemplate(Long id) {
        return templateMapper.selectById(id);
    }

    @Override
    public PageResult<TemplateRespVO> getTemplatePage(TemplatePageReqVO pageReqVO) {

        LambdaQueryWrapper<TemplateDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(pageReqVO.getCenterId() != null, TemplateDO::getCenterId, pageReqVO.getCenterId());
        wrapper.eq(pageReqVO.getTypeId() != null, TemplateDO::getTypeId, pageReqVO.getTypeId());


        IPage<TemplateDO> myPage = templateMapper.selectPage(
                new Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize()),
                wrapper
        );

        List<TemplateDO> list = myPage.getRecords();
        // 批量查询centerName
        Set<Long> centerIds = list.stream()
                .map(TemplateDO::getCenterId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, String> centerNameMap = centerIds.isEmpty() ? Collections.emptyMap() :
                centerInfoMapper.selectBatchIds(centerIds)
                        .stream()
                        .collect(Collectors.toMap(CenterInfoDO::getId, CenterInfoDO::getName));

        // 批量查询监控类型名
        Set<Long> typeIds = list.stream().map(TemplateDO::getTypeId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, String> typeNameMap = typeIds.isEmpty() ? Collections.emptyMap() :
                monitorTypeMapper.selectBatchIds(typeIds)
                        .stream().collect(Collectors.toMap(MonitorTypeDO::getId, MonitorTypeDO::getName));

        // 转换并补全字段
        List<TemplateRespVO> voList = list.stream().map(template  -> {
            TemplateRespVO vo = TemplateInfoConvert.INSTANCE.convert(template);
            vo.setCenterName(centerNameMap.get(template.getCenterId()));
            vo.setTypeName(typeNameMap.get(template.getTypeId()));
            return vo;
        }).collect(Collectors.toList());

        return new PageResult<>(voList, myPage.getTotal());
    }

}