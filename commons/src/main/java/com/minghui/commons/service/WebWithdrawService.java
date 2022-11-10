package com.minghui.commons.service;

import com.minghui.commons.entity.WebWithdraw;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;
import com.minghui.commons.utils.PageUtils;

/**
* @author Administrator
* @description 针对表【t_web_withdraw】的数据库操作Service
* @createDate 2022-05-12 01:30:08
*/
public interface WebWithdrawService extends IService<WebWithdraw> {

    PageUtils queryPage(Map<String,Object> params);

}
