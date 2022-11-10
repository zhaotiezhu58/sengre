package com.minghui.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName t_web_version
 */
@TableName(value ="t_web_version")
@Data
public class WebVersion implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 版本号
     */
    @TableField(value = "app_version")
    private String appVersion;

    /**
     * 下载地址
     */
    @TableField(value = "app_url")
    private String appUrl;

    /**
     * 版本坐标
     */
    @TableField(value = "app_index")
    private Integer appIndex;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}