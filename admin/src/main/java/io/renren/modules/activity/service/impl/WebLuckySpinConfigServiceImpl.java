package io.renren.modules.activity.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.activity.dao.WebLuckySpinConfigDao;
import io.renren.modules.activity.entity.WebLuckySpinConfigEntity;
import io.renren.modules.activity.service.WebLuckySpinConfigService;


@Service("webLuckySpinConfigService")
public class WebLuckySpinConfigServiceImpl extends ServiceImpl<WebLuckySpinConfigDao, WebLuckySpinConfigEntity> implements WebLuckySpinConfigService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebLuckySpinConfigEntity> page = this.page(
                new Query<WebLuckySpinConfigEntity>().getPage(params),
                new QueryWrapper<WebLuckySpinConfigEntity>()
        );

        return new PageUtils(page);
    }

}