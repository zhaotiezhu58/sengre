package com.minghui.commons.service.impl;

import com.minghui.commons.dao.WebDictDao;
import com.minghui.commons.entity.WebDictEntity;
import com.minghui.commons.service.WebDictService;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("webDictService")
public class WebDictServiceImpl extends ServiceImpl<WebDictDao, WebDictEntity> implements WebDictService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebDictEntity> page = this.page(
                new Query<WebDictEntity>().getPage(params),
                new QueryWrapper<WebDictEntity>()
        );

        return new PageUtils(page);
    }

    @Cacheable(value = {"web:dict"}, key = "'list'", unless="#result == null")
    @Override
    public List<WebDictEntity> getAll() {
        return this.list(
                new QueryWrapper<WebDictEntity>().lambda()
                        .eq(WebDictEntity::getStatus, 1));
    }
}