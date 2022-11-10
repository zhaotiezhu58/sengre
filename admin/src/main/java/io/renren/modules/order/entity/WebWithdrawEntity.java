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
@TableName("t_web_withdraw")
public class WebWithdrawEntity implements Serializable {
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
	 * 商户代码
	 */
	private String merchantCode;
	/**
	 * 通道代码
	 */
	private String channelCode;
	/**
	 * 提币金额(法币)
	 */
	private BigDecimal amount;
	/**
	 * 实际到账金额(法币)
	 */
	private BigDecimal realAmount;
	/**
	 * 提币金额(虚拟币)
	 */
	private BigDecimal virtualAmount;
	/**
	 * 实际到账金额(虚拟币 )
	 */
	private BigDecimal virtualRealAmount;
	/**
	 * 手续费
	 */
	private BigDecimal fee;
	/**
	 * 第三方订单号
	 */
	private String payOrderNo;
	/**
	 * 第三方签名
	 */
	private String paySign;
	/**
	 * 1:法币 2:虚拟币
	 */
	private Integer type;
	/**
	 * 错误信息
	 */
	private String errMsg;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 最后修改时间
	 */
	private Date modifyTime;
	/**
	 * 0:待审核 1:成功 -1:失败
	 */
	private Integer status;
	/**
	 * 上级代理
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
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * 账户
	 */
	private String account;

	private String mobile;

	private String ifscCode;
	private String remake;
}
