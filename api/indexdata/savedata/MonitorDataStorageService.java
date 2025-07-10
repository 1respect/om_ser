package cn.iocoder.yudao.module.monitor.api.indexdata.savedata;
import cn.iocoder.yudao.module.monitor.dal.dataobject.index.MonitorDataDO;
import cn.iocoder.yudao.module.monitor.dal.mysql.index.MonitorDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class MonitorDataStorageService {

    @Autowired
    private MonitorDataMapper monitorDataMapper;

    public void saveBatch(List<MonitorDataDO> dataList) {
        if (dataList != null && !dataList.isEmpty()) {
            monitorDataMapper.insertBatch(dataList); // 使用自定义的批量插入方法

        }
    }
}

