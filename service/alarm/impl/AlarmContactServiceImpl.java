package cn.iocoder.yudao.module.monitor.service.alarm.impl;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.contact.vo.AlarmContactCreateReqVO;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.contact.vo.AlarmContactPageReqVO;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.contact.vo.AlarmContactRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.contact.vo.AlarmContactUpdateReqVO;
import cn.iocoder.yudao.module.monitor.controller.admin.template.vo.TemplateRespVO;
import cn.iocoder.yudao.module.monitor.convert.template.TemplateInfoConvert;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmContactDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.center.CenterInfoDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.template.TemplateDO;
import cn.iocoder.yudao.module.monitor.dal.mysql.alarm.AlarmContactMapper;
import cn.iocoder.yudao.module.monitor.dal.mysql.center.CenterInfoMapper;
import cn.iocoder.yudao.module.monitor.service.alarm.AlarmContactService;
import cn.iocoder.yudao.module.monitor.convert.alarm.AlarmContactConvert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlarmContactServiceImpl implements AlarmContactService {

    @Autowired
    private AlarmContactMapper alarmContactMapper;
    @Resource
    private CenterInfoMapper centerInfoMapper;

    @Override
    public Long createAlarmContact(AlarmContactCreateReqVO reqVO) {
        AlarmContactDO contact = AlarmContactConvert.INSTANCE.convert(reqVO);
        contact.setCreateTime(LocalDateTime.now());
        alarmContactMapper.insert(contact);
        return contact.getId();
    }

    @Override
    public void updateAlarmContact(AlarmContactUpdateReqVO reqVO) {
        AlarmContactDO contact = AlarmContactConvert.INSTANCE.convert(reqVO);
        alarmContactMapper.updateById(contact);
    }

    @Override
    public void deleteAlarmContact(Long id) {
        alarmContactMapper.deleteById(id);
    }

    @Override
    public AlarmContactDO getAlarmContact(Long id) {
        return alarmContactMapper.selectById(id);
    }

    @Override
    public PageResult<AlarmContactRespVO> getAlarmContactPage(AlarmContactPageReqVO reqVO) {
        LambdaQueryWrapper<AlarmContactDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(reqVO.getName() != null && !reqVO.getName().isEmpty(), AlarmContactDO::getName, reqVO.getName())
                .like(reqVO.getPhone() != null && !reqVO.getPhone().isEmpty(), AlarmContactDO::getPhone, reqVO.getPhone());

        wrapper.eq(reqVO.getCenterId() != null, AlarmContactDO::getCenterId, reqVO.getCenterId());


        IPage<AlarmContactDO> myPage = alarmContactMapper.selectPage(
                new Page<>(reqVO.getPageNo(), reqVO.getPageSize()),
                wrapper
        );

        List<AlarmContactDO> list = myPage.getRecords();
        // 批量查询centerName
        Set<Long> centerIds = list.stream()
                .map(AlarmContactDO::getCenterId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, String> centerNameMap = centerIds.isEmpty() ? Collections.emptyMap() :
                centerInfoMapper.selectBatchIds(centerIds)
                        .stream()
                        .collect(Collectors.toMap(CenterInfoDO::getId, CenterInfoDO::getName));

        List<AlarmContactRespVO> voList = list.stream().map(alarmContact-> {
            AlarmContactRespVO vo = AlarmContactConvert.INSTANCE.convert(alarmContact);
            vo.setCenterName(centerNameMap.get(alarmContact.getCenterId()));
            return vo;
        }).collect(Collectors.toList());

        return new PageResult<>(voList, myPage.getTotal());
    }

    @Override
    public List<AlarmContactDO> getAlarmContactList() {
        return alarmContactMapper.selectList(null);
    }

    @Override
    public List<String> getEmailsByCenterId(Long centerId) {
        if (centerId == null) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AlarmContactDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AlarmContactDO::getCenterId, centerId);
        List<AlarmContactDO> contacts = alarmContactMapper.selectList(wrapper);
        return contacts.stream()
                .map(AlarmContactDO::getEmail)
                .filter(email -> email != null && !email.isEmpty())
                .collect(Collectors.toList());
    }
}
