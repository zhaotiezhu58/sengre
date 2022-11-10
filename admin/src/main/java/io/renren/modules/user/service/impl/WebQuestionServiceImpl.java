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

import io.renren.modules.user.dao.WebQuestionDao;
import io.renren.modules.user.entity.WebQuestionEntity;
import io.renren.modules.user.service.WebQuestionService;


@Service("webQuestionService")
public class WebQuestionServiceImpl extends ServiceImpl<WebQuestionDao, WebQuestionEntity> implements WebQuestionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebQuestionEntity> lambda = new QueryWrapper<WebQuestionEntity>().lambda();
        String userName = MapUtil.getStr(params, "userName");
        lambda.eq(StringUtils.isNotEmpty(userName), WebQuestionEntity::getUserName, userName);

        lambda.orderByDesc(WebQuestionEntity::getCreateTime);

        IPage<WebQuestionEntity> page = this.page(
                new Query<WebQuestionEntity>().getPage(params),
                lambda
        );

        return new PageUtils(page);
    }

}