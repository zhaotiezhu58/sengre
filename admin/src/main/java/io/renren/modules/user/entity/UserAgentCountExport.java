package io.renren.modules.user.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserAgentCountExport {

    /**
     * 用户名
     */
    @ExcelProperty("用户名")
    private String userName;
    /**
     * 代理数
     */
    @ExcelProperty("代理数")
    private long count;
}
