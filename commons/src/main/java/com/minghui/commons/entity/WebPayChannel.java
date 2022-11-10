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
 * @TableName t_web_pay_channel
 */
@TableName(value ="t_web_pay_channel")
@Data
public class WebPayChannel implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 通道代码
     */
    @TableField(value = "channel_code")
    private String channelCode;

    /**
     * 通道名称
     */
    @TableField(value = "channel_name")
    private String channelName;

    /**
     * 1:代收 2:代付
     */
    @TableField(value = "channel_type")
    private Integer channelType;

    /**
     * 1:法币 2:虚拟货币
     */
    @TableField(value = "pay_type")
    private Integer payType;

    /**
     * 商户代码
     */
    @TableField(value = "merchant_code")
    private String merchantCode;

    /**
     * 最小金额
     */
    @TableField(value = "min_amount")
    private BigDecimal minAmount;

    /**
     * 最大金额
     */
    @TableField(value = "max_amount")
    private BigDecimal maxAmount;

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

    /**
     * 值越大越靠前
     */
    private Integer pxh;
}