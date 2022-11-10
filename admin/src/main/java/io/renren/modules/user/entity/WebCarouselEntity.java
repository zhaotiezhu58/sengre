package io.renren.modules.user.entity;

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
 * @date 2022-05-18 17:44:20
 */
@Data
@TableName("t_web_carousel")
public class WebCarouselEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 1:首页 2:邀请
	 */
	private Integer type;
	/**
	 * 0:停用 1:启用
	 */
	private Integer status;

}
