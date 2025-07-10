package cn.iocoder.yudao.module.monitor.api.indexdata.http;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

import javax.annotation.PostConstruct;

/**
 * 监控数据采集相关配置属性类
 */
@Component
@ConfigurationProperties(prefix = "monitor.data.fetch")
@Data
public class MonitorDataFetchProperties {

    /**
     * 请求协议（如 http 或 https）
     */
    private String scheme;

    /**
     * 接口主机地址（如 172.20.101.1）
     */
    private String host;

    /**
     * 接口路径（如 /admin/Ninterface/real_data）
     */
    private String path;

    /**
     * 定时任务执行间隔时间（毫秒）
     */
    private long fixedRateMillis;

    /**
     * 线程池核心线程数
     */
    private int corePoolSize;

    /**
     * 线程池最大线程数
     */
    private int maxPoolSize;

    /**
     * 线程池队列容量
     */
    private int queueCapacity;

    /**
     * 最大重试次数
     */
    private int retryMaxAttempts;

    /**
     * 重试间隔时间（毫秒）
     */
    private long retryDelayMillis;

    /**
     * 每秒请求限制（QPS）
     */
    private int qpsLimit;
    private boolean enabled = true;

    private int port;
    @PostConstruct
    public void validate() {
        if (scheme == null || host == null || path == null || fixedRateMillis <= 0) {
            throw new IllegalStateException("缺少必要配置项，请检查 application.yaml 中 monitor.data.fetch 的配置");
        }
    }
}
