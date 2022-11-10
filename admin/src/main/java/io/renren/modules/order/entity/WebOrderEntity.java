package io.renren.modules.order.entity;

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
 * @date 2022-05-18 18:10:23
 */
@Data
@TableName("t_web_order")
public class WebOrderEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 订单金额
	 */
	private BigDecimal amount;
	/**
	 * 收益
	 */
	private BigDecimal income;
	/**
	 * 虚拟收益
	 */
	private BigDecimal virtualIncome;
	/**
	 * 商品名称
	 */
	private String productName;
	/**
	 * 商品图片
	 */
	private String productUrl;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 最后修改时间
	 */
	private Date modifyTime;
	/**
	 * 上级代理
	 */
	private String agent;
	/**
	 * 代理树节点
	 */
	private String agentNode;
	/**
	 * 代理节点等级
	 */
	private Integer agentLevel;

}
