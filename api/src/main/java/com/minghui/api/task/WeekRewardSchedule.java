package com.minghui.api.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.minghui.commons.constants.Constant;
import com.minghui.commons.entity.WebUserEntity;
import com.minghui.commons.entity.WebVirtualRecordEntity;
import com.minghui.commons.service.WebParamsService;
import com.minghui.commons.service.WebUserService;
import com.minghui.commons.service.WebVirtualRecordService;
import com.minghui.commons.utils.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.util.*;

/**
 * 每周奖励
 * @author Administrator
 */
@Slf4j
//@Component
public class WeekRewardSchedule {

    @Autowired
    private WebParamsService webParamsService;

    @Autowired
    private WebUserService webUserService;

    @Autowired
    private WebVirtualRecordService webVirtualRecordService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Scheduled(cron = "${task.weekreward.cron}")
    public void weekReward() {

        log.info("每周奖励开始...");
        String enable_week_reward = webParamsService.getParamsValue("enable_week_reward");
        if (!StringUtils.equals(enable_week_reward, "1")) {
            return;
        }

        String weekreward = webParamsService.getParamsValue("weekreward");
        BigDecimal minAmount = new BigDecimal(weekreward);

        // 查询满足条件的用户
        List<WebUserEntity> users = webUserService.list(new QueryWrapper<WebUserEntity>().lambda().ge(WebUserEntity::getBalance, minAmount));
        if (CollUtil.isNotEmpty(users)) {
            List<WebVirtualRecordEntity> virtuals = new ArrayList<>();
            List<WebUserEntity> updateUsers = new ArrayList<>();
            Date date = new Date();
            for (WebUserEntity user : users) {
                BigDecimal amount = NumberUtil.mul(user.getBalance(), 2);
                // 添加代币
                //webUserService.updateUserBalance(;);

                WebUserEntity temp = new WebUserEntity();
                temp.setUserName(user.getUserName());
                temp.setVirtualBalance(amount);
                updateUsers.add(temp);

                // 添加代币记录
                WebVirtualRecordEntity virtual = new WebVirtualRecordEntity();
                virtual.setUserName(user.getUserName());
                virtual.setSerialNo(IdUtils.randomId());
                virtual.setRefBillNo(null);
                virtual.setAmount(amount);
                virtual.setBeforeAmount(user.getVirtualBalance());
                virtual.setAfterAmount(NumberUtil.add(user.getVirtualBalance(), amount));
                virtual.setType(Constant.VirtualType.WEEKRAWARD.getValue());
                virtual.setCreateTime(date);
                virtual.setAgent(user.getAgent());
                virtual.setAgentNode(user.getAgentNode());
                virtual.setAgentLevel(user.getAgentLevel());
                virtuals.add(virtual);
            }
            TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
            try {
                webUserService.batchUpdateUserVirtualBalances(updateUsers);
                webVirtualRecordService.saveBatch(virtuals);
                transactionManager.commit(transactionStatus);
            } catch (Exception e) {
                e.printStackTrace();
                transactionManager.rollback(transactionStatus);
            }
        }
        log.info("每周奖励结束...");
    }

    public static void main(String[] args) {
        topup();
        //withdraw();
    }

    public static void topup() {
        Map<String, Object> params = new TreeMap<>();
        params.put("merchantId", "flbxiaohao"); // 商户号
        params.put("userId", "nihaoma"); // 用户ID  商户系统唯一，不可随机
        params.put("payMethod", "11113"); // 支付通道  联系客服获取
        params.put("money", "10000"); // 金额
        params.put("bizNum", IdUtils.randomId()); // 商户订单号
        params.put("notifyAddress", "http://47.243.237.139:9522/topup/callback/yypay"); // 异步回调地址
        params.put("type", "recharge"); // 'recharge', 固定值
        params.put("name", "lisi"); // 付款人姓名 如支付通道为INR通道必填
        params.put("mobile", "1515203330"); // 付款人手机 如支付通道为INR通道必填
        params.put("email", "649206445@qq.com"); // 付款人邮箱 如支付通道为INR通道必填
        params.put("ip", "49.68.47.163"); // ip
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            if (null != params.get(key)) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }



        String queryStr = sb.substring(0, sb.length() - 1);
        String queryStr1 = sb + "key=12345678";
        System.out.println("签名串:" + queryStr1);

        String sign = SecureUtil.md5(queryStr1);
        System.out.println("获得签名:" + sign);
        queryStr += "&sign=" + sign.toUpperCase(Locale.ROOT);
        String url = "https://api.yypay.pro/pay/order?" + queryStr;
        //String url = "https://api.yypay.pro/pay/order?bizNum=1524328815063601152&merchantId=xiaohao&money=10000&payMethod=11111&type=recharge&userId=9453377119&sign=52DDDB597C11F794FC0992E6F69DF08A";
        HttpRequest request = HttpUtil.createPost(url);
        request.contentType("application/x-www-form-urlencoded");
        System.out.println("请求参数:" + request);
        HttpResponse response = request.execute();
        System.out.println(response.body());
    }

    public static void withdraw() {
        Map<String, Object> params = new TreeMap<>();
        params.put("merchantId", "2218"); // 商户号
        params.put("payMethod", "220010"); // 支付通道  联系客服获取
        params.put("money", "10000"); // 金额
        params.put("bizNum", IdUtils.randomId()); // 商户订单号
        params.put("notifyAddress", "12345"); // 异步回调地址
        params.put("name", null); // 付款人姓名 如支付通道为INR通道必填
        params.put("mobile", null); // 付款人手机 如支付通道为INR通道必填
        params.put("account", "6546116548"); // 付款人邮箱 如支付通道为INR通道必填
        params.put("ifscCode", null); // ip
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            if (null != params.get(key)) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }



        String queryStr = sb.substring(0, sb.length() - 1);
        String queryStr1 = sb + "key=123456789";
        System.out.println("签名串:" + queryStr1);

        String sign = SecureUtil.md5(queryStr1);
        System.out.println("获得签名:" + sign);
        queryStr += "&sign=" + sign.toUpperCase(Locale.ROOT);
        String url = "https://api.yypay.pro/pay/order/paid?" + queryStr;
        HttpRequest request = HttpUtil.createPost(url);
        request.contentType("application/x-www-form-urlencoded");
        System.out.println("请求参数:" + request);
        HttpResponse response = request.execute();
        System.out.println(response.body());
    }
}
