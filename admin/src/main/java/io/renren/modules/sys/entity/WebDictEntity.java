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
 * @date 2022-06-03 00:33:06
 */
@Data
@TableName("t_web_dict")
public class WebDictEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 名称
	 */
	private String dictName;
	/**
	 * 键
	 */
	private String dictKey;
	/**
	 * 值
	 */
	private String dictValue;
	/**
	 * 1:资金流动 2:充值 3:提现 4:投资状态
	 */
	private Integer type;
	/**
	 * 0:停用 1:启用
	 */
	private Integer status;
	/**
	 * 排序号
	 */
	private Integer pxh;

}
