package io.renren.modules.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.order.entity.WebOrderEntity;

import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 18:10:23
 */
public interface WebOrderService extends IService<WebOrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

