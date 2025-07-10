package cn.iocoder.yudao.module.monitor.service.alarm;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.contact.vo.AlarmContactCreateReqVO;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.contact.vo.AlarmContactPageReqVO;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.contact.vo.AlarmContactRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.alarm.contact.vo.AlarmContactUpdateReqVO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.alarm.AlarmContactDO;

import java.util.List;

public interface AlarmContactService {

    Long createAlarmContact(AlarmContactCreateReqVO reqVO);

    void updateAlarmContact(AlarmContactUpdateReqVO reqVO);

    void deleteAlarmContact(Long id);

    AlarmContactDO getAlarmContact(Long id);

    PageResult<AlarmContactRespVO> getAlarmContactPage(AlarmContactPageReqVO reqVO);

    List<AlarmContactDO> getAlarmContactList();

    /**
     * 根据智算中心ID获取所有紧急联系人邮箱
     */
    List<String> getEmailsByCenterId(Long centerId);

}