package com.minghui.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.minghui.commons.entity.WebDictEntity;
import com.minghui.commons.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-04-07 00:46:06
 */
public interface WebDictService extends IService<WebDictEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<WebDictEntity> getAll();
}

