package cn.iocoder.yudao.module.monitor.controller.admin.machine.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false) // 设置 chain = false，避免设备导入有问题
public class MonitorEntityTempExcelVO {

    @ExcelProperty("name")
    @NotEmpty(message = "设备名称不能为空")
    private String name;

    @ExcelProperty("snCode")
    @Schema(description = "snCode", example = "001")
    private String snCode;

    @Schema(description = "typeId")
    @ExcelProperty("typeId")
    private Long typeId;

    @Schema(description = "parentId")
    @ExcelProperty("parentId")
    private Long parentId;

    @Schema(description = "pdiIndex")
    @ExcelProperty("pdiIndex")
    private Long pdiIndex;

    @Schema(description = "remark")
    @ExcelProperty("remark")
    private String remark;
}
