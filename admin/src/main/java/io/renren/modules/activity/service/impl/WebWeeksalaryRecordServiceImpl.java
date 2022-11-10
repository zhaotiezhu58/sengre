package io.renren.modules.activity.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.renren.modules.order.entity.WebCommissionRecordEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.activity.dao.WebWeeksalaryRecordDao;
import io.renren.modules.activity.entity.WebWeeksalaryRecordEntity;
import io.renren.modules.activity.service.WebWeeksalaryRecordService;


@Service("webWeeksalaryRecordService")
public class WebWeeksalaryRecordServiceImpl extends ServiceImpl<WebWeeksalaryRecordDao, WebWeeksalaryRecordEntity> implements WebWeeksalaryRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebWeeksalaryRecordEntity> lambda = new QueryWrapper<WebWeeksalaryRecordEntity>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        lambda.eq(StringUtils.isNotEmpty(userName), WebWeeksalaryRecordEntity::getUserName, userName);

        Date beginTime = MapUtil.getDate(params, "beginTime");
        lambda.ge(beginTime != null, WebWeeksalaryRecordEntity::getCreateTime, beginTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        lambda.le(endTime != null, WebWeeksalaryRecordEntity::getCreateTime, endTime);

        lambda.orderByDesc(WebWeeksalaryRecordEntity::getCreateTime);

        IPage<WebWeeksalaryRecordEntity> page = this.page(
                new Query<WebWeeksalaryRecordEntity>().getPage(params),
                lambda
        );

        return new PageUtils(page);
    }

}