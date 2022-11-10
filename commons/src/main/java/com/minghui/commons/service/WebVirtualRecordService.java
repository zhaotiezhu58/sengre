package com.minghui.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.minghui.commons.entity.WebVirtualRecordEntity;
import com.minghui.commons.utils.PageUtils;

import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-05 00:52:08
 */
public interface WebVirtualRecordService extends IService<WebVirtualRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

