package com.minghui.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.minghui.commons.entity.WebLevelEntity;
import com.minghui.commons.utils.PageUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-04 18:44:56
 */
public interface WebLevelService extends IService<WebLevelEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据分类获取等级列表
     * @param type
     * @return
     */
    List<WebLevelEntity> getLevelsByType(int type);

    /**
     * 根据等级值获取等级信息
     * @param value
     * @return
     */
    WebLevelEntity getLevelByLevelValue(int value);

    /**
     * 判断用户当前属于哪个等级
     * @param type
     * @param amount
     * @return
     */
    WebLevelEntity getUserCurrLevel(List<WebLevelEntity> list, BigDecimal amount);
}

