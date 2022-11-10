package com.minghui.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.minghui.commons.entity.WebNoticeEntity;
import com.minghui.commons.utils.PageUtils;

import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-04 18:44:56
 */
public interface WebNoticeService extends IService<WebNoticeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

