package com.minghui.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minghui.commons.entity.WebLuckySpin;
import com.minghui.commons.service.WebLuckySpinService;
import com.minghui.commons.dao.WebLuckySpinDao;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
* @author Administrator
* @description 针对表【t_web_lucky_spin】的数据库操作Service实现
* @createDate 2022-05-15 22:45:44
*/
@Service
public class WebLuckySpinServiceImpl extends ServiceImpl<WebLuckySpinDao, WebLuckySpin> implements WebLuckySpinService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebLuckySpin> wrapper = new QueryWrapper<WebLuckySpin>().lambda();

        IPage<WebLuckySpin> page = this.page(
            new Query<WebLuckySpin>().getPage(params),
            wrapper);
        return new PageUtils(page);
    }
}




