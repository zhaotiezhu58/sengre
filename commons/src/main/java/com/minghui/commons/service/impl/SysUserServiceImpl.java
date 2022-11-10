package com.minghui.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minghui.commons.entity.SysUser;
import com.minghui.commons.service.SysUserService;
import com.minghui.commons.dao.SysUserDao;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
* @author Administrator
* @description 针对表【sys_user(系统用户)】的数据库操作Service实现
* @createDate 2022-05-27 17:27:43
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<SysUser> wrapper = new QueryWrapper<SysUser>().lambda();

        IPage<SysUser> page = this.page(
            new Query<SysUser>().getPage(params),
            wrapper);
        return new PageUtils(page);
    }
}




