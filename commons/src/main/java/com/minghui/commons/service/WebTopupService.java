package com.minghui.commons.service;

import com.minghui.commons.entity.WebTopup;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;
import com.minghui.commons.utils.PageUtils;

/**
* @author Administrator
* @description 针对表【t_web_topup】的数据库操作Service
* @createDate 2022-05-11 16:50:58
*/
public interface WebTopupService extends IService<WebTopup> {

    PageUtils queryPage(Map<String,Object> params);

}
