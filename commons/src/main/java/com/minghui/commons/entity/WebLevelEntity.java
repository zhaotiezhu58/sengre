package com.minghui.commons.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-04 18:44:56
 */
@Data
@TableName("t_web_level")
public class WebLevelEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 等级名称
	 */
	private String levelName;
	/**
	 * 等级值
	 */
	private Integer levelValue;
	/**
	 * 最小余额
	 */
	private BigDecimal minBalance;
	/**
	 * 最大余额
	 */
	private BigDecimal maxBalance;
	/**
	 * 商品金额
	 */
	private Double productPrice;
	/**
	 * 每日刷单数
	 */
	private Integer dayCount;
	/**
	 * 刷单次收益
	 */
	private BigDecimal income;
	/**
	 * 直属1级代理返点百分比
	 */
	private Double agent1;
	/**
	 * 直属2级代理返点百分比
	 */
	private Double agent2;
	/**
	 * 直属3级代理返点百分比
	 */
	private Double agent3;
	/**
	 * 周薪
	 */
	private BigDecimal weeklySalary;
	/**
	 * 类型 (1:普通等级 2:VIP等级)
	 */
	private Integer levelType;
	/**
	 * 状态 (0:停用 1:启用)
	 */
	private Integer status;

}
