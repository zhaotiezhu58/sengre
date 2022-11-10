package io.renren.modules.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.order.entity.WebVirtualRecordEntity;

import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 20:50:42
 */
public interface WebVirtualRecordService extends IService<WebVirtualRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

