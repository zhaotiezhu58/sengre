package io.renren.modules.order.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

import io.renren.modules.order.dao.WebFundRecordDao;
import io.renren.modules.order.entity.WebFundRecordEntity;
import io.renren.modules.order.service.WebFundRecordService;


@Service("webFundRecordService")
public class WebFundRecordServiceImpl extends ServiceImpl<WebFundRecordDao, WebFundRecordEntity> implements WebFundRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebFundRecordEntity> lambda = new QueryWrapper<WebFundRecordEntity>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        lambda.eq(StringUtils.isNotEmpty(userName), WebFundRecordEntity::getUserName, userName);

        String serialNo = MapUtil.getStr(params, "serialNo");
        lambda.eq(StringUtils.isNotEmpty(serialNo), WebFundRecordEntity::getSerialNo, serialNo);

        String refBillNo = MapUtil.getStr(params, "refBillNo");
        lambda.eq(StringUtils.isNotEmpty(refBillNo), WebFundRecordEntity::getRefBillNo, refBillNo);

        String type = MapUtil.getStr(params, "type");
        lambda.eq(StringUtils.isNotEmpty(type), WebFundRecordEntity::getType, type);

        Date beginTime = MapUtil.getDate(params, "beginTime");
        lambda.ge(beginTime != null, WebFundRecordEntity::getCreateTime, beginTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        lambda.le(endTime != null, WebFundRecordEntity::getCreateTime, endTime);

        String userNode = MapUtil.getStr(params, "userNode");
        lambda.like(StringUtils.isNotEmpty(userNode), WebFundRecordEntity::getAgentNode, userNode);

        lambda.orderByDesc(WebFundRecordEntity::getCreateTime);

        IPage<WebFundRecordEntity> page = this.page(
                new Query<WebFundRecordEntity>().getPage(params),
                lambda
        );

        return new PageUtils(page);
    }

    @Override
    public List<WebFundRecordEntity> queryList(Map<String, Object> params) {
        LambdaQueryWrapper<WebFundRecordEntity> lambda = new QueryWrapper<WebFundRecordEntity>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        lambda.eq(StringUtils.isNotEmpty(userName), WebFundRecordEntity::getUserName, userName);

        String serialNo = MapUtil.getStr(params, "serialNo");
        lambda.eq(StringUtils.isNotEmpty(serialNo), WebFundRecordEntity::getSerialNo, serialNo);

        String refBillNo = MapUtil.getStr(params, "refBillNo");
        lambda.eq(StringUtils.isNotEmpty(refBillNo), WebFundRecordEntity::getRefBillNo, refBillNo);

        String type = MapUtil.getStr(params, "type");
        lambda.eq(StringUtils.isNotEmpty(type), WebFundRecordEntity::getType, type);

        Date beginTime = MapUtil.getDate(params, "beginTime");
        lambda.ge(beginTime != null, WebFundRecordEntity::getCreateTime, beginTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        lambda.le(endTime != null, WebFundRecordEntity::getCreateTime, endTime);

        String userNode = MapUtil.getStr(params, "userNode");
        lambda.like(StringUtils.isNotEmpty(userNode), WebFundRecordEntity::getAgentNode, userNode);

        lambda.orderByDesc(WebFundRecordEntity::getCreateTime);
        return this.list(lambda);
    }

}