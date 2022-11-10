package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.WebDictEntity;

import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-06-03 00:33:06
 */
public interface WebDictService extends IService<WebDictEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

