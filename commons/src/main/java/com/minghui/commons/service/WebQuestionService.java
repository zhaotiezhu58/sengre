package com.minghui.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.minghui.commons.entity.WebQuestionEntity;
import com.minghui.commons.utils.PageUtils;

import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-07 22:07:54
 */
public interface WebQuestionService extends IService<WebQuestionEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

