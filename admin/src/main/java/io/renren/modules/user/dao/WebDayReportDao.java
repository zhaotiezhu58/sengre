package io.renren.modules.user.dao;

import io.renren.modules.user.entity.WebDayReportEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 22:29:21
 */
@Mapper
public interface WebDayReportDao extends BaseMapper<WebDayReportEntity> {

    int batchInsertOrUpdate(List<WebDayReportEntity> list);

    int insertOrUpdate(@Param("report") WebDayReportEntity report);
}
