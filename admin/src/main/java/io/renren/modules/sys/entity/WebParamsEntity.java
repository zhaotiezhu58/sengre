package io.renren.modules.sys.entity;

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
 * @date 2022-03-03 18:43:48
 */
@Data
@TableName("t_web_params")
public class WebParamsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * key
	 */
	private String paramsKey;
	/**
	 * value
	 */
	private String paramsValue;
	/**
	 * 备注
	 */
	private String remake;
	/**
	 * 1:普通文本 2:html文本
	 */
	private Integer type;

}
