package cn.iocoder.yudao.module.monitor.service.machine;


import cn.iocoder.yudao.module.monitor.controller.admin.machine.vo.MonitorEntityTempExcelVO;
import cn.iocoder.yudao.module.monitor.controller.admin.machine.vo.ServerImportExcelVO;
import cn.iocoder.yudao.module.monitor.controller.admin.machine.vo.ServerImportRespVO;

import java.util.List;
public interface ImportExcelService {


    ServerImportRespVO importDevice(List<ServerImportExcelVO> importDevices, boolean updateSupport);

   // void importDeviceTemp(List<MonitorEntityTempExcelVO> importDevices, boolean updateSupport);

    public ServerImportRespVO importDataList(List<MonitorEntityTempExcelVO> importUsers, boolean isUpdateSupport);
}
