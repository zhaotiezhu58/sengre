package io.renren.modules.order.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.renren.modules.order.entity.WebWithdrawEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.order.dao.WebTopupDao;
import io.renren.modules.order.entity.WebTopupEntity;
import io.renren.modules.order.service.WebTopupService;


@Service("webTopupService")
public class WebTopupServiceImpl extends ServiceImpl<WebTopupDao, WebTopupEntity> implements WebTopupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebTopupEntity> lambda = new QueryWrapper<WebTopupEntity>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        lambda.eq(StringUtils.isNotEmpty(userName), WebTopupEntity::getUserName, userName);

        String orderNo = MapUtil.getStr(params, "orderNo");
        lambda.eq(StringUtils.isNotEmpty(orderNo), WebTopupEntity::getOrderNo, orderNo);

        String payOrderNo = MapUtil.getStr(params, "payOrderNo");
        lambda.eq(StringUtils.isNotEmpty(payOrderNo), WebTopupEntity::getPayOrderNo, payOrderNo);

        String status = MapUtil.getStr(params, "status");
        lambda.eq(StringUtils.isNotEmpty(status), WebTopupEntity::getStatus, status);

        Date beginTime = MapUtil.getDate(params, "beginTime");
        lambda.ge(beginTime != null, WebTopupEntity::getCreateTime, beginTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        lambda.le(endTime != null, WebTopupEntity::getCreateTime, endTime);

        String userNode = MapUtil.getStr(params, "userNode");
        lambda.like(StringUtils.isNotEmpty(userNode), WebTopupEntity::getAgentNode, userNode);

        String type = MapUtil.getStr(params, "type");
        lambda.eq(StringUtils.isNotEmpty(type), WebTopupEntity::getType, type);

        lambda.orderByDesc(WebTopupEntity::getCreateTime);

        IPage<WebTopupEntity> page = this.page(
                new Query<WebTopupEntity>().getPage(params),
                lambda
        );

        return new PageUtils(page);
    }

    @Override
    public List<WebTopupEntity> queryList(Map<String, Object> params) {
        LambdaQueryWrapper<WebTopupEntity> lambda = new QueryWrapper<WebTopupEntity>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        lambda.eq(StringUtils.isNotEmpty(userName), WebTopupEntity::getUserName, userName);

        String orderNo = MapUtil.getStr(params, "orderNo");
        lambda.eq(StringUtils.isNotEmpty(orderNo), WebTopupEntity::getOrderNo, orderNo);

        String payOrderNo = MapUtil.getStr(params, "payOrderNo");
        lambda.eq(StringUtils.isNotEmpty(payOrderNo), WebTopupEntity::getPayOrderNo, payOrderNo);

        String status = MapUtil.getStr(params, "status");
        lambda.eq(StringUtils.isNotEmpty(status), WebTopupEntity::getStatus, status);

        Date beginTime = MapUtil.getDate(params, "beginTime");
        lambda.ge(beginTime != null, WebTopupEntity::getCreateTime, beginTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        lambda.le(endTime != null, WebTopupEntity::getCreateTime, endTime);

        String userNode = MapUtil.getStr(params, "userNode");
        lambda.like(StringUtils.isNotEmpty(userNode), WebTopupEntity::getAgentNode, userNode);

        String type = MapUtil.getStr(params, "type");
        lambda.eq(StringUtils.isNotEmpty(type), WebTopupEntity::getType, type);

        return this.list(lambda);
    }

}