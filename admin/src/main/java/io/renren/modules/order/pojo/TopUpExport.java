package io.renren.modules.order.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TopUpExport {

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
     * 充值金额
     */
    @ExcelProperty("金额")
    private BigDecimal amount;
    /**
     * 实际到账金额
     */
    @ExcelProperty("实际到账金额")
    private BigDecimal realAmount;
    /**
     * 1:法币 2:虚拟币
     */
    @ExcelProperty("货币")
    private String type;
    /**
     * 第三方订单号
     */
    @ExcelProperty("第三方订单号")
    private String payOrderNo;
    /**
     * 用户IP
     */
    @ExcelProperty("IP")
    private String ip;
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
     * 0:初始化 1:待支付 2:成功
     */
    @ExcelProperty("状态")
    private String status;
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
     *
     */
    @ExcelProperty("上级代理")
    private String agent;
}
