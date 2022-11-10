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
 * @date 2022-02-26 21:01:52
 */
@Data
@TableName("t_web_user_log")
public class WebUserLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 用户手机号
	 */
	private String userPhone;
	/**
	 * 登录IP
	 */
	private String loginIp;
	/**
	 * 登录IP详细地址
	 */
	private String loginIpDetail;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
