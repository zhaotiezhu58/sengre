package com.minghui.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.minghui.commons.dao.WebLevelDao;
import com.minghui.commons.entity.WebLevelEntity;
import com.minghui.commons.service.WebLevelService;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service("webLevelService")
public class WebLevelServiceImpl extends ServiceImpl<WebLevelDao, WebLevelEntity> implements WebLevelService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebLevelEntity> page = this.page(
                new Query<WebLevelEntity>().getPage(params),
                new QueryWrapper<WebLevelEntity>()
        );

        return new PageUtils(page);
    }

    @Cacheable(value = {"web:levels"}, key = "#type", unless="#result == null")
    @Override
    public List<WebLevelEntity> getLevelsByType(int type) {
        return this.list(new QueryWrapper<WebLevelEntity>().lambda().eq(WebLevelEntity::getLevelType, type).orderByAsc(WebLevelEntity::getLevelValue));
    }

    @Cacheable(value = {"web:level"}, key = "#value", unless="#result == null")
    @Override
    public WebLevelEntity getLevelByLevelValue(int value) {
        return this.getOne(new QueryWrapper<WebLevelEntity>().lambda().eq(WebLevelEntity::getLevelValue, value));
    }

    @Override
    public WebLevelEntity getUserCurrLevel(List<WebLevelEntity> levels, BigDecimal amount) {
        List<WebLevelEntity> list = levels.stream().filter(temp -> NumberUtil.isGreaterOrEqual(amount, temp.getMinBalance()) && NumberUtil.isLessOrEqual(amount, temp.getMaxBalance())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
}