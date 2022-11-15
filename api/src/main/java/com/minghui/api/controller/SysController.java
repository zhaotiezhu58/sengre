package com.minghui.api.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.extra.mail.MailUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.Producer;
import com.minghui.api.controller.request.PageBaseRequest;
import com.minghui.commons.constants.Constant;
import com.minghui.commons.entity.*;
import com.minghui.commons.service.*;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.R;
import com.minghui.commons.utils.RedisKeyUtil;
import com.sun.mail.util.MailSSLSocketFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.*;

import javax.activation.DataHandler;
import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Api(value = "系统配置相关", tags = "系统配置")
@RequestMapping("/sys")
@RestController
public class SysController {

    @Autowired
    private WebParamsService webParamsService;

    @Autowired
    private Producer producer;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private WebNoticeService webNoticeService;

    @Autowired
    private WebCarouselService webCarouselService;

    @Autowired
    private WebVersionService webVersionService;

    /**
     * 注册验证码
     */
    @ApiOperation(value = "注册验证码")
    @GetMapping("/captcha.jpg")
    public void captcha(HttpServletResponse response, String uuid)throws Exception {
        if (StringUtils.isEmpty(uuid)) {
            throw new Exception();
        }
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        String code = producer.createText();
        BufferedImage image = producer.createImage(code);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);

        redisTemplate.opsForValue().set(RedisKeyUtil.RegKaptchaKey(uuid), code, 5, TimeUnit.MINUTES);
    }

    @ApiOperation(value = "配置列表")
    @GetMapping("/config")
    public R config() throws Exception {
        List<WebParamsEntity> list = webParamsService.getAll();

        Map<String,String> paramsMap = new HashMap<>();
        for (WebParamsEntity params : list) {
            paramsMap.put(params.getParamsKey(), params.getParamsValue());
        }
        //Map<String,String> paramsMap = list.stream().collect(Collectors.toMap(WebParamsEntity::getParamsKey,WebParamsEntity::getParamsValue));

        String enable_home_notice = paramsMap.get("enable_home_notice");

        JSONObject obj = new JSONObject();
        obj.put("inviteUrl", paramsMap.get("invite_url"));
        obj.put("currency", paramsMap.get("currency"));
        obj.put("virtualCurrency", paramsMap.get("virtual_currency"));
        obj.put("countryCode", paramsMap.get("country_code"));
        obj.put("homeNotice", StringUtils.equals(enable_home_notice, "1") ? paramsMap.get("home_notice") : "");
        obj.put("withdrawFee", paramsMap.get("withdraw_fee"));
        obj.put("usdtExchangeRate", paramsMap.get("usdt_exchange_rate"));
        obj.put("oss", paramsMap.get("oss_domain"));
        return R.ok().put("data", obj);
    }


    @ApiOperation(value = "通知列表")
    @GetMapping("/notice/list")
    public R notice(PageBaseRequest request) {

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());

        PageUtils page = webNoticeService.queryPage(params);

        return R.ok().put("page", page);
    }

    @ApiOperation(value = "通知详情")
    @GetMapping("/notice/detail/{id}")
    public R notice(HttpServletRequest httpServletRequest, @PathVariable("id") String id) {
        WebNoticeEntity notice = webNoticeService.getById(id);
        if (notice == null) {
            return R.error();
        }
        return R.ok().put("data", notice);
    }

    @ApiOperation(value = "密码问题列表")
    @GetMapping("/question")
    public R question() {
        String question = webParamsService.getParamsValue("question");

        return R.ok().put("list", JSONObject.parseArray(question));
    }

    @ApiOperation(value = "轮播列表")
    @GetMapping("/carousel/list/{type}")
    public R carouselList(@PathVariable("type") String type) {
        //List<WebCarousel> list = webCarouselService.list(
        //        new QueryWrapper<WebCarousel>().lambda()
        //                .eq(WebCarousel::getStatus, 1)
        //                .eq(WebCarousel::getType, type)
        //);
        List<WebCarousel> list = webCarouselService.getCarouselsByType(NumberUtils.toInt(type));
        JSONArray arr = new JSONArray();
        for (WebCarousel carousel : list) {
            JSONObject obj = new JSONObject();
            obj.put("content", carousel.getContent());
            arr.add(obj);
        }
        return R.ok().put("list", arr);
    }

    @ApiOperation(value = "获取app版本")
    @GetMapping("/version}")
    public R version() {
        WebVersion version = webVersionService.getOne(new QueryWrapper<WebVersion>());
        JSONObject obj = new JSONObject();
        obj.put("version", version.getAppVersion());
        obj.put("url", version.getAppUrl());
        obj.put("index", version.getAppIndex());
        return R.ok().put("data", obj);
    }
}
