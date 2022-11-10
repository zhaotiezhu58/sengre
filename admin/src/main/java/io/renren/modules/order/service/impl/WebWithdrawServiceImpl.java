package io.renren.modules.order.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.renren.modules.order.entity.WebOrderEntity;
import io.renren.modules.order.entity.WebTopupEntity;
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

import io.renren.modules.order.dao.WebWithdrawDao;
import io.renren.modules.order.entity.WebWithdrawEntity;
import io.renren.modules.order.service.WebWithdrawService;


@Service("webWithdrawService")
public class WebWithdrawServiceImpl extends ServiceImpl<WebWithdrawDao, WebWithdrawEntity> implements WebWithdrawService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebWithdrawEntity> lambda = new QueryWrapper<WebWithdrawEntity>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        lambda.eq(StringUtils.isNotEmpty(userName), WebWithdrawEntity::getUserName, userName);

        String orderNo = MapUtil.getStr(params, "orderNo");
        lambda.eq(StringUtils.isNotEmpty(orderNo), WebWithdrawEntity::getOrderNo, orderNo);

        String payOrderNo = MapUtil.getStr(params, "payOrderNo");
        lambda.eq(StringUtils.isNotEmpty(payOrderNo), WebWithdrawEntity::getPayOrderNo, payOrderNo);

        Date beginTime = MapUtil.getDate(params, "beginTime");
        lambda.ge(beginTime != null, WebWithdrawEntity::getCreateTime, beginTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        lambda.le(endTime != null, WebWithdrawEntity::getCreateTime, endTime);

        String status = MapUtil.getStr(params, "status");
        lambda.eq(StringUtils.isNotEmpty(status), WebWithdrawEntity::getStatus, status);

        String type = MapUtil.getStr(params, "type");
        lambda.eq(StringUtils.isNotEmpty(type), WebWithdrawEntity::getType, type);

        String userNode = MapUtil.getStr(params, "userNode");
        lambda.like(StringUtils.isNotEmpty(userNode), WebWithdrawEntity::getAgentNode, userNode);

        lambda.orderByDesc(WebWithdrawEntity::getModifyTime);

        IPage<WebWithdrawEntity> page = this.page(
                new Query<WebWithdrawEntity>().getPage(params),
                lambda
        );

        return new PageUtils(page);
    }

    @Override
    public List<WebWithdrawEntity> queryList(Map<String, Object> params) {
        LambdaQueryWrapper<WebWithdrawEntity> lambda = new QueryWrapper<WebWithdrawEntity>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        lambda.eq(StringUtils.isNotEmpty(userName), WebWithdrawEntity::getUserName, userName);

        String orderNo = MapUtil.getStr(params, "orderNo");
        lambda.eq(StringUtils.isNotEmpty(orderNo), WebWithdrawEntity::getOrderNo, orderNo);

        String payOrderNo = MapUtil.getStr(params, "payOrderNo");
        lambda.eq(StringUtils.isNotEmpty(payOrderNo), WebWithdrawEntity::getPayOrderNo, payOrderNo);

        Date beginTime = MapUtil.getDate(params, "beginTime");
        lambda.ge(beginTime != null, WebWithdrawEntity::getCreateTime, beginTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        lambda.le(endTime != null, WebWithdrawEntity::getCreateTime, endTime);

        String status = MapUtil.getStr(params, "status");
        lambda.eq(StringUtils.isNotEmpty(status), WebWithdrawEntity::getStatus, status);

        String userNode = MapUtil.getStr(params, "userNode");
        lambda.like(StringUtils.isNotEmpty(userNode), WebWithdrawEntity::getAgentNode, userNode);

        String type = MapUtil.getStr(params, "type");
        lambda.eq(StringUtils.isNotEmpty(type), WebWithdrawEntity::getType, type);

        return this.list(lambda);
    }

}