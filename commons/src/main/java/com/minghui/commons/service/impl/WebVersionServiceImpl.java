package com.minghui.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minghui.commons.entity.WebVersion;
import com.minghui.commons.service.WebVersionService;
import com.minghui.commons.dao.WebVersionDao;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
* @author Administrator
* @description 针对表【t_web_version】的数据库操作Service实现
* @createDate 2022-05-12 18:21:58
*/
@Service
public class WebVersionServiceImpl extends ServiceImpl<WebVersionDao, WebVersion> implements WebVersionService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebVersion> wrapper = new QueryWrapper<WebVersion>().lambda();

        IPage<WebVersion> page = this.page(
            new Query<WebVersion>().getPage(params),
            wrapper);
        return new PageUtils(page);
    }
}




