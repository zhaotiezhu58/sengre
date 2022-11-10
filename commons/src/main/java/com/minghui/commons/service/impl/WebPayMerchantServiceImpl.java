package com.minghui.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minghui.commons.entity.WebPayMerchant;
import com.minghui.commons.service.WebPayMerchantService;
import com.minghui.commons.dao.WebPayMerchantDao;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
* @author Administrator
* @description 针对表【t_web_pay_merchant】的数据库操作Service实现
* @createDate 2022-05-11 16:50:58
*/
@Service
public class WebPayMerchantServiceImpl extends ServiceImpl<WebPayMerchantDao, WebPayMerchant> implements WebPayMerchantService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebPayMerchant> wrapper = new QueryWrapper<WebPayMerchant>().lambda();

        IPage<WebPayMerchant> page = this.page(
            new Query<WebPayMerchant>().getPage(params),
            wrapper);
        return new PageUtils(page);
    }
}




