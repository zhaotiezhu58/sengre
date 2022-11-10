package com.minghui.commons.service.impl;

import com.minghui.commons.dao.WebProductDao;
import com.minghui.commons.entity.WebProductEntity;
import com.minghui.commons.service.WebProductService;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("webProductService")
public class WebProductServiceImpl extends ServiceImpl<WebProductDao, WebProductEntity> implements WebProductService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebProductEntity> page = this.page(
                new Query<WebProductEntity>().getPage(params),
                new QueryWrapper<WebProductEntity>()
        );

        return new PageUtils(page);
    }

    @Cacheable(value = {"web:product"}, key = "'list'", unless="#result == null")
    @Override
    public List<WebProductEntity> getAll() {
        return this.list(new QueryWrapper<WebProductEntity>().lambda().eq(WebProductEntity::getStatus, 1));
    }


    @Cacheable(value = {"web:product"}, key = "#id", unless="#result == null")
    @Override
    public WebProductEntity getProductById(Integer id) {
        return this.getOne(new QueryWrapper<WebProductEntity>().lambda().eq(WebProductEntity::getStatus, 1).eq(WebProductEntity::getId, id));
    }
}