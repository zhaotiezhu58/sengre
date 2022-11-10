package io.renren.modules.pay.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.pay.dao.WebPayMerchantDao;
import io.renren.modules.pay.entity.WebPayMerchantEntity;
import io.renren.modules.pay.service.WebPayMerchantService;


@Service("webPayMerchantService")
public class WebPayMerchantServiceImpl extends ServiceImpl<WebPayMerchantDao, WebPayMerchantEntity> implements WebPayMerchantService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebPayMerchantEntity> page = this.page(
                new Query<WebPayMerchantEntity>().getPage(params),
                new QueryWrapper<WebPayMerchantEntity>()
        );

        return new PageUtils(page);
    }

}