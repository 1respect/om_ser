package cn.iocoder.yudao.module.monitor.controller.admin.machine.dto;

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
public class TestBmcConnectionDTO {
    private String ip;
    private String account;
    private String password;
}
