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
@TableName("t_web_lucky_spin_config")
public class WebLuckySpinConfigEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 奖品
	 */
	private String prize;
	/**
	 * 权重
	 */
	private Integer probability;

}
