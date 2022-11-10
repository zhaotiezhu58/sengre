package com.minghui.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minghui.commons.entity.WebLuckySpinConfig;
import com.minghui.commons.service.WebLuckySpinConfigService;
import com.minghui.commons.dao.WebLuckySpinConfigDao;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
* @author Administrator
* @description 针对表【t_web_lucky_spin_config】的数据库操作Service实现
* @createDate 2022-05-15 22:54:49
*/
@Service
public class WebLuckySpinConfigServiceImpl extends ServiceImpl<WebLuckySpinConfigDao, WebLuckySpinConfig> implements WebLuckySpinConfigService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebLuckySpinConfig> wrapper = new QueryWrapper<WebLuckySpinConfig>().lambda();

        IPage<WebLuckySpinConfig> page = this.page(
            new Query<WebLuckySpinConfig>().getPage(params),
            wrapper);
        return new PageUtils(page);
    }
}




