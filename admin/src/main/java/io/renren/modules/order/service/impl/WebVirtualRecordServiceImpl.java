package io.renren.modules.order.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.order.dao.WebVirtualRecordDao;
import io.renren.modules.order.entity.WebVirtualRecordEntity;
import io.renren.modules.order.service.WebVirtualRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Service("webVirtualRecordService")
public class WebVirtualRecordServiceImpl extends ServiceImpl<WebVirtualRecordDao, WebVirtualRecordEntity> implements WebVirtualRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebVirtualRecordEntity> lambda = new QueryWrapper<WebVirtualRecordEntity>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        lambda.eq(StringUtils.isNotEmpty(userName), WebVirtualRecordEntity::getUserName, userName);

        String serialNo = MapUtil.getStr(params, "serialNo");
        lambda.eq(StringUtils.isNotEmpty(serialNo), WebVirtualRecordEntity::getSerialNo, serialNo);

        String refBillNo = MapUtil.getStr(params, "refBillNo");
        lambda.eq(StringUtils.isNotEmpty(refBillNo), WebVirtualRecordEntity::getRefBillNo, refBillNo);

        String type = MapUtil.getStr(params, "type");
        lambda.eq(StringUtils.isNotEmpty(type), WebVirtualRecordEntity::getType, type);

        Date beginTime = MapUtil.getDate(params, "beginTime");
        lambda.ge(beginTime != null, WebVirtualRecordEntity::getCreateTime, beginTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        lambda.le(endTime != null, WebVirtualRecordEntity::getCreateTime, endTime);

        String userNode = MapUtil.getStr(params, "userNode");
        lambda.like(StringUtils.isNotEmpty(userNode), WebVirtualRecordEntity::getAgentNode, userNode);

        lambda.orderByDesc(WebVirtualRecordEntity::getCreateTime);

        IPage<WebVirtualRecordEntity> page = this.page(
                new Query<WebVirtualRecordEntity>().getPage(params),
                lambda
        );

        return new PageUtils(page);
    }

}