package io.renren.modules.activity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.activity.entity.WebLuckySpinConfigEntity;

import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 21:49:29
 */
public interface WebLuckySpinConfigService extends IService<WebLuckySpinConfigEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

