
package cn.iocoder.yudao.module.monitor.api.indexdata.http;

import java.util.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.iocoder.yudao.module.monitor.api.indexdata.alarminfo.AlarmCheckService;
import cn.iocoder.yudao.module.monitor.api.indexdata.alarminfo.DeviceWithMetricConfigVO;
import cn.iocoder.yudao.module.monitor.api.indexdata.alarminfo.MonitorDataAndEntityVO;
import cn.iocoder.yudao.module.monitor.api.indexdata.savedata.MonitorDataStorageService;
import cn.iocoder.yudao.module.monitor.dal.dataobject.index.MonitorDataDO;
import cn.iocoder.yudao.module.monitor.dal.mysql.alarm.MonitorEntityTempMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import com.google.common.util.concurrent.RateLimiter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;

/**
 * 监控数据服务类，负责定时获取和处理监控数据
 */
@Service
@Slf4j
public class MonitorDataService {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MonitorEntityTempMapper monitorEntityTempMapper;

    @Autowired
    private Executor monitorDataFetchExecutor;

    @Autowired
    private MonitorDataFetchProperties properties;

    @Autowired
    private MonitorDataStorageService storageService;

    @Autowired
    private AlarmCheckService alarmCheckService;

    private RateLimiter rateLimiter;
    private int waitAttempts =4; // 每个设备检测最大尝试次数
    private long waitIntervalMillis = 5000L; // 每次尝试间隔(ms)

    /**
     * 初始化方法，用于创建和配置RateLimiter
     */
    @PostConstruct
    public void init() {
        double qpsLimit = properties.getQpsLimit();
        if (qpsLimit > 0) {
            this.rateLimiter = RateLimiter.create(qpsLimit);
            log.info("已启用限流器，QPS限制为: {}", qpsLimit);
        } else {
            this.rateLimiter = null;
            log.warn("未启用限流器，qpsLimit 设置为: {}", qpsLimit);
        }
    }

    /**
     * 定时任务，用于定期收集和保存监控数据
     */
    @Scheduled(fixedRateString = "${monitor.data.fetch.fixed-rate-millis}")
    public void scheduledCollectAndSave() {
        boolean isEnabled = Optional.ofNullable(properties.isEnabled()).orElse(true);
        if (!isEnabled) {
            log.info("数据采集任务已禁用，跳过本次执行");
            return;
        }

        int currentPage = 1;
        int pageSize = 50;
        LocalDateTime now = LocalDateTime.now();
        List<MonitorDataDO> buffer = new ArrayList<>();
        List<MonitorDataAndEntityVO> bufferVO = new ArrayList<>();
        try {
            while (true) {

                List<DeviceWithMetricConfigVO> devices = monitorEntityTempMapper.selectDeviceWithMetricConfig(currentPage, pageSize);
                int total = monitorEntityTempMapper.countDeviceWithMetricConfig(); // 假设存在此方法用于获取总数
                Page<DeviceWithMetricConfigVO> pageResult = new Page<>(currentPage, pageSize, total);
                pageResult.setRecords(devices);

                log.info("正在处理第 {} 页数据，共 {} 条记录", currentPage, devices.size());

                if (CollectionUtil.isEmpty(devices)) {
                    break;
                }

                List<CompletableFuture<Void>> futures = new ArrayList<>();

                for (DeviceWithMetricConfigVO device : devices) {
                    String snCode = device.getSnCode();
                    if(null !=snCode) {
                        Long currentPdiIndex = Optional.ofNullable(device.getPdiIndex()).orElse(0L);
                        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                            try {

                                String response = fetchDataWithRetry(currentPdiIndex, snCode);

                               //增加延迟请求
                               // String response = fetchDataWithWait(currentPdiIndex, snCode);

                                processDeviceMetrics(response, buffer,bufferVO, now, device);
                            } catch (Exception e) {

                                log.error("采集失败且达到最大重试次数，设备sn_code={}", snCode);
                            }
                        }, monitorDataFetchExecutor);

                        futures.add(future);
                    }
                }

                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

                if (!buffer.isEmpty()) {
                    storageService.saveBatch(buffer);       // 批量保存监控数据
                    alarmCheckService.checkAndGenerateAlarms(bufferVO);// 判断并生成报警

                    buffer.clear();
                    bufferVO.clear();
                }

                currentPage++;
            }
        } catch (Exception e) {
            log.error("定时采集任务发生异常", e);
        }
    }

    /**
     * 使用重试策略获取监控数据
     *
     * @param pdiIndex 设备的PDI索引
     * @param snCode 设备序列号
     * @return 设备监控数据的JSON字符串
     * @throws Exception 抛出异常如果数据获取失败
     */
