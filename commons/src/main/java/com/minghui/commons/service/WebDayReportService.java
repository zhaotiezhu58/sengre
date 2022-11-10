package com.minghui.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.minghui.commons.entity.WebDayReportEntity;
import com.minghui.commons.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-04 18:44:56
 */
public interface WebDayReportService extends IService<WebDayReportEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void batchInsertOrUpdate(List<WebDayReportEntity> list) throws Exception;

    void insertOrUpdate(WebDayReportEntity report) throws Exception;
}

