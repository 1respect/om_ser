package cn.iocoder.yudao.module.monitor.service.machine;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.monitor.common.HttpResponseCommon;
import cn.iocoder.yudao.module.monitor.config.MonitorProperties;
import cn.iocoder.yudao.module.monitor.controller.admin.machine.dto.TestBmcConnectionDTO;
import cn.iocoder.yudao.module.monitor.controller.admin.machine.vo.MachineInfoVO;
import cn.iocoder.yudao.module.monitor.controller.admin.machine.vo.ServerInfoPageReqVO;
import cn.iocoder.yudao.module.monitor.controller.admin.machine.vo.ServerInfoSaveReqVO;
import cn.iocoder.yudao.module.monitor.dal.dataobject.machine.ServerInfoDO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;

import java.net.URI;
import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.MONITOR_Server_DUPLICATE;

/**
 * 物理机管理 Service 实现类
 *
 * @author 智能化运维
 */
@Service
@Validated
public class ServerInfoServiceImpl implements ServerInfoService {

    @Resource
    private cn.iocoder.yudao.module.monitor.dal.mysql.machine.ServerInfoMapper serverInfoDOMapper;

    @Resource
    private MonitorProperties monitorProperties;

    /**
     * 创建物理机管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */

    @Override
    public Long createServerInfo(ServerInfoSaveReqVO createReqVO) {
        // 插入
        ServerInfoDO serverInfoDO = BeanUtils.toBean(createReqVO, ServerInfoDO.class);
        serverInfoDOMapper.insert(serverInfoDO);
        // 返回
        return serverInfoDO.getId();
    }

    @Override
    public void updateServerInfo(ServerInfoSaveReqVO updateReqVO) {
        // 校验存在
        validateServerInfoExists(updateReqVO.getId());
        // 更新
        ServerInfoDO updateObj = BeanUtils.toBean(updateReqVO, ServerInfoDO.class);
        serverInfoDOMapper.updateById(updateObj);
    }

    @Override
    public void deleteServerInfo(Long id) {
        // 校验存在
        validateServerInfoExists(id);
        // 删除
        serverInfoDOMapper.deleteById(id);
    }

    private void validateServerInfoExists(Long id) {
        if (serverInfoDOMapper.selectById(id) == null) {
            throw exception(MONITOR_Server_DUPLICATE);
        }

    }

    @Override
    public ServerInfoDO getServerInfo(Long id) {
        // 校验存在
        validateServerInfoExists(id);
        return serverInfoDOMapper.selectById(id);
    }

    @Override
    public PageResult<ServerInfoDO> getServerInfoPage(ServerInfoPageReqVO pageReqVO) {
        return serverInfoDOMapper.selectPage(pageReqVO);
    }

