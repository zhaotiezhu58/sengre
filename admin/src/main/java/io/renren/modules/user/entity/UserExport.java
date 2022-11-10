package io.renren.modules.user.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserExport {

    /**
     * 用户名
     */
    @ExcelProperty("用户名")
    private String userName;
    /**
     * 余额
     */
    @ExcelProperty("余额")
    private BigDecimal balance;
    /**
     * 代币
     */
    @ExcelProperty("代币")
    private BigDecimal virtualBalance;
    /**
     * 邀请码
     */
    @ExcelProperty("邀请码")
    private String inviteCode;
    /**
     * 上级代理
     */
    @ExcelProperty("上级代理")
    private String agent;
    /**
     * 注册时间
     */
    @ExcelProperty("注册时间")
    private Date regTime;
    /**
     * 注册IP
     */
    @ExcelProperty("注册IP")
    private String regIp;
    /**
     * 登录时间
     */
    @ExcelProperty("登录时间")
    private Date loginTime;
    /**
     * 登录IP
     */
    @ExcelProperty("登录IP")
    private String loginIp;
    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String remake;
}
