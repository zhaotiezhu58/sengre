package io.renren.modules.activity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.activity.entity.WebWeeksalaryRecordEntity;

import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 21:42:14
 */
public interface WebWeeksalaryRecordService extends IService<WebWeeksalaryRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

