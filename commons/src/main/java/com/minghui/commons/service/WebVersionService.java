package com.minghui.commons.service;

import com.minghui.commons.entity.WebVersion;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;
import com.minghui.commons.utils.PageUtils;

/**
* @author Administrator
* @description 针对表【t_web_version】的数据库操作Service
* @createDate 2022-05-12 18:21:58
*/
public interface WebVersionService extends IService<WebVersion> {

    PageUtils queryPage(Map<String,Object> params);

}
