package io.renren.modules.user.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.user.dao.WebLevelDao;
import io.renren.modules.user.entity.WebLevelEntity;
import io.renren.modules.user.service.WebLevelService;


@Service("webLevelService")
public class WebLevelServiceImpl extends ServiceImpl<WebLevelDao, WebLevelEntity> implements WebLevelService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebLevelEntity> lambda = new QueryWrapper<WebLevelEntity>().lambda();

        String levelValue = MapUtil.getStr(params, "levelValue");
        lambda.eq(StringUtils.isNotEmpty(levelValue), WebLevelEntity::getLevelValue, levelValue);

        IPage<WebLevelEntity> page = this.page(
                new Query<WebLevelEntity>().getPage(params),
                lambda
        );

        return new PageUtils(page);
    }

}