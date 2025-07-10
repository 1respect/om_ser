package cn.iocoder.yudao.module.monitor.service.machine;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.monitor.controller.admin.machine.dto.TestBmcConnectionDTO;
import cn.iocoder.yudao.module.monitor.controller.admin.machine.vo.*;
import cn.iocoder.yudao.module.monitor.dal.dataobject.machine.ServerInfoDO;

import java.util.Collection;
import java.util.List;

/**
 * 物理机管理 Service 接口
 *
 * @author 智能化运维
 */
public interface ServerInfoService {

    /**
     * 创建物理机管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createServerInfo(ServerInfoSaveReqVO createReqVO);


    /**
     * 更新物理机管理
     *
     * @param updateReqVO 更新信息
     */
    void updateServerInfo(ServerInfoSaveReqVO updateReqVO);

    /**
     * 删除物理机管理
     *
     * @param id 编号
     */
    void deleteServerInfo(Long id);

    /**
     * 获得物理机管理
     *
     * @param id 编号
     * @return 物理机管理
     */
    ServerInfoDO getServerInfo(Long id);

    /**
     * 获得物理机管理分页
     *
     * @param pageReqVO 分页查询
     * @return 物理机管理分页
     */
    PageResult<ServerInfoDO> getServerInfoPage(ServerInfoPageReqVO pageReqVO);

    /**
     * 获得监控类型列表
     *
     * @param ids 监控类型编号数组。如果为空，不进行筛选
     * @return 监控类型列表
     */
    List<ServerInfoDO> getServerInfoList(Collection<Long> ids);

    /**
     *  物理加开机
     * @param id 要执行物理机的id
     */
    CommonResult<Boolean> bootable(Long id);

    /**
     *  物理加关机
     * @param id 要执行物理机的id
     */
    CommonResult<Boolean> shutDown(Long id);

    /**
     *  物理加重启
     * @param id 要执行物理机的id
     */
    CommonResult<Boolean> restart(Long id);

    /**
     *  物理加安装系统
     * @param id 要执行物理机的id
     */
    CommonResult<Boolean> cdInstall(Long id);

    /**
     *  测试BMC连接并返回物理信息
     */
    MachineInfoVO testBmcConnection(TestBmcConnectionDTO testBmcConnectionDTO);


}