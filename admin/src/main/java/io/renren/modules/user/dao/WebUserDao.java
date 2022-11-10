package io.renren.modules.user.dao;

import io.renren.modules.user.entity.WebUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * 
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 16:07:36
 */
@Mapper
public interface WebUserDao extends BaseMapper<WebUserEntity> {

    /**
     * 修改用户余额
     * @param userName
     * @param balance
     * @return
     */
    int updateUserBalance(@Param("userName") String userName, @Param("balance") BigDecimal balance);

    /**
     * 批量修改用户代币
     * @param list
     * @return
     */
    int batchUpdateUserVirtualBalances(List<WebUserEntity> list);
}
