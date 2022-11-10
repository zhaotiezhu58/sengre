package io.renren.modules.order.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class WithdrawExport {

    /**
     * 订单号
     */
    @ExcelProperty("订单号")
    private String orderNo;
    /**
     * 用户名
     */
    @ExcelProperty("用户名")
    private String userName;
    /**
     * 商户代码
     */
    @ExcelProperty("商户代码")
    private String merchantCode;
    /**
     * 通道代码
     */
    @ExcelProperty("通道代码")
    private String channelCode;
    /**
     * 提币金额(法币)
     */
    @ExcelProperty("金额")
    private BigDecimal amount;
    /**
     * 实际到账金额(法币)
     */
    @ExcelProperty("实际到账")
    private BigDecimal realAmount;
    /**
     * 手续费
     */
    @ExcelProperty("手续费")
    private BigDecimal fee;
    /**
     * 手续费
     */
    @ExcelProperty("出款账户")
    private String account;
    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private Date createTime;
    /**
     * 最后修改时间
     */
    @ExcelProperty("最后修改时间")
    private Date modifyTime;
    /**
     * 0:待审核 1:成功 -1:失败
     */
    @ExcelProperty("状态")
    private String status;
    /**
     * 上级代理
     */
    @ExcelProperty("上级代理")
    private String agent;
    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String remake;
}
