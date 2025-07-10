package cn.iocoder.yudao.module.monitor.controller.admin.center;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.monitor.controller.admin.center.vo.CenterInfoSimpleVO;
import cn.iocoder.yudao.module.monitor.convert.center.CenterInfoConvert;
import cn.iocoder.yudao.module.monitor.service.center.CenterInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 智算中心")
@RestController
@RequestMapping("/monitor/center-info")
public class CenterInfoController {

    @Resource
    private CenterInfoService centerInfoService;

    /** 获取智算中心下拉框列表 */
    @GetMapping("/simple-list")
    public CommonResult<List<CenterInfoSimpleVO>> getSimpleCenterList() {
        return success(CenterInfoConvert.INSTANCE.convertSimpleList(centerInfoService.getSimpleCenterList()));
    }
}