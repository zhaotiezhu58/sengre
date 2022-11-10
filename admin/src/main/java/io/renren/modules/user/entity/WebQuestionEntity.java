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
 * @date 2022-05-18 16:34:07
 */
@Data
@TableName("t_web_question")
public class WebQuestionEntity implements Serializable {
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
	 * 问题ID
	 */
	private String questionId;
	/**
	 * 答案
	 */
	private String answer;
	/**
	 * 设置时间
	 */
	private Date createTime;

}
