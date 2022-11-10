package io.renren.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.user.dao.WebNoticeDao;
import io.renren.modules.user.entity.WebNoticeEntity;
import io.renren.modules.user.service.WebNoticeService;


@Service("webNoticeService")
public class WebNoticeServiceImpl extends ServiceImpl<WebNoticeDao, WebNoticeEntity> implements WebNoticeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebNoticeEntity> lambda = new QueryWrapper<WebNoticeEntity>().lambda();

        lambda.orderByDesc(WebNoticeEntity::getCreateTime);

        IPage<WebNoticeEntity> page = this.page(
                new Query<WebNoticeEntity>().getPage(params),
                lambda
        );

        return new PageUtils(page);
    }

}