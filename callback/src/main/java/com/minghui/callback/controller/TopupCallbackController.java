package com.minghui.callback.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.minghui.commons.constants.Constant;
import com.minghui.commons.entity.*;
import com.minghui.commons.service.*;
import com.minghui.commons.utils.IdUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Api(value = "回调支付相关", tags = "回调支付")
@RequestMapping("/topup/callback")
@RestController
public class TopupCallbackController {

    @Autowired
    private WebUserService webUserService;

    @Autowired
    private WebTopupService webTopupService;

    @Autowired
    private WebFundRecordService webFundRecordService;

    @Autowired
    private WebDayReportService webDayReportService;

    @Autowired
    private WebVirtualRecordService webVirtualRecordService;

    private static final double INVITATION_REWARD = 150;

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
        //        "\"merchantBizNum\": \"1529494893104336896\"," +
        //        "\"merchantId\": \"U1002\"," +
        //        "\"money\": \"500\"," +
        //        "\"orderMoney\": \"100\"," +
        //        "\"sign\": \"3e4cb63aefba9075727dc80905ca4419\"," +
        //        "\"status\": \"1\"," +
        //        "\"sysBizNum\": \"PUSDT8522841267865\"}";
        JSONObject data = JSONObject.parseObject(body);

        // 订单号
        String merchantBizNum = data.getString("merchantBizNum");
        // 签名
        String sign = data.getString("sign");
        WebTopup topup = webTopupService.getOne(new QueryWrapper<WebTopup>().lambda().eq(WebTopup::getOrderNo, merchantBizNum).eq(WebTopup::getStatus, 1));
        if (topup == null) {
            throw new Exception("未匹配订单");
        }
        //if (!StringUtils.equals(sign.toLowerCase(), topup.getPaySign().toLowerCase())) {
        //    throw new Exception("签名不一样");
        //}
        Integer status = data.getInteger("status");
        if (status.intValue() != 1) {
            return "error";
        }

        BigDecimal orderMoney = data.getBigDecimal("money");
        if (topup.getAmount().doubleValue() != orderMoney.doubleValue()) {
            //// 订单金额与实际付款金额不符11
            topup.setRealAmount(orderMoney);
            if (topup.getType().intValue() == 2) {
                // 充U汇率
                String rate = webParamsService.getParamsValue("usdt_exchange_rate");
                BigDecimal realAmount = NumberUtil.mul(orderMoney, NumberUtils.toDouble(rate));
                topup.setRealAmount(realAmount);
            }
        }

        Date date = new Date();
        WebUserEntity user = webUserService.getUser(topup.getUserName());

        // 修改订单状态
        boolean update = webTopupService.update(
                new UpdateWrapper<WebTopup>().lambda()
                        .eq(WebTopup::getId, topup.getId())
                        .eq(WebTopup::getStatus, 1)
                        .set(WebTopup::getStatus, 2)
                        .set(WebTopup::getModifyTime, now)
                        .set(WebTopup::getRealAmount, topup.getRealAmount())
        );
        if (!update) {
            throw new Exception("修改订单失败");
        }

        List<WebFundRecordEntity> funds = new ArrayList<>();
        // 添加充值流水
        WebFundRecordEntity fund = new WebFundRecordEntity();
        fund.setUserName(user.getUserName());
        fund.setSerialNo(IdUtils.randomId());
        fund.setRefBillNo(topup.getOrderNo());
        fund.setAmount(topup.getRealAmount());
        fund.setBeforeAmount(user.getBalance());
        fund.setAfterAmount(NumberUtil.add(user.getBalance(), topup.getRealAmount()));
        fund.setType(Constant.FundType.TOPUP.getValue());
        fund.setCreateTime(date);
        fund.setAgent(user.getAgent());
        fund.setAgentNode(user.getAgentNode());
        fund.setAgentLevel(user.getAgentLevel());
        funds.add(fund);

        // 充值赠送
        // 充值区间赠送金额
        BigDecimal giveAmount = new BigDecimal("0");
        String topup_give_away = webParamsService.getParamsValue("topup_give_away");
        JSONObject topupObj = JSONObject.parseObject(topup_give_away);
        Double min = topupObj.getDouble("min");
        Double max = topupObj.getDouble("max");
        Double percentage = topupObj.getDouble("percentage");
        if (topup.getRealAmount().doubleValue() >= min && topup.getRealAmount().doubleValue() <= max) {
            if (percentage.doubleValue() > 0) {
                giveAmount = NumberUtil.mul(topup.getRealAmount(), NumberUtil.div(percentage.doubleValue(), 100));
                // 添加赠送流水流水记录
                WebFundRecordEntity giveFund = new WebFundRecordEntity();
                giveFund.setUserName(user.getUserName());
                giveFund.setSerialNo(IdUtils.randomId());
                giveFund.setRefBillNo(topup.getOrderNo());
                giveFund.setAmount(giveAmount);
                giveFund.setBeforeAmount(fund.getAfterAmount());
                giveFund.setAfterAmount(NumberUtil.add(fund.getAfterAmount(), giveAmount));
                giveFund.setType(Constant.FundType.PLAT.getValue());
                giveFund.setCreateTime(date);
                giveFund.setAgent(user.getAgent());
                giveFund.setAgentNode(user.getAgentNode());
                giveFund.setAgentLevel(user.getAgentLevel());
                funds.add(giveFund);
            }
        }
        webFundRecordService.saveBatch(funds);

