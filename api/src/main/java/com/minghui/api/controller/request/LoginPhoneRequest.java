package com.minghui.api.controller.request;

import com.minghui.commons.constants.RegConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "密码登录请求类", description = "密码登录参数")
public class LoginPhoneRequest {

    @ApiModelProperty(value = "system.param.err", example = "9453377119", required = true)
    private String userName;

    @Pattern(regexp = RegConstant.USER_PWD_REG, message = "validation.forget.pwd.error")
    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "密码", example = "123qwe", required = true)
    private String password;

    @ApiModelProperty(value = "system.param.err", example = "1:手机号 2:邮箱", required = true)
    private String type;
}
