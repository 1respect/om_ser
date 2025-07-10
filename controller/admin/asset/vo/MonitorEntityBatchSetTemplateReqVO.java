package cn.iocoder.yudao.module.monitor.controller.admin.asset.vo;

import lombok.Data;

import java.util.List;

@Data
public class MonitorEntityBatchSetTemplateReqVO {
    private Long templateId;
    private List<Long> entityIds;
}
