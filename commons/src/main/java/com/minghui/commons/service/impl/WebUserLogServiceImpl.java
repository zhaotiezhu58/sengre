package com.minghui.commons.service.impl;

import com.minghui.commons.dao.WebUserLogDao;
import com.minghui.commons.entity.WebUserLogEntity;
import com.minghui.commons.service.WebUserLogService;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("webUserLogService")
public class WebUserLogServiceImpl extends ServiceImpl<WebUserLogDao, WebUserLogEntity> implements WebUserLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebUserLogEntity> page = this.page(
                new Query<WebUserLogEntity>().getPage(params),
                new QueryWrapper<WebUserLogEntity>()
        );

        return new PageUtils(page);
    }

}