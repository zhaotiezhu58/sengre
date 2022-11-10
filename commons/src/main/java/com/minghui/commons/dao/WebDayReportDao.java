package com.minghui.commons.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.minghui.commons.entity.WebDayReportEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

/**
 * 
 * 
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-04 18:44:56
 */
@Mapper
public interface WebDayReportDao extends BaseMapper<WebDayReportEntity> {

    int batchInsertOrUpdate(List<WebDayReportEntity> list);

    int insertOrUpdate(@Param("report") WebDayReportEntity report);

}
