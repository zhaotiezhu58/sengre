package com.minghui.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minghui.commons.entity.SysConfig;
import com.minghui.commons.service.SysConfigService;
import com.minghui.commons.dao.SysConfigDao;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
* @author Administrator
* @description 针对表【sys_config(系统配置信息表)】的数据库操作Service实现
* @createDate 2022-06-11 07:23:31
*/
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigDao, SysConfig> implements SysConfigService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<SysConfig> wrapper = new QueryWrapper<SysConfig>().lambda();

        IPage<SysConfig> page = this.page(
            new Query<SysConfig>().getPage(params),
            wrapper);
        return new PageUtils(page);
    }
}




