package io.renren.modules.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.user.dao.WebDayReportDao;
import io.renren.modules.user.entity.WebDayReportEntity;
import io.renren.modules.user.service.WebDayReportService;


@Service("webDayReportService")
public class WebDayReportServiceImpl extends ServiceImpl<WebDayReportDao, WebDayReportEntity> implements WebDayReportService {

    @Autowired
    private WebDayReportDao webDayReportDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebDayReportEntity> page = this.page(
                new Query<WebDayReportEntity>().getPage(params),
                new QueryWrapper<WebDayReportEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void batchInsertOrUpdate(List<WebDayReportEntity> list) throws Exception {
        int i = webDayReportDao.batchInsertOrUpdate(list);
        if (i <= 0) {
            throw new Exception("修改日报表失败");
        }
    }

    @Override
    public void insertOrUpdate(WebDayReportEntity report) throws Exception {
        int i = webDayReportDao.insertOrUpdate(report);
        if (i <= 0) {
            throw new Exception("修改日报表失败");
        }
    }
}