package com.minghui.commons.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minghui.commons.entity.WebTopup;
import com.minghui.commons.entity.WebWithdraw;
import com.minghui.commons.service.WebWithdrawService;
import com.minghui.commons.dao.WebWithdrawDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
* @author Administrator
* @description 针对表【t_web_withdraw】的数据库操作Service实现
* @createDate 2022-05-12 01:30:08
*/
@Service
public class WebWithdrawServiceImpl extends ServiceImpl<WebWithdrawDao, WebWithdraw> implements WebWithdrawService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebWithdraw> wrapper = new QueryWrapper<WebWithdraw>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        wrapper.eq(StringUtils.isNotBlank(userName), WebWithdraw::getUserName, userName);

        String type = MapUtil.getStr(params, "type");
        wrapper.eq(StringUtils.isNotBlank(type), WebWithdraw::getType, type);

        wrapper.orderByDesc(WebWithdraw::getCreateTime);

        IPage<WebWithdraw> page = this.page(
            new Query<WebWithdraw>().getPage(params),
            wrapper);
        return new PageUtils(page);
    }
}




