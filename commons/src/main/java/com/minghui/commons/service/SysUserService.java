package com.minghui.commons.service;

import com.minghui.commons.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;
import com.minghui.commons.utils.PageUtils;

/**
* @author Administrator
* @description 针对表【sys_user(系统用户)】的数据库操作Service
* @createDate 2022-05-27 17:27:43
*/
public interface SysUserService extends IService<SysUser> {

    PageUtils queryPage(Map<String,Object> params);

}
