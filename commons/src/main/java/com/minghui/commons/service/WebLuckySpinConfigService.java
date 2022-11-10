package com.minghui.commons.service;

import com.minghui.commons.entity.WebLuckySpinConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;
import com.minghui.commons.utils.PageUtils;

/**
* @author Administrator
* @description 针对表【t_web_lucky_spin_config】的数据库操作Service
* @createDate 2022-05-15 22:54:49
*/
public interface WebLuckySpinConfigService extends IService<WebLuckySpinConfig> {

    PageUtils queryPage(Map<String,Object> params);

}
