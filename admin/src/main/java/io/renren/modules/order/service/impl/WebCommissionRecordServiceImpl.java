package io.renren.modules.order.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.renren.modules.order.entity.WebFundRecordEntity;
import io.renren.modules.user.entity.WebUserEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.order.dao.WebCommissionRecordDao;
import io.renren.modules.order.entity.WebCommissionRecordEntity;
import io.renren.modules.order.service.WebCommissionRecordService;


@Service("webCommissionRecordService")
public class WebCommissionRecordServiceImpl extends ServiceImpl<WebCommissionRecordDao, WebCommissionRecordEntity> implements WebCommissionRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebCommissionRecordEntity> lambda = new QueryWrapper<WebCommissionRecordEntity>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        lambda.eq(StringUtils.isNotEmpty(userName), WebCommissionRecordEntity::getUserName, userName);

        String commissionUser = MapUtil.getStr(params, "commissionUser");
        lambda.eq(StringUtils.isNotEmpty(commissionUser), WebCommissionRecordEntity::getCommissionUser, commissionUser);

        String refBillNo = MapUtil.getStr(params, "refBillNo");
        lambda.eq(StringUtils.isNotEmpty(refBillNo), WebCommissionRecordEntity::getRefBillNo, refBillNo);

        Date beginTime = MapUtil.getDate(params, "beginTime");
        lambda.ge(beginTime != null, WebCommissionRecordEntity::getCreateTime, beginTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        lambda.le(endTime != null, WebCommissionRecordEntity::getCreateTime, endTime);

        String userNode = MapUtil.getStr(params, "userNode");
        lambda.like(StringUtils.isNotEmpty(userNode), WebCommissionRecordEntity::getAgentNode, userNode);

        lambda.orderByDesc(WebCommissionRecordEntity::getCreateTime);

        IPage<WebCommissionRecordEntity> page = this.page(
                new Query<WebCommissionRecordEntity>().getPage(params),
                lambda
        );

        return new PageUtils(page);
    }

}