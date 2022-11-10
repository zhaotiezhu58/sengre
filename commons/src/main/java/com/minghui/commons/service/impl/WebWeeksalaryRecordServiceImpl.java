package com.minghui.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minghui.commons.entity.WebWeeksalaryRecord;
import com.minghui.commons.service.WebWeeksalaryRecordService;
import com.minghui.commons.dao.WebWeeksalaryRecordDao;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
* @author Administrator
* @description 针对表【t_web_weeksalary_record】的数据库操作Service实现
* @createDate 2022-05-12 04:04:50
*/
@Service
public class WebWeeksalaryRecordServiceImpl extends ServiceImpl<WebWeeksalaryRecordDao, WebWeeksalaryRecord> implements WebWeeksalaryRecordService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebWeeksalaryRecord> wrapper = new QueryWrapper<WebWeeksalaryRecord>().lambda();

        IPage<WebWeeksalaryRecord> page = this.page(
            new Query<WebWeeksalaryRecord>().getPage(params),
            wrapper);
        return new PageUtils(page);
    }
}




