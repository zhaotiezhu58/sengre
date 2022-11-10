package com.minghui.commons.service.impl;

import com.minghui.commons.dao.WebQuestionDao;
import com.minghui.commons.entity.WebQuestionEntity;
import com.minghui.commons.service.WebQuestionService;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("webQuestionService")
public class WebQuestionServiceImpl extends ServiceImpl<WebQuestionDao, WebQuestionEntity> implements WebQuestionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebQuestionEntity> page = this.page(
                new Query<WebQuestionEntity>().getPage(params),
                new QueryWrapper<WebQuestionEntity>()
        );

        return new PageUtils(page);
    }

}