        // 给用户加钱
        webUserService.updateUserBalance(user.getUserName(), NumberUtil.add(topup.getRealAmount(), giveAmount));

        // 记录报表
        WebDayReportEntity dayReport = new WebDayReportEntity();
        dayReport.setUserName(user.getUserName());
        dayReport.setToDay(now);
        dayReport.setTopUp(topup.getRealAmount());
        dayReport.setWithdraw(new BigDecimal("0"));
        dayReport.setBet(new BigDecimal("0"));
        dayReport.setInCome(new BigDecimal("0"));
        dayReport.setCommission(new BigDecimal("0"));
        dayReport.setVirtualIncome(new BigDecimal("0"));
        dayReport.setAgent(user.getAgent());
        dayReport.setAgentNode(user.getAgentNode());
        dayReport.setAgentLevel(user.getAgentLevel());
        webDayReportService.insertOrUpdate(dayReport);

        // 解锁上级虚拟代币
        unlockVirtualAmount(topup, user);
        // 邀请奖励
        invitationReward(topup, user);
        return "success";
    }

    /**
     * 邀请奖励
     * @param topup
     * @param user
     */
    private void invitationReward(WebTopup topup, WebUserEntity user) throws Exception {
        // 500以下没有邀请奖励
        if (topup.getRealAmount().doubleValue() < 500) {
            return;
        }
        // 查询用户是否是首充
        int count = webTopupService.count(
                new QueryWrapper<WebTopup>().lambda()
                        .eq(WebTopup::getUserName, user.getUserName())
                        .eq(WebTopup::getStatus, 2)
        );
        if (count > 1) {
            return;
        }

        // 获取上级代理信息
        WebUserEntity agentUser = webUserService.getUser(user.getAgent());
        if (agentUser == null) {
            return;
        }

        // 上级代理加钱
        webUserService.updateUserBalance(agentUser.getUserName(), new BigDecimal(INVITATION_REWARD));
        // 流水记录
        WebFundRecordEntity fund = new WebFundRecordEntity();
        fund.setUserName(agentUser.getUserName());
        fund.setSerialNo(IdUtils.randomId());
        fund.setRefBillNo(topup.getOrderNo());
        fund.setAmount(new BigDecimal(INVITATION_REWARD));
        fund.setBeforeAmount(agentUser.getBalance());
        fund.setAfterAmount(NumberUtil.add(agentUser.getBalance(), INVITATION_REWARD));
        fund.setType(Constant.FundType.INVITATION_REWARD.getValue());
        fund.setCreateTime(new Date());
        fund.setAgent(agentUser.getAgent());
        fund.setAgentNode(agentUser.getAgentNode());
        fund.setAgentLevel(agentUser.getAgentLevel());
        webFundRecordService.save(fund);
    }

    /**
     * 解锁上级代币
     * @param topup
     * @param user
     * @throws Exception
     */
    private void unlockVirtualAmount(WebTopup topup, WebUserEntity user) throws Exception {
        // 获取上级代理信息
        WebUserEntity agentUser = webUserService.getUser(user.getAgent());
        if (agentUser == null || agentUser.getVirtualBalance().doubleValue() <= 0) {
            return;
        }
        // 可以解锁的代币金额
        BigDecimal unlockAmount = NumberUtil.mul(topup.getRealAmount().doubleValue() * 0.15);
        if (unlockAmount.doubleValue() > agentUser.getVirtualBalance().doubleValue()) {
           // 可解锁的金额大于代理代币余额
           unlockAmount = agentUser.getVirtualBalance();
        }
        // 解锁代币
        webUserService.unlockVirtualBalance(agentUser.getUserName(), unlockAmount);
        // 代币记录
        WebVirtualRecordEntity virtual = new WebVirtualRecordEntity();
        virtual.setUserName(agentUser.getUserName());
        virtual.setSerialNo(IdUtils.randomId());
        virtual.setRefBillNo(topup.getOrderNo());
        virtual.setAmount(unlockAmount.negate());
        virtual.setBeforeAmount(agentUser.getVirtualBalance());
        virtual.setAfterAmount(NumberUtil.sub(agentUser.getVirtualBalance(), unlockAmount));
        virtual.setType(Constant.VirtualType.TOPUP_LOCK.getValue());
        virtual.setCreateTime(new Date());
        virtual.setAgent(agentUser.getAgent());
        virtual.setAgentNode(agentUser.getAgentNode());
        virtual.setAgentLevel(agentUser.getAgentLevel());
        webVirtualRecordService.save(virtual);
    }
}
