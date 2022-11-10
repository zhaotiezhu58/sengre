package com.minghui.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minghui.commons.entity.WebPayChannel;
import com.minghui.commons.service.WebPayChannelService;
import com.minghui.commons.dao.WebPayChannelDao;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
* @author Administrator
* @description 针对表【t_web_pay_channel】的数据库操作Service实现
* @createDate 2022-05-11 16:50:58
*/
@Service
public class WebPayChannelServiceImpl extends ServiceImpl<WebPayChannelDao, WebPayChannel> implements WebPayChannelService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebPayChannel> wrapper = new QueryWrapper<WebPayChannel>().lambda();

        IPage<WebPayChannel> page = this.page(
            new Query<WebPayChannel>().getPage(params),
            wrapper);
        return new PageUtils(page);
    }
}




