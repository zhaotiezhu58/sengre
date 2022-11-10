package io.renren.modules.user.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.user.dao.WebUserLogDao;
import io.renren.modules.user.entity.WebUserLogEntity;
import io.renren.modules.user.service.WebUserLogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Service("webUserLogService")
public class WebUserLogServiceImpl extends ServiceImpl<WebUserLogDao, WebUserLogEntity> implements WebUserLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebUserLogEntity> lambda = new QueryWrapper<WebUserLogEntity>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        lambda.like(StringUtils.isNotEmpty(userName), WebUserLogEntity::getUserPhone, userName);

        Date beginTime = MapUtil.getDate(params, "beginTime");
        lambda.ge(beginTime != null, WebUserLogEntity::getCreateTime, beginTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        lambda.le(endTime != null, WebUserLogEntity::getCreateTime, endTime);

        lambda.orderByDesc(WebUserLogEntity::getCreateTime);
        IPage<WebUserLogEntity> page = this.page(
                new Query<WebUserLogEntity>().getPage(params),
                lambda
        );

        return new PageUtils(page);
    }

}