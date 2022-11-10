package io.renren.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.user.entity.WebDayReportEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 22:29:21
 */
public interface WebDayReportService extends IService<WebDayReportEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void batchInsertOrUpdate(List<WebDayReportEntity> list) throws Exception;

    void insertOrUpdate(WebDayReportEntity report) throws Exception;
}

