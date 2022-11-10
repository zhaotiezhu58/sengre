package com.minghui.commons.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minghui.commons.dao.WebVirtualRecordDao;
import com.minghui.commons.entity.WebVirtualRecordEntity;
import com.minghui.commons.service.WebVirtualRecordService;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("webVirtualRecordService")
public class WebVirtualRecordServiceImpl extends ServiceImpl<WebVirtualRecordDao, WebVirtualRecordEntity> implements WebVirtualRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        LambdaQueryWrapper<WebVirtualRecordEntity> wrapper = new QueryWrapper<WebVirtualRecordEntity>().lambda();

        String userName = MapUtil.getStr(params,"userName");
        wrapper.eq(StringUtils.isNotBlank(userName), WebVirtualRecordEntity::getUserName, userName);

        wrapper.orderByDesc(WebVirtualRecordEntity::getCreateTime);

        IPage<WebVirtualRecordEntity> page = this.page(
                new Query<WebVirtualRecordEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}