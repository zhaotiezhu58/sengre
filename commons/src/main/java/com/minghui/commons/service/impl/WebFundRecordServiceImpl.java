package com.minghui.commons.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.minghui.commons.dao.WebFundRecordDao;
import com.minghui.commons.entity.WebFundRecordEntity;
import com.minghui.commons.service.WebFundRecordService;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("webFundRecordService")
public class WebFundRecordServiceImpl extends ServiceImpl<WebFundRecordDao, WebFundRecordEntity> implements WebFundRecordService {

    @Autowired
    private WebFundRecordDao webFundRecordDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebFundRecordEntity> wrapper = new QueryWrapper<WebFundRecordEntity>().lambda();

        String userName = MapUtil.getStr(params,"userName");
        wrapper.eq(StringUtils.isNotBlank(userName), WebFundRecordEntity::getUserName, userName);

        wrapper.orderByDesc(WebFundRecordEntity::getCreateTime);

        IPage<WebFundRecordEntity> page = this.page(
                new Query<WebFundRecordEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public BigDecimal getFundsSum(String userName, int type) {
        return null;
    }

}