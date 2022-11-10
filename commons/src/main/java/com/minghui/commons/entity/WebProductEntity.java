package com.minghui.commons.entity;

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
 * @date 2022-05-04 18:44:56
 */
@Data
@TableName("t_web_product")
public class WebProductEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 商品名称
	 */
	private String productName;
	/**
	 * 商品图片
	 */
	private String productImg;
	/**
	 * 等级值
	 */
	private Integer levelValue;
	/**
	 * 排序号(值越大越靠前)
	 */
	private Integer pxh;
	/**
	 * 状态(0:停用 1:启用)
	 */
	private Integer status;

}
