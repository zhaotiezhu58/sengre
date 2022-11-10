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
@TableName("t_web_fund_record")
public class WebFundRecordEntity implements Serializable {
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
	 * 流水号
	 */
	private String serialNo;
	/**
	 * 业务流水号
	 */
	private String refBillNo;
	/**
	 * 操作金额
	 */
	private BigDecimal amount;
	/**
	 * 操作前金额
	 */
	private BigDecimal beforeAmount;
	/**
	 * 操作后金额
	 */
	private BigDecimal afterAmount;
	/**
	 * 业务类型
	 */
	private Integer type;
	/**
	 * 创建时间
	 */
	private Date createTime;
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
