package com.minghui.commons.service;

import com.minghui.commons.entity.SysConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;
import com.minghui.commons.utils.PageUtils;

/**
* @author Administrator
* @description 针对表【sys_config(系统配置信息表)】的数据库操作Service
* @createDate 2022-06-11 07:23:31
*/
public interface SysConfigService extends IService<SysConfig> {

    PageUtils queryPage(Map<String,Object> params);

}
