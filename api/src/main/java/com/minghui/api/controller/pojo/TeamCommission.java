package com.minghui.api.controller.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeamCommission {
    private Integer people;

    private BigDecimal commission;

    private double rate;
}
