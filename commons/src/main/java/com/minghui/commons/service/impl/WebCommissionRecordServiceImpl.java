package com.minghui.commons.service.impl;

import com.minghui.commons.dao.WebCommissionRecordDao;
import com.minghui.commons.entity.WebCommissionRecordEntity;
import com.minghui.commons.service.WebCommissionRecordService;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("webCommissionRecordService")
public class WebCommissionRecordServiceImpl extends ServiceImpl<WebCommissionRecordDao, WebCommissionRecordEntity> implements WebCommissionRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebCommissionRecordEntity> page = this.page(
                new Query<WebCommissionRecordEntity>().getPage(params),
                new QueryWrapper<WebCommissionRecordEntity>()
        );

        return new PageUtils(page);
    }

}