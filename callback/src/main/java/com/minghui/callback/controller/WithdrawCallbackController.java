package com.minghui.callback.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.minghui.commons.constants.Constant;
import com.minghui.commons.entity.WebDayReportEntity;
import com.minghui.commons.entity.WebFundRecordEntity;
import com.minghui.commons.entity.WebUserEntity;
import com.minghui.commons.entity.WebWithdraw;
import com.minghui.commons.service.*;
import com.minghui.commons.utils.IdUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Api(value = "回调代付相关", tags = "回调代付相关")
@RequestMapping("/withdraw/callback")
@RestController
public class WithdrawCallbackController {

    @Autowired
    private WebUserService webUserService;

    @Autowired
    private WebWithdrawService webWithdrawService;


    @Autowired
    private WebDayReportService webDayReportService;


    private static final double INVITATION_REWARD = 150;

    @Autowired
    private WebFundRecordService webFundRecordService;

    @Autowired
    private WebParamsService webParamsService;

    @Transactional
    @ApiOperation(value = "yypay回调")
    @PostMapping("/yypay")
    public String yypay(HttpServletRequest httpServletRequest) throws Exception {
        String pay_callback_ip = webParamsService.getParamsValue("pay_callback_ip");
        if (StringUtils.isEmpty(pay_callback_ip)) {
            throw new Exception("未配置白名单");
        }
        String[] ips = pay_callback_ip.split(",");
        List<String> ipList = Arrays.asList(ips);
        String clientIP = ServletUtil.getClientIP(httpServletRequest);
        log.info("回调IP:{}", clientIP);
        if (!ipList.contains(clientIP)) {
            throw new Exception("异常IP访问");
        }
        Date now = new Date();
        String body = IoUtil.readUtf8(httpServletRequest.getInputStream());
        log.info("回调参数:{}", body);
        //String body = "{\"curreny\": \"inr\"," +
        //        "\"merchantBizNum\": \"1526914501201498112\"," +
        //        "\"merchantId\": \"U1002\"," +
        //        "\"money\": \"3.000000\"," +
        //        "\"sign\": \"3e4cb63aefba9075727dc80905ca4419\"," +
        //        "\"status\": \"1\"," +
        //        "\"errmsg\": \"Invalid beneficiary details\"," +
        //        "\"sysBizNum\": \"PUSDT8522841267865\"}";
        JSONObject data = JSONObject.parseObject(body);

        // 订单号
        String merchantBizNum = data.getString("merchantBizNum");
        // 签名
        String sign = data.getString("sign");
        WebWithdraw withdraw = webWithdrawService.getOne(new QueryWrapper<WebWithdraw>().lambda().eq(WebWithdraw::getOrderNo, merchantBizNum).eq(WebWithdraw::getStatus, -2));
        if (withdraw == null) {
            throw new Exception("未匹配订单");
        }
        //if (!StringUtils.equals(sign, withdraw.getPaySign())) {
        //    throw new Exception("签名不一致");
        //}

        String status = data.getString("status");
        if (StringUtils.equals(status, "1")) {
            Date date = new Date();
            WebUserEntity user = webUserService.getUser(withdraw.getUserName());

            // 修改订单状态
            boolean update = webWithdrawService.update(
                    new UpdateWrapper<WebWithdraw>().lambda()
                            .eq(WebWithdraw::getId, withdraw.getId())
                            .eq(WebWithdraw::getStatus, -2)
                            .set(WebWithdraw::getStatus, 1)
                            .set(WebWithdraw::getModifyTime, now)
            );
            if (!update) {
                throw new Exception("修改订单失败");
            }

            WebDayReportEntity report = new WebDayReportEntity();
            report.setUserName(user.getUserName());
            report.setToDay(date);
            report.setTopUp(new BigDecimal("0"));
            report.setWithdraw(withdraw.getAmount());
            report.setBet(new BigDecimal("0"));
            report.setInCome(new BigDecimal("0"));
            report.setCommission(new BigDecimal("0"));
            report.setVirtualIncome(new BigDecimal("0"));
            report.setAgent(user.getAgent());
            report.setAgentNode(user.getAgentNode());
            report.setAgentLevel(user.getAgentLevel());
            webDayReportService.insertOrUpdate(report);

            return "success";
        } else if (StringUtils.equals(status, "2")) {
            // 修改订单状态
            webWithdrawService.update(
                    new UpdateWrapper<WebWithdraw>().lambda()
                            .eq(WebWithdraw::getId, withdraw.getId())
                            .eq(WebWithdraw::getStatus, -2)
                            .set(WebWithdraw::getStatus, -1)
                            .set(WebWithdraw::getModifyTime, now)
                            .set(WebWithdraw::getErrMsg, data.getString("errmsg"))
            );

            WebUserEntity user = webUserService.getOne(new QueryWrapper<WebUserEntity>().lambda().eq(WebUserEntity::getUserName, withdraw.getUserName()));
            // 返还金额
            webUserService.updateUserBalance(user.getUserName(), withdraw.getAmount());
            // 返还流水记录
            WebFundRecordEntity fund = new WebFundRecordEntity();
            fund.setUserName(user.getUserName());
            fund.setSerialNo(IdUtils.randomId());
            fund.setRefBillNo(withdraw.getOrderNo());
            fund.setAmount(withdraw.getAmount());
            fund.setBeforeAmount(user.getBalance());
            fund.setAfterAmount(NumberUtil.add(user.getBalance(), withdraw.getAmount()));
            fund.setType(Constant.FundType.RETURN.getValue());
            fund.setCreateTime(new Date());
            fund.setAgent(user.getAgent());
            fund.setAgentNode(user.getAgentNode());
            fund.setAgentLevel(user.getAgentLevel());
            webFundRecordService.save(fund);
            return "success";
        }
        return "error";
    }

    public static void main(String[] args) {
        String s = " Lorenzo Ocsing  ";
        System.out.println(s);
        System.out.println(s.trim());
    }
}
