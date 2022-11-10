package com.minghui.api.controller.request;

import com.minghui.commons.constants.RegConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "密码修改请求类", description = "密码修改请求类")
public class PasswordSetRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "问题ID", example = "1", required = true)
    private String id;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "问题答案", example = "北京", required = true)
    private String answer;

    @Pattern(regexp = RegConstant.USER_PWD_REG, message = "validation.forget.pwd.error")
    @NotNull(message = "validation.forget.pwd.error")
    @ApiModelProperty(value = "密码", example = "123qwe", required = true)
    private String password;

    @Pattern(regexp = RegConstant.USER_PWD_REG, message = "validation.forget.pwd.error")
    @NotNull(message = "validation.forget.pwd.error")
    @ApiModelProperty(value = "确认密码", example = "123qwe", required = true)
    private String confirmPassword;
}
