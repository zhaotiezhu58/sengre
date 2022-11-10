package com.minghui.api.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.minghui.api.controller.request.PageBaseRequest;
import com.minghui.api.controller.request.PayTopUpRequest;
import com.minghui.api.controller.request.TopUpOrderRequest;
import com.minghui.api.utils.JwtUtils;
import com.minghui.commons.constants.Constant;
import com.minghui.commons.entity.*;
import com.minghui.commons.service.*;
import com.minghui.commons.utils.IdUtils;
import com.minghui.commons.utils.MathUtil;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Api(value = "支付相关", tags = "支付")
@RequestMapping("/pay")
@RestController
public class PayController {

    @Autowired
    private WebUserService webUserService;

    @Autowired
    private WebParamsService webParamsService;

    @Autowired
    private WebPayChannelService webPayChannelService;

    @Autowired
    private WebPayMerchantService webPayMerchantService;

    @Autowired
    private WebTopupService webTopupService;

    @Autowired
    private WebDictService webDictService;

    @ApiOperation(value = "获取法币支付通道")
    @GetMapping("/getPayTypeList")
    public R getPayTypeList(HttpServletRequest httpServletRequest) {
        List<WebPayChannel> list = webPayChannelService.list(
                new QueryWrapper<WebPayChannel>().lambda()
                        .eq(WebPayChannel::getStatus, 1)
                        .eq(WebPayChannel::getChannelType, 1)
                        .eq(WebPayChannel::getPayType, 1)
                        .orderByDesc(WebPayChannel::getPxh)
        );
        JSONArray arr = new JSONArray();
        for (WebPayChannel channel : list) {
            JSONObject obj = new JSONObject();
            obj.put("code", channel.getId());
            obj.put("name", channel.getChannelName());
            obj.put("min", channel.getMinAmount());
            obj.put("max", channel.getMaxAmount());
            arr.add(obj);
        }
        return R.ok().put("data", arr);
    }

    @ApiOperation(value = "获取虚拟货币支付通道")
    @GetMapping("/getCoinChainList")
    public R getCoinChainList(HttpServletRequest httpServletRequest) {
        List<WebPayChannel> list = webPayChannelService.list(
                new QueryWrapper<WebPayChannel>().lambda()
                        .eq(WebPayChannel::getStatus, 1)
                        .eq(WebPayChannel::getChannelType, 1)
                        .eq(WebPayChannel::getPayType, 2)
                        .orderByDesc(WebPayChannel::getPxh)
        );
        JSONArray arr = new JSONArray();
        for (WebPayChannel channel : list) {
            JSONObject obj = new JSONObject();
            obj.put("code", channel.getId());
            obj.put("name", channel.getChannelName());
            obj.put("min", channel.getMinAmount());
            obj.put("max", channel.getMaxAmount());
            arr.add(obj);
        }
        return R.ok().put("data", arr);
    }

    @ApiOperation(value = "充值")
    @GetMapping("/topup")
    public R topup(@Validated PayTopUpRequest request, HttpServletRequest httpServletRequest) {

        String userName = JwtUtils.getUserName(httpServletRequest);
        WebUserEntity user = webUserService.getUser(userName);

        String payUrl = "";

        WebPayChannel channel = webPayChannelService.getById(request.getCode());
        // 验证通道信息
        WebPayMerchant merchant = webPayMerchantService.getOne(
                new QueryWrapper<WebPayMerchant>().lambda()
                        .eq(WebPayMerchant::getMerchantCode, channel.getMerchantCode())
        );

        BigDecimal amount = new BigDecimal(request.getAmount());
        // 调用接口
        Map<String, Object> params = new TreeMap<>();
        params.put("merchantId", merchant.getMerchantCode()); // 商户号
        params.put("userId", userName); // 用户ID  商户系统唯一，不可随机
        params.put("payMethod", channel.getChannelCode()); // 支付通道  联系客服获取
        params.put("money", NumberUtil.mul(amount, 100)); // 金额
        String orderNo = IdUtils.randomId();
        params.put("bizNum", orderNo); // 商户订单号
        params.put("notifyAddress", merchant.getTopupNotifyUrl()); // 异步回调地址
        params.put("type", "recharge"); // 'recharge', 固定值
        params.put("name", null); // 付款人姓名 如支付通道为INR通道必填
        params.put("mobile", null); // 付款人手机 如支付通道为INR通道必填
        params.put("email", null); // 付款人邮箱 如支付通道为INR通道必填
        params.put("ip", ServletUtil.getClientIP(httpServletRequest)); // ip
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            if (null != params.get(key)) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }

