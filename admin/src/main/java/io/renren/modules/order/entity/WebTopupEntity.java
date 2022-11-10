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
 * @date 2022-05-18 18:21:18
 */
@Data
@TableName("t_web_topup")
public class WebTopupEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 充值金额
	 */
	private BigDecimal amount;
	/**
	 * 实际到账金额
	 */
	private BigDecimal realAmount;
	/**
	 * 1:法币 2:虚拟币
	 */
	private Integer type;
	/**
	 * 第三方签名
	 */
	private String paySign;
	/**
	 * 第三方订单号
	 */
	private String payOrderNo;
	/**
	 * 1:inr 2:usdt
	 */
	private Integer payCurreny;
	/**
	 * 用户IP
	 */
	private String ip;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 最后修改时间
	 */
	private Date modifyTime;
	/**
	 * 0:初始化 1:待支付 2:成功
	 */
	private Integer status;
	/**
	 * 商户代码
	 */
	private String merchantCode;
	/**
	 * 通道代码
	 */
	private String channelCode;
	/**
	 * 
	 */
	private String agent;
	/**
	 * 
	 */
	private String agentNode;
	/**
	 * 
	 */
	private Integer agentLevel;

}
