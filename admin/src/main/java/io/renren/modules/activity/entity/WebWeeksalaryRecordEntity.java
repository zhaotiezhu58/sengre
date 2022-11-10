package io.renren.modules.activity.entity;

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
 * @date 2022-05-18 21:42:14
 */
@Data
@TableName("t_web_weeksalary_record")
public class WebWeeksalaryRecordEntity implements Serializable {
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
	 * 领取日期
	 */
	private String today;
	/**
	 * 领取金额
	 */
	private BigDecimal amount;
	/**
	 * 领取时间
	 */
	private Date createTime;

}
