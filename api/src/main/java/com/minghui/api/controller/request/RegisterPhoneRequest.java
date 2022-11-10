package com.minghui.api.controller.request;

import com.minghui.commons.constants.RegConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "注册请求类", description = "注册请求参数")
public class RegisterPhoneRequest {

    @ApiModelProperty(value = "手机号码/邮箱", example = "9453377119", required = true)
    private String userName;

    @Pattern(regexp = RegConstant.USER_PWD_REG, message = "validation.forget.pwd.error")
    @NotNull(message = "validation.forget.pwd.error")
    @ApiModelProperty(value = "密码", example = "123qwe", required = true)
    private String password;

    @Pattern(regexp = RegConstant.USER_PWD_REG, message = "validation.forget.pwd.error")
    @NotNull(message = "validation.forget.pwd.error")
    @ApiModelProperty(value = "确认密码", example = "123qwe", required = true)
    private String confirmPassword;

    @NotNull(message = "validation.forget.pwd.error")
    @ApiModelProperty(value = "邀请码", example = "1234", required = false)
    private String inviteCode;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "获取验证码时的UUID", example = "1234", required = true)
    private String uuid;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "1:手机号 2:邮箱", example = "1", required = true)
    private String type;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "验证码", example = "1234", required = true)
    private String verificationCode;
}
