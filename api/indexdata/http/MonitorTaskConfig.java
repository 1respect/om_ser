
package cn.iocoder.yudao.module.monitor.api.indexdata.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // 添加导入
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;

/**
 * 监控任务配置类，用于配置监控数据抓取相关的线程池
 */
@Configuration
public class MonitorTaskConfig {

    // 日志记录器
    private static final Logger log = LoggerFactory.getLogger(MonitorTaskConfig.class); // 添加日志实例

    // 监控数据抓取属性
    @Resource
    private MonitorDataFetchProperties monitorDataFetchProperties;

    /**
     * 创建并配置监控数据抓取使用的线程池
     *
     * @return 配置好的线程池执行器
     */
    @Bean("monitorDataFetchExecutor")
    public ThreadPoolTaskExecutor monitorDataFetchExecutor() {
        int corePoolSize = monitorDataFetchProperties.getCorePoolSize();
        int maxPoolSize = monitorDataFetchProperties.getMaxPoolSize();
        int queueCapacity = monitorDataFetchProperties.getQueueCapacity();

        if (corePoolSize <= 0) {
            corePoolSize = 10; // 默认最小线程数
            log.warn("corePoolSize 配置非法，已使用默认值: {}", corePoolSize);
        }

        if (maxPoolSize <= 0 || maxPoolSize < corePoolSize) {
            maxPoolSize = corePoolSize * 2; // 默认最大线程数
            log.warn("maxPoolSize 配置非法，已使用默认值: {}", maxPoolSize);
        }

        if (queueCapacity <= 0) {
            queueCapacity = 200; // 调整默认队列容量
            log.warn("queueCapacity 配置非法，已使用默认值: {}", queueCapacity);
        }

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("monitor-data-fetch-");
        executor.setRejectedExecutionHandler((r, exec) -> {
            log.error("任务被拒绝: {}, 线程池信息: core={}, max={}, currentPoolSize={}, activeCount={}",
                    r.toString(), exec.getCorePoolSize(), exec.getMaximumPoolSize(),
                    exec.getPoolSize(), exec.getActiveCount());
        });
        executor.initialize();
        return executor;
    }
}