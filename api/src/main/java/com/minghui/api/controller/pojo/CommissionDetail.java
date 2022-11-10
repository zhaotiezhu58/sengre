package com.minghui.api.controller.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CommissionDetail {

    private String friendName;

    private BigDecimal commission;
}
