package com.minghui.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.minghui.commons.entity.WebCountryEntity;
import com.minghui.commons.utils.PageUtils;

import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-04 18:56:38
 */
public interface WebCountryService extends IService<WebCountryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

