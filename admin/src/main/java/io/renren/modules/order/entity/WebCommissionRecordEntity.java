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
 * @date 2022-05-18 21:26:55
 */
@Data
@TableName("t_web_commission_record")
public class WebCommissionRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 下单用户
	 */
	private String userName;
	/**
	 * 上级用户
	 */
	private String commissionUser;
	/**
	 * 佣金
	 */
	private BigDecimal commission;
	/**
	 * 关联订单号
	 */
	private String refBillNo;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 代理节点
	 */
	private String agentNode;
	/**
	 * 代理节点等级
	 */
	private Integer agentLevel;

}
