package com.minghui.commons.service;

import com.minghui.commons.entity.WebWeeksalaryRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;
import com.minghui.commons.utils.PageUtils;

/**
* @author Administrator
* @description 针对表【t_web_weeksalary_record】的数据库操作Service
* @createDate 2022-05-12 04:04:50
*/
public interface WebWeeksalaryRecordService extends IService<WebWeeksalaryRecord> {

    PageUtils queryPage(Map<String,Object> params);

}
