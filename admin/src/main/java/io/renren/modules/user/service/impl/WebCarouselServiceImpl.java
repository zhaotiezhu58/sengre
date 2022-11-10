package io.renren.modules.user.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.user.dao.WebCarouselDao;
import io.renren.modules.user.entity.WebCarouselEntity;
import io.renren.modules.user.service.WebCarouselService;


@Service("webCarouselService")
public class WebCarouselServiceImpl extends ServiceImpl<WebCarouselDao, WebCarouselEntity> implements WebCarouselService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebCarouselEntity> page = this.page(
                new Query<WebCarouselEntity>().getPage(params),
                new QueryWrapper<WebCarouselEntity>()
        );

        return new PageUtils(page);
    }

}