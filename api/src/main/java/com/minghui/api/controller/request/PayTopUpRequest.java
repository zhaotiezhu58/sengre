package com.minghui.api.controller.request;

import com.minghui.commons.constants.RegConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "充值请求类", description = "充值请求参数")
public class PayTopUpRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "通道代码", example = "10001", required = true)
    private String code;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "充值金额", example = "100", required = true)
    private String amount;
}
