package com.minghui.commons.service.impl;

import com.minghui.commons.dao.WebParamsDao;
import com.minghui.commons.entity.WebParamsEntity;
import com.minghui.commons.service.WebParamsService;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("webParamsService")
public class WebParamsServiceImpl extends ServiceImpl<WebParamsDao, WebParamsEntity> implements WebParamsService {

    @Autowired
    private WebParamsDao webParamsDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebParamsEntity> page = this.page(
                new Query<WebParamsEntity>().getPage(params),
                new QueryWrapper<WebParamsEntity>()
        );

        return new PageUtils(page);
    }

    @Cacheable(value = {"web:params"}, key = "#key", unless="#result == null")
    @Override
    public String getParamsValue(String key) {
        WebParamsEntity params = webParamsDao.selectOne(
                new QueryWrapper<WebParamsEntity>()
                        .eq("params_key", key)
        );
        return params.getParamsValue();
    }

    @Cacheable(value = {"web:params"}, key = "'list'", unless="#result == null")
    @Override
    public List<WebParamsEntity> getAll() {
        return this.list();
    }

}