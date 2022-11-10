package io.renren.modules.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.order.entity.WebFundRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 20:50:42
 */
public interface WebFundRecordService extends IService<WebFundRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<WebFundRecordEntity> queryList(Map<String, Object> params);
}

