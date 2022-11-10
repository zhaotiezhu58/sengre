package io.renren.modules.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.pay.entity.WebPayChannelEntity;

import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 17:55:19
 */
public interface WebPayChannelService extends IService<WebPayChannelEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