//    @Retryable(maxAttemptsExpression = "#{@environment.getProperty('monitor.data.fetch.retry-max-attempts', Integer.class)}",
//            backoff = @Backoff(delayExpression = "#{@environment.getProperty('monitor.data.fetch.retry-delay-millis', Long.class)}"))
//    public String fetchDataWithRetry(Long pdiIndex, String snCode) throws Exception {
//        String url = String.format("%s://%s%s?sn_code=%s",
//                properties.getScheme(), properties.getHost(), properties.getPath(), snCode);
//
//        if (rateLimiter != null) {
//            rateLimiter.acquire(); // 获取令牌
//        }
//
//        try (CloseableHttpClient client = shouldIgnoreCertificate() ? createTrustAllClient() : HttpClients.createDefault()) {
//            HttpGet request = new HttpGet(url);
//            HttpResponse response = client.execute(request);
//
//            if (response.getStatusLine().getStatusCode() >= 400) {
//                String responseBody = EntityUtils.toString(response.getEntity());
//                throw new RuntimeException("HTTP 错误: " + response.getStatusLine().getStatusCode() +
//                        ", 响应内容: " + responseBody);
//            }
//
//            return EntityUtils.toString(response.getEntity());
//        }
//    }

    /**
     * 判断是否忽略SSL证书验证
     * @return 如果配置为忽略证书返回 true，否则返回 false
     */
    private boolean shouldIgnoreCertificate() {
        //return Optional.ofNullable(properties.getIgnoreCertificate()).orElse(false);
        return true;
    }

    /**
     * 创建一个信任所有SSL证书的HttpClient
     *
     * @return 一个信任所有SSL证书的CloseableHttpClient实例
     * @throws Exception 如果SSL上下文创建失败
     */
    private CloseableHttpClient createTrustAllClient() throws Exception {
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(null, (chain, authType) -> true)
                .build();

        return HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier((hostname, session) -> true)
                .build();
    }

    /**
     * 处理设备的监控指标数据
     *
     * @param response 设备监控数据的JSON字符串
     * @param buffer 存储监控数据的缓冲区
     * @param now 当前时间
     *
     */
    public void processDeviceMetrics(String response, List<MonitorDataDO> buffer, List<MonitorDataAndEntityVO> bufferVO,LocalDateTime now,
                                     DeviceWithMetricConfigVO device) {
        if (response == null || response.trim().isEmpty()) return;

        try {
            Long id = device.getId();

            Long parentId = device.getParentId();
            Long typeId = device.getTypeId();
            Long templateId = device.getTemplateId();
            JsonNode root = mapper.readTree(response);
            JsonNode deviceArr = root.get("device");
             Long objId = device.getId();
            if (deviceArr != null && deviceArr.isArray()) {
                for (JsonNode devNode : deviceArr) {
                    if (!devNode.has("device_sn_code")) continue;
                    String deviceId = devNode.get("device_sn_code").asText();

                    JsonNode metrics = devNode.get("device_metrics");
                    if (metrics != null && metrics.isArray()) {
                        for (JsonNode metric : metrics) {
                            MonitorDataDO record = new MonitorDataDO();
                            record.setDeviceId(deviceId);
                            record.setMonitorObjId(objId);
                            record.setParentMonObjId(parentId);
                            record.setTypeId(typeId);
                            if (metric.has("metric_name")) {
                                record.setMetricName(metric.get("metric_name").asText());
                            }
                            if (metric.has("metric_value")) {
                                record.setMetricValue(metric.get("metric_value").asText());
                            }
                            record.setCollectionTime(now);
                            record.setReportTime(now);
                            record.setServerTime(now);
                            buffer.add(record);


                            MonitorDataAndEntityVO  vo = new MonitorDataAndEntityVO();
                            vo.setDeviceId(deviceId);
                            vo.setMonitorObjId(objId);
                            vo.setParentMonObjId(parentId);
                            vo.setTypeId(typeId);
                            if (metric.has("metric_name")) {
                                vo.setMetricName(metric.get("metric_name").asText());
                            }
                            if (metric.has("metric_value")) {
                                vo.setMetricValue(metric.get("metric_value").asText());
                            }
                            vo.setCollectionTime(now);
                            vo.setReportTime(now);
                            vo.setServerTime(now);
                            vo.setTemplateId(templateId);
                            vo.setMetricId(device.getMetricId());
                            bufferVO.add(vo);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("解析设备指标失败", e);
        }
    }


    /**
     * 单设备采集数据：  HTTPS方案   采集不到时自动轮询/等待
     */

    @Retryable(maxAttemptsExpression = "#{@environment.getProperty('monitor.data.fetch.retry-max-attempts', Integer.class)}",
            backoff = @Backoff(delayExpression = "#{@environment.getProperty('monitor.data.fetch.retry-delay-millis', Long.class)}"))
    public String fetchDataWithWait(Long pdiIndex, String snCode) throws Exception {
        String url = String.format("%s://%s%s?sn_code=%s",
                properties.getScheme(), properties.getHost(), properties.getPath(), snCode);

        if (rateLimiter != null) rateLimiter.acquire();

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(10000) // 连接超时时间（毫秒）
                .setSocketTimeout(10000)  // 读取超时时间（毫秒）
                .build();

          CloseableHttpClient client;
          if (shouldIgnoreCertificate()) {
            client = createTrustAllClient(); // 已包含 SSL 配置
        } else {
            client = HttpClients.custom()
                    .setDefaultRequestConfig(config)
                    .build();
        }

        for (int attempt = 1; attempt <= waitAttempts; attempt++) {
            try {
                HttpGet request = new HttpGet(url);
                request.setHeader("User-Agent", "Mozilla/5.0"); // 添加 User-Agent
                request.setHeader("Content-Type", "application/json");

                HttpResponse response = client.execute(request);
                int statusCode = response.getStatusLine().getStatusCode();
                String result = EntityUtils.toString(response.getEntity());

                if (statusCode >= 400) {
                    log.warn("HTTP错误: %d, 响应内容: %s", statusCode, result);
                    Thread.sleep(waitIntervalMillis);
                } else if (isValidData(result)) {
                    return result;
                }

                // 没拿到数据则等待后再试
                if (attempt < waitAttempts) {
                    Thread.sleep(waitIntervalMillis);
                }
            } catch (Exception ex) {
                log.error("设备sn_code={} 第{}次请求异常: {}", snCode, attempt, ex.getMessage());
                if (attempt < waitAttempts) {
                    Thread.sleep(waitIntervalMillis);
                }
            }
        }

        log.warn("设备sn_code={} 轮询{}次后仍未获取到有效数据", snCode, waitAttempts);
        return null;
    }


    /**
     * 判断响应数据是否有效
     */
    private boolean isValidData(String result) {
        try {
            if (result == null || result.trim().isEmpty()) return false;
            JsonNode root = mapper.readTree(result);
            JsonNode deviceArr = root.get("device");
            return deviceArr != null && deviceArr.isArray() && deviceArr.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }


    //===========================HTTP方案================================================
    /**
     * 使用重试策略获取监控数据
     *
     * @param pdiIndex 设备的PDI索引
     * @param snCode 设备序列号
     * @return 设备监控数据的JSON字符串
     * @throws Exception 抛出异常如果数据获取失败
     */
    @Retryable(maxAttemptsExpression = "#{@environment.getProperty('monitor.data.fetch.retry-max-attempts', Integer.class)}",
            backoff = @Backoff(delayExpression = "#{@environment.getProperty('monitor.data.fetch.retry-delay-millis', Long.class)}"))
    public String fetchDataWithRetry(Long pdiIndex, String snCode) throws Exception {
        String url = String.format("%s://%s:%d%s?sn_code=%s",
                properties.getScheme(), properties.getHost(), properties.getPort(), properties.getPath(), snCode);

        if (rateLimiter != null) {
            rateLimiter.acquire(); // 获取令牌
        }

        try (CloseableHttpClient client = shouldIgnoreCertificate() ? createTrustAllClient() : HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() >= 400) {
                String responseBody = EntityUtils.toString(response.getEntity());
                throw new RuntimeException("HTTP 错误: " + response.getStatusLine().getStatusCode() +
                        ", 响应内容: " + responseBody);
            }

            return EntityUtils.toString(response.getEntity());
        }
    }


}