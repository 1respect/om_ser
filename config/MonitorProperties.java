package cn.iocoder.yudao.module.monitor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "monitor")
@Component
@Data
public class MonitorProperties {
    private String ipRoot;
    private String bootable;//开机的url
    private String shutDown;//关机的url
    private String restart;//重启的url
    private String cdInstall;// 安装系统的url
    private String username;//获取token的用户名
    private String password;//获取token的密码
    private String url;//获取token的url
    private String info;
}
