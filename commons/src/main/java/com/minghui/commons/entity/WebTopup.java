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
 * @TableName t_web_topup
 */
@TableName(value ="t_web_topup")
@Data
public class WebTopup implements Serializable {
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
     * 充值金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 实际到账金额
     */
    @TableField(value = "real_amount")
    private BigDecimal realAmount;

    /**
     * 类型
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 第三方签名
     */
    @TableField(value = "pay_sign")
    private String paySign;

    /**
     * 第三方订单号
     */
    @TableField(value = "pay_order_no")
    private String payOrderNo;

    /**
     * 1:inr 2:usdt
     */
    @TableField(value = "pay_curreny")
    private Integer payCurreny;

    /**
     * 用户IP
     */
    @TableField(value = "ip")
    private String ip;

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
     * 0:初始化 1:待支付 2:已支付
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 商户代码
     */
    private String merchantCode;

    /**
     * 通道代码
     */
    private String channelCode;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

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