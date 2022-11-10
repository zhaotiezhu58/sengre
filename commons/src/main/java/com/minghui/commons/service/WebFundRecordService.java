package com.minghui.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.minghui.commons.entity.WebFundRecordEntity;
import com.minghui.commons.utils.PageUtils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-04 18:44:56
 */
public interface WebFundRecordService extends IService<WebFundRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);

    BigDecimal getFundsSum(String userName, int type);
}

