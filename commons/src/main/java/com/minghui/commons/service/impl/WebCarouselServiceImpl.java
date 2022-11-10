package com.minghui.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minghui.commons.entity.WebCarousel;
import com.minghui.commons.service.WebCarouselService;
import com.minghui.commons.dao.WebCarouselDao;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
* @author Administrator
* @description 针对表【t_web_carousel】的数据库操作Service实现
* @createDate 2022-05-12 18:14:32
*/
@Service
public class WebCarouselServiceImpl extends ServiceImpl<WebCarouselDao, WebCarousel> implements WebCarouselService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebCarousel> wrapper = new QueryWrapper<WebCarousel>().lambda();

        IPage<WebCarousel> page = this.page(
            new Query<WebCarousel>().getPage(params),
            wrapper);
        return new PageUtils(page);
    }

    @Cacheable(value = {"web:carousels"}, key = "#type", unless="#result == null")
    @Override
    public List<WebCarousel> getCarouselsByType(int type) {
        return this.list(new QueryWrapper<WebCarousel>().lambda()
                .eq(WebCarousel::getStatus, 1)
                .eq(WebCarousel::getType, type));
    }
}




