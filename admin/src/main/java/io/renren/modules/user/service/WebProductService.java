package io.renren.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.user.entity.WebProductEntity;

import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 16:54:59
 */
public interface WebProductService extends IService<WebProductEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

