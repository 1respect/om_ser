
package cn.iocoder.yudao.module.monitor.service.machine;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.common.util.validation.ValidationUtils;
import cn.iocoder.yudao.module.monitor.controller.admin.machine.vo.MonitorEntityTempExcelVO;
import cn.iocoder.yudao.module.monitor.controller.admin.machine.vo.ServerImportExcelVO;
import cn.iocoder.yudao.module.monitor.controller.admin.machine.vo.ServerImportRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.machine.vo.ServerInfoSaveReqVO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.asset.MonitorEntityTempDO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.machine.ServerInfoDO;
import cn.iocoder.yudao.module.monitor.dal.mysql.asset.AssetMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import com.alibaba.excel.EasyExcel;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.USER_IMPORT_LIST_IS_EMPTY;

/**
 * IoT 设备 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class ImportExcelServiceImpl implements ImportExcelService {

    @Resource
    private cn.iocoder.yudao.module.monitor.dal.mysql.machine.ServerInfoMapper serverInfoDOMapper;

    @Resource
    private AssetMapper AssetMapper;

    /**
     * 导入物理机设备列表
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerImportRespVO importDevice(List<ServerImportExcelVO> importDevices, boolean updateSupport) {
        if (CollUtil.isEmpty(importDevices)) {
            throw new RuntimeException("导入设备列表不能为空");
        }

        int initialCapacity = importDevices.size();
        ServerImportRespVO respVO = ServerImportRespVO.builder()
                .createServerNames(new ArrayList<>(initialCapacity))
                .updateServerNames(new ArrayList<>(initialCapacity))
                .failureServerNames(new LinkedHashMap<>(initialCapacity))
                .build();

        for (ServerImportExcelVO importDevice : importDevices) {
            handleImportDevice(importDevice, updateSupport, respVO);
        }

        return respVO;
    }

    /**
     * 处理单个物理机设备导入
     */
    private void handleImportDevice(ServerImportExcelVO importDevice,
                                    boolean updateSupport,
                                    ServerImportRespVO respVO) {
        String serverName = importDevice.getServername();
        if (StrUtil.isBlank(serverName) ||
                StrUtil.isBlank(importDevice.getSno()) ||
                StrUtil.isBlank(importDevice.getBrand()) ||
                StrUtil.isBlank(importDevice.getModel())) {
            respVO.getFailureServerNames().put(serverName, "存在空字段");
            return;
        }

        try {
            if (updateSupport) {
                // TODO: 实现更新逻辑
                respVO.getUpdateServerNames().add(serverName);
            } else {
                createDevice(new ServerInfoSaveReqVO()
                        .setServerName(serverName)
                        .setSno(importDevice.getSno())
                        .setBrand(importDevice.getBrand())
                        .setModel(importDevice.getModel())
                        .setMonitorEntityTempId(importDevice.getMonitorEntityTempId()));
                respVO.getCreateServerNames().add(serverName);
            }
        } catch (ServiceException ex) {
            log.error("设备导入失败，设备名称：{}", serverName, ex);
            respVO.getFailureServerNames().put(serverName, ex.getMessage());
        } catch (Exception ex) {
            log.error("未知错误导致设备导入失败，设备名称：{}", serverName, ex);
            respVO.getFailureServerNames().put(serverName, "未知错误：" + ex.getMessage());
        }
    }

    /**
     * 将物理机保存到数据库
     */
    public Long createDevice(ServerInfoSaveReqVO saveReqVO) {
        ServerInfoDO serverInfo = BeanUtils.toBean(saveReqVO, ServerInfoDO.class);
        serverInfoDOMapper.insert(serverInfo);
        return serverInfo.getId();
    }


    @Override
    @Transactional(rollbackFor = Exception.class) // 添加事务，异常则回滚所有导入
    public ServerImportRespVO importDataList(List<MonitorEntityTempExcelVO> importUsers, boolean isUpdateSupport) {
        // 1.1 参数校验
        if (CollUtil.isEmpty(importUsers)) {
            throw exception(USER_IMPORT_LIST_IS_EMPTY);
        }

        // 2. 遍历，逐个创建 or 更新
        ServerImportRespVO respVO = ServerImportRespVO.builder().createServerNames(new ArrayList<>())
                .updateServerNames(new ArrayList<>()).failureServerNames(new LinkedHashMap<>()).build();
        importUsers.forEach(importUser -> {
            // 2.1.1 校验字段是否符合要求

            // 2.2.1 判断如果不存在，在进行插入
            MonitorEntityTempDO existUser = AssetMapper.selectById(importUser.getSnCode());
            if (existUser == null) {
                AssetMapper.insert(BeanUtils.toBean(importUser, MonitorEntityTempDO.class));
                respVO.getCreateServerNames().add(importUser.getSnCode());
                return;
            }
        });
        return respVO;
    }

}