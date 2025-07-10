package cn.iocoder.yudao.module.monitor.controller.admin.machine.vo;


import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * 设备 Excel 导入 VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false) // 设置 chain = false，避免设备导入有问题
public class ServerImportExcelVO {

    @ExcelProperty("物理机名称")
    @NotEmpty(message = "设备名称不能为空")
    private String servername;

    @ExcelProperty("序列号")
    @Schema(description = "序列号", example = "001")
    private String sno;

    @Schema(description = "品牌")
    @ExcelProperty("品牌")
    private String brand;

    @Schema(description = "型号")
    @ExcelProperty("型号")
    private String model;

    @Schema(description = "其挂载到哪个对象下", requiredMode = Schema.RequiredMode.REQUIRED, example = "7853")
    @ExcelProperty("挂载对象")
    private Long monitorEntityTempId;

}
