package com.minghui.commons.service.impl;

import com.minghui.commons.dao.WebNoticeDao;
import com.minghui.commons.entity.WebNoticeEntity;
import com.minghui.commons.service.WebNoticeService;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("webNoticeService")
public class WebNoticeServiceImpl extends ServiceImpl<WebNoticeDao, WebNoticeEntity> implements WebNoticeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebNoticeEntity> page = this.page(
                new Query<WebNoticeEntity>().getPage(params),
                new QueryWrapper<WebNoticeEntity>()
        );

        return new PageUtils(page);
    }

}