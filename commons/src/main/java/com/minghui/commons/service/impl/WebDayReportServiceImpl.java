package com.minghui.commons.service.impl;

import com.minghui.commons.dao.WebDayReportDao;
import com.minghui.commons.entity.WebDayReportEntity;
import com.minghui.commons.service.WebDayReportService;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


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