package cn.iocoder.yudao.module.monitor.controller.admin.template.vo;

import cn.iocoder.yudao.module.monitor.controller.admin.asset.vo.MonitorEntityTempRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.template.alertrule.vo.AlertRuleRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.template.metricconfig.vo.MetricConfigRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 -  监控模板 Response VO")
@Data
@ExcelIgnoreUnannotated
public class TemplateRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "2033")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "模版名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("模版名称")
    private String templateName;

    @Schema(description = "智算中心ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("智算中心ID")
    private Long centerId;

    @Schema(description = "监控对象类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "31486")
    @ExcelProperty("监控对象类型ID")
    private Long typeId;

    @Schema(description = "协议（HTTP/HTTPS/其他）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("协议（HTTP/HTTPS/其他）")
    private String protocol;

    @Schema(description = "请求方式（GET/POST）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("请求方式（GET/POST）")
    private String requestMethod;

    @Schema(description = "请求URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @ExcelProperty("请求URL")
    private String url;

    @Schema(description = "监控周期(秒)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("监控周期(秒)")
    private Integer period;

    @Schema(description = "超时设置(秒)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("超时设置(秒)")
    private Integer timeout;

    @Schema(description = "用户名", example = "张三")
    @ExcelProperty("用户名")
    private String username;

    @Schema(description = "密码")
    @ExcelProperty("密码")
    private String password;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    private List<MetricConfigRespVO> metrics;
//    private List<AlertRuleRespVO> alarms;
//    private List<MonitorEntityTempRespVO> targets;

    private String centerName;
    private String typeName;


}