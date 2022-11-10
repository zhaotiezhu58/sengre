package com.minghui.commons.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-04 18:44:57
 */
@Data
@TableName("t_web_user")
public class WebUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 国家区号
	 */
	private String areaCode;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 余额
	 */
	private BigDecimal balance;
	/**
	 * 虚拟余额
	 */
	private BigDecimal virtualBalance;
	/**
	 * 已解锁的虚拟金额
	 */
	private BigDecimal unlockVirtualBalance;
	/**
	 * 登录密码
	 */
	private String loginPwd;
	/**
	 * 邀请码
	 */
	private String inviteCode;
	/**
	 * 状态 0:冻结 1:正常
	 */
	private Integer status;
	/**
	 * 上级代理
	 */
	private String agent;
	/**
	 * 代理树节点
	 */
	private String agentNode;
	/**
	 * 代理节点等级
	 */
	private Integer agentLevel;
	/**
	 * 注册时间
	 */
	private Date regTime;
	/**
	 * 注册IP
	 */
	private String regIp;
	/**
	 * 登录时间
	 */
	private Date loginTime;
	/**
	 * 登录IP
	 */
	private String loginIp;
	/**
	 * 备注
	 */
	private String remake;

	private String inviteQrcode;
}
