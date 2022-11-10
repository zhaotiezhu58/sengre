package com.minghui.commons.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minghui.commons.entity.WebTopup;
import com.minghui.commons.service.WebTopupService;
import com.minghui.commons.dao.WebTopupDao;
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
* @description 针对表【t_web_topup】的数据库操作Service实现
* @createDate 2022-05-11 16:50:58
*/
@Service
public class WebTopupServiceImpl extends ServiceImpl<WebTopupDao, WebTopup> implements WebTopupService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebTopup> wrapper = new QueryWrapper<WebTopup>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        wrapper.eq(StringUtils.isNotBlank(userName), WebTopup::getUserName, userName);

        String type = MapUtil.getStr(params, "type");
        wrapper.eq(StringUtils.isNotBlank(type), WebTopup::getType, type);

        wrapper.orderByDesc(WebTopup::getCreateTime);

        IPage<WebTopup> page = this.page(
            new Query<WebTopup>().getPage(params),
            wrapper);
        return new PageUtils(page);
    }
}




