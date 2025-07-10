package cn.iocoder.yudao.module.monitor.controller.admin.template.vo;

import cn.iocoder.yudao.module.monitor.controller.admin.asset.vo.MonitorEntityTempRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.template.alertrule.vo.AlertRuleRespVO;
import cn.iocoder.yudao.module.monitor.controller.admin.template.metricconfig.vo.MetricConfigRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

@Schema(description = "管理后台 -  监控模板新增/修改 Request VO")
@Data
public class TemplateSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "2033")
    private Long id;

    @Schema(description = "模版名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "模版名称不能为空")
    private String templateName;

    @Schema(description = "智算中心ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9151")
    @NotNull(message = "智算中心ID不能为空")
    private Long centerId;

    @Schema(description = "监控对象类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "31486")
    @NotNull(message = "监控对象类型ID不能为空")
    private Long typeId;

    @Schema(description = "协议（HTTP/HTTPS/其他）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "协议（HTTP/HTTPS/其他）不能为空")
    private String protocol;

    @Schema(description = "请求方式（GET/POST）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "请求方式（GET/POST）不能为空")
    private String requestMethod;

    @Schema(description = "请求URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @NotEmpty(message = "请求URL不能为空")
    private String url;

    @Schema(description = "监控周期(秒)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "监控周期(秒)不能为空")
    private Integer period;

    @Schema(description = "超时设置(秒)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "超时设置(秒)不能为空")
    private Integer timeout;

    @Schema(description = "用户名", example = "张三")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "数据源类型")
    private Integer sourceType;

}