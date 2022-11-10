package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.WebParamsEntity;

import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-03-03 18:43:48
 */
public interface WebParamsService extends IService<WebParamsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    String getParamsValue(String key);
}

