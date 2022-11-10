package com.minghui.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_web_withdraw
 */
@TableName(value ="t_web_withdraw")
@Data
public class WebWithdraw implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单号
     */
    @TableField(value = "order_no")
    private String orderNo;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 商户代码
     */
    @TableField(value = "merchant_code")
    private String merchantCode;

    /**
     * 通道代码
     */
    @TableField(value = "channel_code")
    private String channelCode;

    /**
     * 提币金额(法币)
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 实际到账金额(法币)
     */
    @TableField(value = "real_amount")
    private BigDecimal realAmount;

    /**
     * 提币金额(虚拟币)
     */
    @TableField(value = "virtual_amount")
    private BigDecimal virtualAmount;

    /**
     * 实际到账金额(虚拟币 )
     */
    @TableField(value = "virtual_real_amount")
    private BigDecimal virtualRealAmount;

    /**
     * 手续费
     */
    @TableField(value = "fee")
    private BigDecimal fee;

    /**
     * 第三方订单号
     */
    @TableField(value = "pay_order_no")
    private String payOrderNo;

    /**
     * 第三方签名
     */
    @TableField(value = "pay_sign")
    private String paySign;

    /**
     * 1:法币 2:虚拟币
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 错误信息
     */
    @TableField(value = "err_msg")
    private String errMsg;

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

    /**
     * 0:待审核 1:成功 -1:失败
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 上级代理
     */
    @TableField(value = "agent")
    private String agent;

    /**
     * 
     */
    @TableField(value = "agent_node")
    private String agentNode;

    /**
     * 
     */
    @TableField(value = "agent_level")
    private Integer agentLevel;

    private String realName;

    private String account;

    private String mobile;

    private String ifscCode;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}