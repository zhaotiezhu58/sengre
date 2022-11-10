package io.renren.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.renren.modules.sys.dao.WebParamsDao;
import io.renren.modules.sys.entity.WebParamsEntity;
import io.renren.modules.sys.service.WebParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;


@Service("webParamsService")
public class WebParamsServiceImpl extends ServiceImpl<WebParamsDao, WebParamsEntity> implements WebParamsService {

    @Autowired
    private WebParamsDao webParamsDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebParamsEntity> lambda = new QueryWrapper<WebParamsEntity>().lambda();
        IPage<WebParamsEntity> page = this.page(
                new Query<WebParamsEntity>().getPage(params),
                lambda
        );

        return new PageUtils(page);
    }

    //@Cacheable(value = {"web:params"}, key = "#key", unless="#result == null")
    @Override
    public String getParamsValue(String key) {
        WebParamsEntity params = webParamsDao.selectOne(
                new QueryWrapper<WebParamsEntity>()
                        .eq("params_key", key)
        );
        return params.getParamsValue();
    }
}