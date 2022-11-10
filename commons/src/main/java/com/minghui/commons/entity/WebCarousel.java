package com.minghui.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName t_web_carousel
 */
@TableName(value ="t_web_carousel")
@Data
public class WebCarousel implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 1:首页 2:邀请
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 0:停用 1:启用
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}