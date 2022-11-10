package com.minghui.commons.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.minghui.commons.dao.WebOrderDao;
import com.minghui.commons.entity.WebOrderEntity;
import com.minghui.commons.entity.WebVirtualRecordEntity;
import com.minghui.commons.service.WebOrderService;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("webOrderService")
public class WebOrderServiceImpl extends ServiceImpl<WebOrderDao, WebOrderEntity> implements WebOrderService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        LambdaQueryWrapper<WebOrderEntity> wrapper = new QueryWrapper<WebOrderEntity>().lambda();

        String userName = MapUtil.getStr(params,"userName");
        wrapper.eq(StringUtils.isNotBlank(userName), WebOrderEntity::getUserName, userName);

        wrapper.orderByDesc(WebOrderEntity::getCreateTime);
        IPage<WebOrderEntity> page = this.page(
                new Query<WebOrderEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}