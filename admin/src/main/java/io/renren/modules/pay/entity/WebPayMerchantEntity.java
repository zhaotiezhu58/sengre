package io.renren.modules.pay.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("t_web_pay_merchant")
public class WebPayMerchantEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 商户号
	 */
	private String merchantCode;
	/**
	 * 商户名称
	 */
	private String merchantName;
	/**
	 * 商户私钥
	 */
	private String merchantKey;
	/**
	 * 充值地址
	 */
	private String topUrl;
	/**
	 * 提现地址
	 */
	private String withdrawUrl;
	/**
	 * 充值回调
	 */
	private String topupNotifyUrl;
	/**
	 * 提现回调
	 */
	private String withdrawNotifyUrl;
	/**
	 * 0:停用 1:启用
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 最后修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date modifyTime;

}
