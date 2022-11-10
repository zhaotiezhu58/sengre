package com.minghui.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_web_pay_merchant
 */
@TableName(value ="t_web_pay_merchant")
@Data
public class WebPayMerchant implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商户号
     */
    @TableField(value = "merchant_code")
    private String merchantCode;

    /**
     * 商户名称
     */
    @TableField(value = "merchant_name")
    private String merchantName;

    /**
     * 商户私钥
     */
    @TableField(value = "merchant_key")
    private String merchantKey;

    /**
     * 充值地址
     */
    @TableField(value = "top_url")
    private String topUrl;

    /**
     * 提现地址
     */
    @TableField(value = "withdraw_url")
    private String withdrawUrl;

    /**
     * 充值回调
     */
    @TableField(value = "topup_notify_url")
    private String topupNotifyUrl;

    /**
     * 提现回调
     */
    @TableField(value = "withdraw_notify_url")
    private String withdrawNotifyUrl;

    /**
     * 0:停用 1:启用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 最后修改时间
     */
    @TableField(value = "modify_time")
    private Date modifyTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}