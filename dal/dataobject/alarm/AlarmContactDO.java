package cn.iocoder.yudao.module.monitor.dal.dataobject.alarm;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;


@TableName("monitor_alert_contact")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
public class AlarmContactDO {

    private Long id;
    private Long centerId;
    private String name;
    private String phone;
    private String email;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
