package io.renren.modules.pay.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.pay.dao.WebPayChannelDao;
import io.renren.modules.pay.entity.WebPayChannelEntity;
import io.renren.modules.pay.service.WebPayChannelService;


@Service("webPayChannelService")
public class WebPayChannelServiceImpl extends ServiceImpl<WebPayChannelDao, WebPayChannelEntity> implements WebPayChannelService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebPayChannelEntity> page = this.page(
                new Query<WebPayChannelEntity>().getPage(params),
                new QueryWrapper<WebPayChannelEntity>()
        );

        return new PageUtils(page);
    }

}