        String queryStr = sb.substring(0, sb.length() - 1);
        String queryStr1 = sb + "key=" + merchant.getMerchantKey();
        String sign = SecureUtil.md5(queryStr1);
        queryStr += "&sign=" + sign.toUpperCase(Locale.ROOT);
        String url = merchant.getTopUrl() + "?" + queryStr;
        HttpRequest requestPost = HttpUtil.createPost(url);
        //System.out.println(requestPost);
        requestPost.contentType("application/x-www-form-urlencoded");
        HttpResponse response = requestPost.execute();
        //System.out.println(response.body());
        String body = response.body();
        if (StringUtils.isBlank(body)) {
            return R.error();
        }
        JSONObject obj = JSONObject.parseObject(body);
        Boolean success = obj.getBoolean("success");
        if (!success) {
            return R.error();
        }
        Date date = new Date();
        JSONObject data = obj.getJSONObject("data");
        payUrl = data.getString("url");

        BigDecimal realAmount = amount;
        if (channel.getPayType().intValue() == 2) {
            // 充U汇率
            String rate = webParamsService.getParamsValue("usdt_exchange_rate");
            realAmount = NumberUtil.mul(realAmount, NumberUtils.toDouble(rate));
        }

        WebTopup topup = new WebTopup();
        topup.setOrderNo(orderNo);
        topup.setUserName(userName);
        topup.setAmount(amount);
        topup.setRealAmount(realAmount);
        topup.setType(channel.getPayType());
        topup.setPaySign(sign);
        topup.setPayOrderNo(data.getString("sysBizNum"));
        topup.setPayCurreny(channel.getPayType());
        topup.setIp(ServletUtil.getClientIP(httpServletRequest));
        topup.setMerchantCode(channel.getMerchantCode());
        topup.setChannelCode(channel.getChannelCode());
        topup.setCreateTime(date);
        topup.setModifyTime(date);
        topup.setStatus(1);
        topup.setAgent(user.getAgent());
        topup.setAgentNode(user.getAgentNode());
        topup.setAgentLevel(user.getAgentLevel());
        webTopupService.save(topup);

        return R.ok().put("data", payUrl);
    }

    @ApiOperation(value = "充值订单列表")
    @GetMapping("/order/list")
    public R orderList(TopUpOrderRequest request, HttpServletRequest httpServletRequest) {
        if (!StringUtils.equalsAny(request.getType(), "1", "2")) {
            return R.error();
        }

        List<WebDictEntity> dict = webDictService.list(new QueryWrapper<WebDictEntity>().lambda().eq(WebDictEntity::getStatus, 1).eq(WebDictEntity::getType, 1));
        Map<String, String> dictMap = dict.stream().collect(Collectors.toMap(WebDictEntity::getDictKey, dictEntity -> dictEntity.getDictValue()));

        String userName = JwtUtils.getUserName(httpServletRequest);

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("type", request.getType());
        params.put("userName", userName);

        PageUtils page = webTopupService.queryPage(params);
        List<WebTopup> list = (List<WebTopup>) page.getList();
        if (CollUtil.isNotEmpty(list)) {
            JSONArray arr = new JSONArray();
            for (WebTopup topup : list) {
                JSONObject obj = new JSONObject();
                obj.put("amount", MathUtil.scale(topup.getRealAmount()));
                obj.put("status", topup.getStatus());
                obj.put("createTime", topup.getCreateTime());
                obj.put("typeStr", dictMap.getOrDefault(topup.getStatus().toString(), "-"));
                arr.add(obj);
            }
            page.setList(arr);
        }
        return R.ok().put("page", page);
    }
}
