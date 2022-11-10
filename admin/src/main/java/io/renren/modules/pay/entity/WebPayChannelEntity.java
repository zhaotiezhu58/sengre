package io.renren.modules.pay.entity;

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
 * @date 2022-05-18 17:55:19
 */
@Data
@TableName("t_web_pay_channel")
public class WebPayChannelEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 通道代码
	 */
	private String channelCode;
	/**
	 * 通道名称
	 */
	private String channelName;
	/**
	 * 商户代码
	 */
	private String merchantCode;
	/**
	 * 最小金额
	 */
	private BigDecimal minAmount;
	/**
	 * 最大金额
	 */
	private BigDecimal maxAmount;
	/**
	 * 1:代收 2:代付
	 */
	private Integer channelType;
	/**
	 * 1:法币 2:虚拟货币
	 */
	private Integer payType;
	/**
	 * 0:停用 1:启用
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 最后修改时间
	 */
	private Date modifyTime;
	/**
	 * 值越大越靠前
	 */
	private Integer pxh;

}
