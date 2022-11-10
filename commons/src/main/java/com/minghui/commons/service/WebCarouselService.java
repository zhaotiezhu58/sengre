package com.minghui.commons.service;

import com.minghui.commons.entity.WebCarousel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;
import com.minghui.commons.utils.PageUtils;

/**
* @author Administrator
* @description 针对表【t_web_carousel】的数据库操作Service
* @createDate 2022-05-12 18:14:32
*/
public interface WebCarouselService extends IService<WebCarousel> {

    PageUtils queryPage(Map<String,Object> params);

    List<WebCarousel> getCarouselsByType(int type);
}
