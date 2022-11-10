package com.minghui.commons.service.impl;

import com.minghui.commons.dao.WebCountryDao;
import com.minghui.commons.entity.WebCountryEntity;
import com.minghui.commons.service.WebCountryService;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("webCountryService")
public class WebCountryServiceImpl extends ServiceImpl<WebCountryDao, WebCountryEntity> implements WebCountryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebCountryEntity> page = this.page(
                new Query<WebCountryEntity>().getPage(params),
                new QueryWrapper<WebCountryEntity>()
        );

        return new PageUtils(page);
    }

}