package cn.iocoder.yudao.module.monitor.service.center;

import cn.iocoder.yudao.module.monitor.dal.dataobject.center.CenterInfoDO;
import cn.iocoder.yudao.module.monitor.dal.mysql.center.CenterInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CenterInfoServiceImpl implements CenterInfoService {

    @Resource
    private CenterInfoMapper centerInfoMapper;

    @Override
    public List<CenterInfoDO> getSimpleCenterList() {
        return centerInfoMapper.selectList(
                new LambdaQueryWrapper<CenterInfoDO>().eq(CenterInfoDO::getStatus, 1)
        );
    }
}