    /**
     * 获得监控类型列表
     *
     * @param ids 监控类型编号数组。如果为空，不进行筛选
     * @return 监控类型列表
     */
    @Override
    public List<ServerInfoDO> getServerInfoList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            // 修改为查全部
            return serverInfoDOMapper.selectList(null);
        }
        return serverInfoDOMapper.selectListByIds(ids);
    }

    @Override
    public CommonResult<Boolean> bootable(Long id) {
        try{
            ServerInfoDO serverInfoDO = serverInfoDOMapper.selectById(id);
            if (serverInfoDO.getStatus()==1){
                return CommonResult.error(400,"已经开机的机器不能再次开机");
            }
            if (serverInfoDO.getIp()==null||serverInfoDO.getAccount()==null||serverInfoDO.getPassword()==null){
                return CommonResult.error(400,"要开启的机器缺少ip、account、password中的一项");
            }
            // 1. 开机的URL
            String url = monitorProperties.getIpRoot()+monitorProperties.getBootable();
            HttpResponseCommon httpResponseCommon = startOrShutDownCompute(url,getToken(), serverInfoDO.getIp(), serverInfoDO.getAccount(), serverInfoDO.getPassword(),HttpMethod.POST);
            if (httpResponseCommon.getCode()==0){
                serverInfoDO.setStatus(1);
                serverInfoDOMapper.updateById(serverInfoDO);
                return CommonResult.success(true);
            }else {
                return CommonResult.error(httpResponseCommon.getCode(), httpResponseCommon.getMsg());
            }
        }catch (RuntimeException e) {
            return CommonResult.error(500, e.getMessage()); // 捕获异常并返回错误信息
        }
    }

    @Override
    public CommonResult<Boolean> shutDown(Long id) {
        try{
            ServerInfoDO serverInfoDO = serverInfoDOMapper.selectById(id);
            if (serverInfoDO.getStatus()==0){
                return CommonResult.error(400,"已经关机的机器不能再次关机");
            }
            if (serverInfoDO.getIp()==null||serverInfoDO.getAccount()==null||serverInfoDO.getPassword()==null){
                return CommonResult.error(400,"要开启的机器缺少ip、account、password中的一项");
            }
            String url =  monitorProperties.getIpRoot()+monitorProperties.getShutDown();
            HttpResponseCommon httpResponseCommon = startOrShutDownCompute(url,getToken(), serverInfoDO.getIp(), serverInfoDO.getAccount(), serverInfoDO.getPassword(),HttpMethod.POST);
            if (httpResponseCommon.getCode()==0){
                serverInfoDO.setStatus(0);
                serverInfoDOMapper.updateById(serverInfoDO);
                return CommonResult.success(true);
            }else {
                return CommonResult.error(httpResponseCommon.getCode(), httpResponseCommon.getMsg());
            }
        }catch (RuntimeException e) {
            return CommonResult.error(500, e.getMessage()); // 捕获异常并返回错误信息
        }
    }

    @Override
    public CommonResult<Boolean> restart(Long id) {
        try{
            ServerInfoDO serverInfoDO = serverInfoDOMapper.selectById(id);
            if (serverInfoDO.getStatus()==0){
                return CommonResult.error(400,"关机的机器不能重启");
            }
            String url = monitorProperties.getIpRoot()+monitorProperties.getRestart();
            HttpResponseCommon httpResponseCommon = startOrShutDownCompute(url,getToken(), serverInfoDO.getIp(), serverInfoDO.getAccount(), serverInfoDO.getPassword(),HttpMethod.POST);
            if (httpResponseCommon.getCode()==0){
                return CommonResult.success(true);
            }else {
                return CommonResult.error(httpResponseCommon.getCode(), httpResponseCommon.getMsg());
            }
        }catch (RuntimeException e) {
            return CommonResult.error(500, e.getMessage()); // 捕获异常并返回错误信息
        }
    }


    @Override
    public CommonResult<Boolean> cdInstall(Long id) {
        try{
            ServerInfoDO serverInfoDO = serverInfoDOMapper.selectById(id);
            if (serverInfoDO.getStatus()==0){
                return CommonResult.error(400,"关机的机器不能重装系统");
            }
            String url = monitorProperties.getIpRoot()+monitorProperties.getCdInstall();
            HttpResponseCommon httpResponseCommon = startOrShutDownCompute(url,getToken(), serverInfoDO.getIp(), serverInfoDO.getAccount(), serverInfoDO.getPassword(),HttpMethod.POST);
            if (httpResponseCommon.getCode()==0){
                return CommonResult.success(true);
            }else {
                return CommonResult.error(httpResponseCommon.getCode(), httpResponseCommon.getMsg());
            }
        }catch (RuntimeException e) {
            return CommonResult.error(500, e.getMessage()); // 捕获异常并返回错误信息
        }
    }

    @Override
    public MachineInfoVO testBmcConnection(TestBmcConnectionDTO testBmcConnectionDTO) {
        String token = getToken();
        String url = monitorProperties.getIpRoot()+monitorProperties.getInfo();
        HttpResponseCommon httpResponseCommon = startOrShutDownCompute(url,token, testBmcConnectionDTO.getIp(), testBmcConnectionDTO.getAccount(), testBmcConnectionDTO.getPassword(),HttpMethod.GET);
        if (httpResponseCommon.getCode() == 0) {
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) httpResponseCommon.getData();
            MachineInfoVO machineInfoVO = new MachineInfoVO();
            for (Map<String, Object> data : dataList) {
                machineInfoVO.setServerOem(data.get("Manufacturer").toString());
                machineInfoVO.setSno(data.get("SerialNumber").toString());
                machineInfoVO.setModel(data.get("Model").toString());
            }
            return machineInfoVO;
        }else {
            throw new ServiceException(httpResponseCommon.getCode(), httpResponseCommon.getMsg());
        }

    }


    private String getToken() {
        // 1. 使用 Map 构造请求参数（自动转为JSON）
        Map<String, String> requestBody = new HashMap<>();
        //目前唯一可用物理机的登录用户
        String username = monitorProperties.getUsername();
        //目前唯一可用物理机的登录用户的密码
        String password = monitorProperties.getPassword();
        //目前唯一可用的物理机的请求地址url
        String url = monitorProperties.getUrl();

        requestBody.put("password", password);
        requestBody.put("username", username);
        // 2. 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. 封装请求实体
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        // 4. 发送请求
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000); // 连接超时 10 秒
        factory.setReadTimeout(15000);   // 读取超时 15 秒

        RestTemplate restTemplate = new RestTemplate(factory);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
                return (String) data.get("token");
            } else {
                throw new ServiceException(403,"请求失败，状态码: " + response.getStatusCode());
            }
        } catch (ResourceAccessException e) {
            // 捕获超时或连接拒绝异常
            throw new ServiceException(403,"请求超时或服务器不可达: " + e.getMessage());
        } catch (RestClientException e) {
            // 捕获其他 RestTemplate 异常
            throw new ServiceException(403,"请求失败: " + e.getMessage());
        }
    }



    private HttpResponseCommon startOrShutDownCompute(String url,String token, String endpoint, String account, String password, HttpMethod method) {

        // 2. 设置请求头（携带 X-Token）
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Token", token); // 关键点：添加 Token 到 Header
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. 请求体（根据接口要求，如果是 GET 则无需 body）
        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, String>> hosts = new ArrayList<>();
        Map<String, String> host = new HashMap<>();
        host.put("endpoint", endpoint);
        host.put("username", account);
        host.put("password", password);
        hosts.add(host);
        requestBody.put("hosts", hosts);


        // 4. 发送请求（带超时配置）
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000); // 连接超时 10 秒
        factory.setReadTimeout(60000);   // 读取超时 60 秒

        RestTemplate restTemplate = new RestTemplate(factory);

        try {
            ResponseEntity<String> response = null;
            if (method == HttpMethod.GET) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                        .queryParam("endpoint", endpoint)
                        .queryParam("username", account)
                        .queryParam("password", password);
                URI uri = builder.build().encode().toUri();
                System.out.println("Sending GET request to URL: " + uri);
                HttpEntity<?> entity = new HttpEntity<>(headers);
                // 发送 GET 请求，不需要 body
                 response = restTemplate.exchange(
                        uri,
                        method,
                         entity, // GET 请求不需要 body
                        String.class
                );
            }else {
                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
                response = restTemplate.exchange(
                        url,
                        method, // 根据实际接口方法调整（POST/GET）
                        entity,
                        String.class    // 直接返回原始 JSON 字符串（或定义 Response 类）
                );
            }


            // 5. 处理响应
            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
                    int code = (Integer) responseMap.get("code");
                    Object data = responseMap.get("data");
                    String msg = (String) responseMap.get("msg");
                    return new HttpResponseCommon(code,data,msg);
                } catch (Exception e) {
                    throw new ServiceException(400,"解析响应失败: " + e.getMessage());
                }
            } else {
                throw new ServiceException(403,"HTTP 状态码异常: " + response.getStatusCode());
            }
        } catch (ResourceAccessException e) {
            throw new ServiceException(403,"请求超时: " + e.getMessage());
        } catch (RestClientException e) {
            throw new ServiceException(403,"请求失败: " + e.getMessage());
        }
    }




    }
