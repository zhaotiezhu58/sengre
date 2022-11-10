package io.renren.modules.order.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class FundExport {

    /**
     * 用户名
     */
    @ExcelProperty("用户名")
    private String userName;
    /**
     * 流水号
     */
    @ExcelProperty("流水号")
    private String serialNo;
    /**
     * 关联业务号
     */
    @ExcelProperty("业务流水号")
    private String refBillNo;
    /**
     * 操作金额
     */
    @ExcelProperty("操作金额")
    private BigDecimal amount;
    /**
     * 操作前金额
     */
    @ExcelProperty("操作前金额")
    private BigDecimal beforeAmount;
    /**
     * 操作后金额
     */
    @ExcelProperty("操作后金额")
    private BigDecimal afterAmount;
    /**
     * 业务类型
     */
    @ExcelProperty("业务类型")
    private String type;
    /**
     * 创建时间
     */
    @ExcelProperty("操作时间")
    private Date createTime;
    /**
     * 上级代理
     */
    @ExcelProperty("上级代理")
    private String agent;
}
