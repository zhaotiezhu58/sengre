package io.renren.modules.activity.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.renren.modules.activity.entity.WebWeeksalaryRecordEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.activity.dao.WebLuckySpinDao;
import io.renren.modules.activity.entity.WebLuckySpinEntity;
import io.renren.modules.activity.service.WebLuckySpinService;


@Service("webLuckySpinService")
public class WebLuckySpinServiceImpl extends ServiceImpl<WebLuckySpinDao, WebLuckySpinEntity> implements WebLuckySpinService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebLuckySpinEntity> lambda = new QueryWrapper<WebLuckySpinEntity>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        lambda.eq(StringUtils.isNotEmpty(userName), WebLuckySpinEntity::getUserName, userName);

        Date beginTime = MapUtil.getDate(params, "beginTime");
        lambda.ge(beginTime != null, WebLuckySpinEntity::getCreateTime, beginTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        lambda.le(endTime != null, WebLuckySpinEntity::getCreateTime, endTime);

        lambda.orderByDesc(WebLuckySpinEntity::getCreateTime);

        IPage<WebLuckySpinEntity> page = this.page(
                new Query<WebLuckySpinEntity>().getPage(params),
                lambda
        );

        return new PageUtils(page);
    }

}