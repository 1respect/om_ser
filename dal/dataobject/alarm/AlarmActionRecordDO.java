package cn.iocoder.yudao.module.monitor.dal.dataobject.alarm;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@TableName("monitor_alert_action_record")
@Data
public class AlarmActionRecordDO {
    private Long id;
    private Long alertRecordId;
    private Long userId;
    private String processType;
    private LocalDateTime processTime;
    private String description;
    private String remark;
    private LocalDateTime createTime;
}