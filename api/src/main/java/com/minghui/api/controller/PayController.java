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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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

        String cusName=StringUtils.length(userName)>=4?StringUtils.substring(userName,StringUtils.length(userName)-4):"xxxx";

        DecimalFormat df = new DecimalFormat("0.00");
        String amount = df.format(Double.parseDouble(request.getAmount()));
        String currency = "USD";
        String coinCode = "USDT";
        String orderId = IdUtils.randomId();;
        String productName = "AMZNWORK";
        String customerId = cusName;
        String notifyUrl = merchant.getTopupNotifyUrl();
        String redirectUrl = "";
        String locale = "en-US";
        String publicKey = "57DAC61F1D6B4B0C8CC19B1728364560";
        String privateKey = merchant.getMerchantKey();

        String signatureStr = amount +
                currency +
                coinCode +
                orderId +
                productName +
                customerId +
                notifyUrl +
                redirectUrl +
                locale +
                publicKey +
                privateKey;
        String sign = SecureUtil.md5(signatureStr).toLowerCase();

        // 调用接口
        Map<String, Object> params = new TreeMap<>();
        params.put("amount", amount); // 订单金额
        params.put("currency", currency); // 订单币种单位。支持：CNY、USD
        params.put("coin_code", coinCode); // 订单支付币种。固定为 USDT
        params.put("order_id", orderId); // 商户端订单号，在通知商户接口时，会带上这个参数

        params.put("product_name", productName); // 商户端产品名称，会显示在官方收银台页面顶部，留空则显示默认值

        params.put("customer_id", customerId); // 商户端用户编号，可以为用户名，也可以为数据库中的用户编号。取用户手机号 后4位
        params.put("notify_url", notifyUrl); // 完成后回调通知地址
        params.put("redirect_url", redirectUrl); // 完成后同步跳转地址
        params.put("locale", locale); // 收银台多语言，中文（zh-CN），英文（en-US），默认为中文
        params.put("public_key", publicKey); // 商户 public key
        params.put("signature", sign); // 签名串，安全校验签名串。
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            if (null != params.get(key)) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }
        String queryStr = sb.substring(0, sb.length() - 1);

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
        payUrl = data.getString("cashier_url");

        BigDecimal realAmount = new BigDecimal(amount);

        if (channel.getPayType().intValue() == 2) {
            // 充U汇率
            String rate = webParamsService.getParamsValue("usdt_exchange_rate");
            realAmount = NumberUtil.mul(realAmount, NumberUtils.toDouble(rate));
        }

        WebTopup topup = new WebTopup();
        topup.setOrderNo(orderId);
        topup.setUserName(userName);
        topup.setAmount(new BigDecimal(data.getString("amount")));
        topup.setRealAmount(realAmount);
        topup.setType(channel.getPayType());
        topup.setPaySign(sign);//未回调时，先保存订单的签名，回调成功后，将被改成USDT转账的hash值
        topup.setPayOrderNo(data.getString("token"));//三方没有订单号，但这个值可以查询订单是否支付完成
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
