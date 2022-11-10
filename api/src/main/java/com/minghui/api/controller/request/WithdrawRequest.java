package com.minghui.api.controller.request;

import com.minghui.commons.constants.RegConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "法定提币请求类", description = "法定提币请求参数")
public class WithdrawRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "提币金额", example = "100", required = true)
    private String money;

    @ApiModelProperty(value = "提币姓名(法定货币必填,虚拟货币可选)", example = "张三", required = false)
    private String realName;

    @ApiModelProperty(value = "ifsc码,选填", example = "46546", required = false)
    private String ifscCode;

    @ApiModelProperty(value = "手机号码,选填", example = "15152033356", required = false)
    private String mobile;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "提币帐号", example = "5464652135465", required = true)
    private String account;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "1:法定货币 2:虚拟货币", example = "1", required = true)
    private String type;
}
