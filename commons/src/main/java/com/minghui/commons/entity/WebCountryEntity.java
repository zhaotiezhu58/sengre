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
 * @date 2022-05-04 18:56:38
 */
@Data
@TableName("t_web_country")
public class WebCountryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 国家名称
	 */
	private String name;
	/**
	 * 国家区号
	 */
	private String code;
	/**
	 * 图片
	 */
	private String img;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 排序号
	 */
	private Integer pxh;

}
