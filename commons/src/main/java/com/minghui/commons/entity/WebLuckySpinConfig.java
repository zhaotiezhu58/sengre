package com.minghui.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName t_web_lucky_spin_config
 */
@TableName(value ="t_web_lucky_spin_config")
@Data
public class WebLuckySpinConfig implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 奖品
     */
    @TableField(value = "prize")
    private String prize;

    /**
     * 权重
     */
    @TableField(value = "probability")
    private Integer probability;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}