package cn.iocoder.yudao.module.monitor.controller.admin.machine.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false)
public class MachineInfoVO {

    private String serverOem;
    private String model;
    private String sno;

}
