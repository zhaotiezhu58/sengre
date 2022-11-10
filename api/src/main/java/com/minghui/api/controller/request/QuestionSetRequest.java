package com.minghui.api.controller.request;

import com.minghui.commons.constants.RegConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "密码问题设置请求类", description = "密码问题设置请求类")
public class QuestionSetRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "问题ID", example = "1", required = true)
    private String id;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "问题答案", example = "北京", required = true)
    private String answer;
}
