package io.renren.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.user.entity.WebUserEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 16:07:36
 */
public interface WebUserService extends IService<WebUserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<WebUserEntity> queryList(Map<String, Object> params);

    void updateUserBalance(String userName, BigDecimal balance) throws Exception;

    void batchUpdateUserVirtualBalances(List<WebUserEntity> list) throws Exception;
}

