package cn.iocoder.yudao.module.monitor.controller.admin.machine.vo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - IoT 设备导入 Response VO")
@Data
@Builder
public class ServerImportRespVO {

    @Schema(description = "创建成功的设备名称数组", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> createServerNames;

    @Schema(description = "更新成功的设备名称数组", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> updateServerNames;

    @Schema(description = "导入失败的设备集合,key为设备名称,value为失败原因", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<String, String> failureServerNames;
}
