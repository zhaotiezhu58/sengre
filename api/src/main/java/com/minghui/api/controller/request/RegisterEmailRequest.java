package com.minghui.api.controller.request;

import com.minghui.commons.constants.RegConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "注册请求类", description = "注册请求参数")
public class RegisterEmailRequest {

    @ApiModelProperty(value = "邮箱地址", example = "9453377119", required = true)
    private String email;

    @Pattern(regexp = RegConstant.USER_PWD_REG, message = "validation.forget.pwd.error")
    @NotNull(message = "validation.forget.pwd.error")
    @ApiModelProperty(value = "密码", example = "123qwe", required = true)
    private String password;

    @Pattern(regexp = RegConstant.USER_PWD_REG, message = "validation.forget.pwd.error")
    @NotNull(message = "validation.forget.pwd.error")
    @ApiModelProperty(value = "确认密码", example = "123qwe", required = true)
    private String confirmPassword;

    @ApiModelProperty(value = "邀请码", example = "1234")
    private String inviteCode;

    @ApiModelProperty(value = "获取验证码时的UUID", example = "1234")
    private String uuid;
}
