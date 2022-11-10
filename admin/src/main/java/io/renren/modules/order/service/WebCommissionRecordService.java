package io.renren.modules.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.order.entity.WebCommissionRecordEntity;

import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 21:26:55
 */
public interface WebCommissionRecordService extends IService<WebCommissionRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

