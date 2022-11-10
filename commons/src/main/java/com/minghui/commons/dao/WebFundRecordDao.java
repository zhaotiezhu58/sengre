package com.minghui.commons.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.minghui.commons.entity.WebFundRecordEntity;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

/**
 * 
 * 
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-04 18:44:56
 */
@Mapper
public interface WebFundRecordDao extends BaseMapper<WebFundRecordEntity> {

    BigDecimal getFundsSum(String userName, int type);
}
