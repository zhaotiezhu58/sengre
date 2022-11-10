package com.minghui.commons.service;

import com.minghui.commons.entity.WebLuckySpin;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;
import com.minghui.commons.utils.PageUtils;

/**
* @author Administrator
* @description 针对表【t_web_lucky_spin】的数据库操作Service
* @createDate 2022-05-15 22:45:44
*/
public interface WebLuckySpinService extends IService<WebLuckySpin> {

    PageUtils queryPage(Map<String,Object> params);

}
