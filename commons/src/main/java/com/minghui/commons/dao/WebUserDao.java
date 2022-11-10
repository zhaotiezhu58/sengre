package com.minghui.commons.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.minghui.commons.entity.WebUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * 
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-04 18:44:57
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

    /**
     * 修改用户代币余额
     * @param userName
     * @param balance
     * @return
     */
    int updateUserVirtualBalance(@Param("userName") String userName, @Param("balance") BigDecimal balance);

    /**
     * 解锁代币
     * @param userName
     * @param balance
     * @return
     */
    int unlockVirtualBalance(@Param("userName") String userName, @Param("balance") BigDecimal balance);

    /**
     * 收集代币
     * @param userName
     * @param balance
     * @return
     */
    int collectVirtualBalance(@Param("userName") String userName, @Param("balance") BigDecimal balance);
}
