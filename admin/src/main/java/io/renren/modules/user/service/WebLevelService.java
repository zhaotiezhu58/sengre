package io.renren.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.user.entity.WebLevelEntity;

import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 16:40:10
 */
public interface WebLevelService extends IService<WebLevelEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

