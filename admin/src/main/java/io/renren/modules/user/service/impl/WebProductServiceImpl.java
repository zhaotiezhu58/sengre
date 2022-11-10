package io.renren.modules.user.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.renren.modules.user.entity.WebLevelEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.user.dao.WebProductDao;
import io.renren.modules.user.entity.WebProductEntity;
import io.renren.modules.user.service.WebProductService;


@Service("webProductService")
public class WebProductServiceImpl extends ServiceImpl<WebProductDao, WebProductEntity> implements WebProductService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebProductEntity> lambda = new QueryWrapper<WebProductEntity>().lambda();

        String levelValue = MapUtil.getStr(params, "levelValue");
        lambda.eq(StringUtils.isNotEmpty(levelValue), WebProductEntity::getLevelValue, levelValue);

        lambda.orderByDesc(WebProductEntity::getId);

        IPage<WebProductEntity> page = this.page(
                new Query<WebProductEntity>().getPage(params),
                lambda
        );

        return new PageUtils(page);
    }

}