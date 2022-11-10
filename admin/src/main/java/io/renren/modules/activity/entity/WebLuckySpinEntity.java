package io.renren.modules.activity.entity;

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
 * @date 2022-05-18 21:49:29
 */
@Data
@TableName("t_web_lucky_spin")
public class WebLuckySpinEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 奖品碎片
	 */
	private String prize;
	/**
	 * 参与时间
	 */
	private Date createTime;

}
