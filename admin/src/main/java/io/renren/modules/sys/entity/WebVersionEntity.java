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
 * @date 2022-05-18 21:38:25
 */
@Data
@TableName("t_web_version")
public class WebVersionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 版本号
	 */
	private String appVersion;
	/**
	 * 下载地址
	 */
	private String appUrl;
	/**
	 * 版本坐标
	 */
	private Integer appIndex;

}
