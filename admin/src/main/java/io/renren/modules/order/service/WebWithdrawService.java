package io.renren.modules.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.order.entity.WebWithdrawEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 18:21:18
 */
public interface WebWithdrawService extends IService<WebWithdrawEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<WebWithdrawEntity> queryList(Map<String, Object> params);
}

