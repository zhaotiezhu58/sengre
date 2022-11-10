package com.minghui.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.minghui.commons.entity.WebUserLogEntity;
import com.minghui.commons.utils.PageUtils;

import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-02-26 21:01:52
 */
public interface WebUserLogService extends IService<WebUserLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

