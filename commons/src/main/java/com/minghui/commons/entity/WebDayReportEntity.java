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
@TableName("t_web_day_report")
public class WebDayReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 用户手机号
	 */
	private String userName;
	/**
	 * 日期
	 */
	private Date toDay;
	/**
	 * 充值
	 */
	private BigDecimal topUp;
	/**
	 * 提现
	 */
	private BigDecimal withdraw;
	/**
	 * 投注量
	 */
	private BigDecimal bet;
	/**
	 * 收益
	 */
	private BigDecimal inCome;
	/**
	 * 佣金
	 */
	private BigDecimal commission;
	/**
	 * 代币
	 */
	private BigDecimal virtualIncome;
	/**
	 * 上级代理
	 */
	private String agent;
	/**
	 * 代理树节点
	 */
	private String agentNode;
	/**
	 * 代理树节点
	 */
	private Integer agentLevel;

}
