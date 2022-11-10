package com.minghui.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.minghui.commons.entity.WebUserEntity;
import com.minghui.commons.utils.PageUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-04 18:44:57
 */
public interface WebUserService extends IService<WebUserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateUserBalance(String userName, BigDecimal balance) throws Exception;

    void batchUpdateUserVirtualBalances(List<WebUserEntity> list) throws Exception;

    void updateUserVirtualBalance(String userName, BigDecimal balance) throws Exception;

    void unlockVirtualBalance(String userName, BigDecimal balance) throws Exception;

    void collectVirtualBalance(String userName, BigDecimal balance) throws Exception;

    WebUserEntity getUser(String userName);
}

