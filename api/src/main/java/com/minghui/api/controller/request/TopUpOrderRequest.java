package com.minghui.api.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "充值列表请求类", description = "充值列表请求参数")
public class TopUpOrderRequest extends PageBaseRequest{

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "1:法定货币 2:虚拟货币", example = "1", required = true)
    private String type;
}
