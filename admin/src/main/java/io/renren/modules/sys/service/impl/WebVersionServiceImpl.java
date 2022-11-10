package io.renren.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.sys.dao.WebVersionDao;
import io.renren.modules.sys.entity.WebVersionEntity;
import io.renren.modules.sys.service.WebVersionService;


@Service("webVersionService")
public class WebVersionServiceImpl extends ServiceImpl<WebVersionDao, WebVersionEntity> implements WebVersionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebVersionEntity> page = this.page(
                new Query<WebVersionEntity>().getPage(params),
                new QueryWrapper<WebVersionEntity>()
        );

        return new PageUtils(page);
    }

}