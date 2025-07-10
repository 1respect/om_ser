package cn.iocoder.yudao.module.monitor.service.center;

import cn.iocoder.yudao.module.monitor.dal.dataobject.center.CenterInfoDO;

import java.util.List;

public interface CenterInfoService {
    List<CenterInfoDO> getSimpleCenterList();
}