package io.renren.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.sys.dao.WebDictDao;
import io.renren.modules.sys.entity.WebDictEntity;
import io.renren.modules.sys.service.WebDictService;


@Service("webDictService")
public class WebDictServiceImpl extends ServiceImpl<WebDictDao, WebDictEntity> implements WebDictService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebDictEntity> page = this.page(
                new Query<WebDictEntity>().getPage(params),
                new QueryWrapper<WebDictEntity>()
        );

        return new PageUtils(page);
    }

}