package io.renren.modules.order.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.order.dao.WebOrderDao;
import io.renren.modules.order.entity.WebOrderEntity;
import io.renren.modules.order.service.WebOrderService;


@Service("webOrderService")
public class WebOrderServiceImpl extends ServiceImpl<WebOrderDao, WebOrderEntity> implements WebOrderService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebOrderEntity> lambda = new QueryWrapper<WebOrderEntity>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        lambda.eq(StringUtils.isNotEmpty(userName), WebOrderEntity::getUserName, userName);

        String orderNo = MapUtil.getStr(params, "orderNo");
        lambda.eq(StringUtils.isNotEmpty(orderNo), WebOrderEntity::getOrderNo, orderNo);

        Date beginTime = MapUtil.getDate(params, "beginTime");
        lambda.ge(beginTime != null, WebOrderEntity::getCreateTime, beginTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        lambda.le(endTime != null, WebOrderEntity::getCreateTime, endTime);

        String userNode = MapUtil.getStr(params, "userNode");
        lambda.like(StringUtils.isNotEmpty(userNode), WebOrderEntity::getAgentNode, userNode);

        lambda.orderByDesc(WebOrderEntity::getCreateTime);

        IPage<WebOrderEntity> page = this.page(
                new Query<WebOrderEntity>().getPage(params),
                lambda
        );

        return new PageUtils(page);
    }

}