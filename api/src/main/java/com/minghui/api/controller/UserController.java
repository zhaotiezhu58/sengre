package com.minghui.api.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.minghui.api.controller.pojo.CommissionDetail;
import com.minghui.api.controller.pojo.CommissionRank;
import com.minghui.api.controller.pojo.TeamCommission;
import com.minghui.api.controller.request.*;
import com.minghui.api.utils.JwtUtils;
import com.minghui.api.utils.PhoneUtil;
import com.minghui.commons.constants.Constant;
import com.minghui.commons.entity.*;
import com.minghui.commons.service.*;
import com.minghui.commons.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Api(value = "用户相关", tags = "用户")
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private WebUserService webUserService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private WebUserLogService webUserLogService;

    @Autowired
    private WebParamsService webParamsService;

    @Autowired
    private WebFundRecordService webFundRecordService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private WebDictService webDictService;

    @Autowired
    private WebLevelService webLevelService;

    @Autowired
    private WebDayReportService webDayReportService;

    @Autowired
    private WebVirtualRecordService webVirtualRecordService;

    @Autowired
    private WebOrderService webOrderService;

    @Autowired
    private WebQuestionService webQuestionService;

    @Autowired
    private WebCommissionRecordService webCommissionRecordService;

    @Autowired
    private WebWithdrawService webWithdrawService;

    @Autowired
    private WebTopupService webTopupService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;


    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public R registerPhone(@Validated RegisterPhoneRequest request, HttpServletRequest httpServletRequest) {

        /** 校验验证码 **/
        String key = RedisKeyUtil.RegKaptchaKey(request.getUuid());
        String yzm = redisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(yzm) || !StringUtils.equals(yzm, request.getVerificationCode())) {
            return R.error(MsgUtil.get("system.pwd.sm.error"));
        }

        String countryCode = webParamsService.getParamsValue("country_code");

        if (StringUtils.equals(request.getType(), "1")) {
            // 校验手机号码格式
            boolean valid = PhoneUtil.validPhone(countryCode, request.getUserName());
            if (!valid) {
                return R.error(MsgUtil.get("system.phone.validerror"));
            }
        } else {
            // 校验邮箱格式
            boolean valid = Validator.isEmail(request.getUserName());
            if (!valid) {
                return R.error(MsgUtil.get("system.phone.validerror"));
            }
        }

        if (!StringUtils.equals(request.getPassword(), request.getConfirmPassword())) {
            return R.error(MsgUtil.get("system.pwd.different"));
        }

        /** 校验注册IP **/
        String clientIP = ServletUtil.getClientIP(httpServletRequest);
        int registerCount = webUserService.count(
                new QueryWrapper<WebUserEntity>().lambda()
                        .eq(WebUserEntity::getRegIp, clientIP)
        );
        int registerConfig = NumberUtils.toInt(webParamsService.getParamsValue("register_ip"), 1);
        if (registerCount >= registerConfig) {
            return R.error();
        }

        /** 校验用户是否存在 **/
        WebUserEntity existUser = webUserService.getOne(
                new QueryWrapper<WebUserEntity>().lambda()
                        .eq(WebUserEntity::getUserName, request.getUserName())
        );
        if (existUser != null) {
            return R.error(MsgUtil.get("system.register.has"));
        }

        /** 查询代理 **/
        //String default_invitecode = webParamsService.getParamsValue("default_invitecode");
        WebUserEntity agentUser = webUserService.getOne(
                new QueryWrapper<WebUserEntity>().lambda()
                        .eq(WebUserEntity::getInviteCode, request.getInviteCode())
        );
        //if (agentUser == null) {
        //    agentUser = webUserService.getOne(
        //            new QueryWrapper<WebUserEntity>().lambda()
        //                    .eq(WebUserEntity::getId, default_invitecode)
        //    );
        //}
        if (agentUser == null) {
            return R.error(MsgUtil.get("system.register.yqm.error"));
        }

        Date now = new Date();
        WebUserEntity user = new WebUserEntity();
        user.setAreaCode(countryCode);
        user.setUserName(request.getUserName());
        user.setPhone(StringUtils.equals(request.getType(), "1") ? request.getUserName() : null);
        user.setEmail(StringUtils.equals(request.getType(), "2") ? request.getUserName() : null);
        user.setBalance(new BigDecimal("0"));
        user.setVirtualBalance(new BigDecimal("0"));
        user.setUnlockVirtualBalance(new BigDecimal("0"));
        user.setLoginPwd(SecureUtil.md5(request.getPassword()));
        user.setInviteCode(RandomUtil.randomString(6));
        user.setStatus(1);
        user.setAgent(agentUser.getUserName());
        user.setAgentNode(agentUser.getAgentNode() + user.getUserName() + "|");
        user.setAgentLevel(agentUser.getAgentLevel() + 1);
        user.setRegTime(now);
        user.setRegIp(clientIP);
        user.setLoginTime(null);
        user.setLoginIp(null);
        user.setRemake(null);

        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try{
            // 注册赠送彩金
            String registerAmountStr = webParamsService.getParamsValue("register_amount");
            BigDecimal amount = new BigDecimal(registerAmountStr);
            if (amount.doubleValue() > 0) {

                WebFundRecordEntity fund = new WebFundRecordEntity();
                fund.setUserName(user.getUserName());
                fund.setSerialNo(IdUtils.randomId());
                fund.setRefBillNo(null);
                fund.setAmount(amount);
                fund.setBeforeAmount(user.getBalance());
                fund.setAfterAmount(NumberUtil.add(amount, user.getBalance()));
                fund.setType(Constant.FundType.PLAT.getValue());
                fund.setCreateTime(now);
                fund.setAgent(user.getUserName());
                fund.setAgentNode(user.getAgentNode());
                fund.setAgentLevel(user.getAgentLevel());
                webFundRecordService.save(fund);

                user.setBalance(amount);
            }
            webUserService.save(user);
            transactionManager.commit(transactionStatus);
            redisTemplate.delete(key);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(transactionStatus);
        }
        return R.error();
    }

    @Transactional
    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public R loginPhone(@Validated LoginPhoneRequest pwdLoginRequest, HttpServletRequest request) throws Exception {
        String countryCode = webParamsService.getParamsValue("country_code");

        if (StringUtils.equals(pwdLoginRequest.getType(), "1")) {
            // 校验手机号码格式
            boolean valid = PhoneUtil.validPhone(countryCode, pwdLoginRequest.getUserName());
            if (!valid) {
                return R.error(MsgUtil.get("system.phone.validerror"));
            }
        } else {
            // 校验邮箱格式
            boolean valid = Validator.isEmail(pwdLoginRequest.getUserName());
            if (!valid) {
                return R.error(MsgUtil.get("system.phone.validerror"));
            }
        }

        String incKey = RedisKeyUtil.LoginPwdErrorKey(pwdLoginRequest.getUserName());
        /** 每日错误次数上限 **/
        String dayCount = redisTemplate.opsForValue().get(incKey);
        int count = NumberUtils.toInt(dayCount, 0);

        String pwdErrCount = webParamsService.getParamsValue("usr_login_pwd_err_count");
        if (count > NumberUtils.toInt(pwdErrCount)) {
            return R.error(MsgUtil.get("system.user.login.pwd.limit"));
        }

        String pwd = SecureUtil.md5(pwdLoginRequest.getPassword());
        WebUserEntity existUser = webUserService.getOne(
                new QueryWrapper<WebUserEntity>().lambda()
                        .eq(WebUserEntity::getUserName, pwdLoginRequest.getUserName())

        );
        if (existUser == null) {
            return R.error(MsgUtil.get("system.phone.not.reg"));
        }
        if (existUser.getStatus().intValue() == 0) {
            return R.error(MsgUtil.get("system.account.enable"));
        }
        if (!StringUtils.equals(pwd, existUser.getLoginPwd())) {
            /** 累计密码错误 **/
            redisTemplate.opsForValue().increment(incKey);
            redisTemplate.expire(incKey, 1, TimeUnit.DAYS);
            return R.error(MsgUtil.get("system.user.login.pwd.error"));
        }

        String clientIP = ServletUtil.getClientIP(request);

        /** 保存token **/
        Map<String, String> map = new HashMap<>();
        map.put("userName", existUser.getUserName());
        map.put("userIp", clientIP);
        map.put("random", RandomUtil.randomString(6));
        String token = JwtUtils.getToken(map);
        redisTemplate.opsForValue().set(RedisKeyUtil.UserTokenKey(existUser.getUserName()), token, 15, TimeUnit.MINUTES);

        Date now = new Date();
        /** 更新最后登录时间 **/
        webUserService.update(
                new UpdateWrapper<WebUserEntity>().lambda()
                        .eq(WebUserEntity::getId, existUser.getId())
                        .set(WebUserEntity::getLoginIp, clientIP)
                        .set(WebUserEntity::getLoginTime, now)
        );
        /** 删除密码输入错误次数 **/
        redisTemplate.delete(incKey);

        /** 登录日志 **/
        WebUserLogEntity log = new WebUserLogEntity();
        log.setUserPhone(existUser.getUserName());
        log.setLoginIp(clientIP);
        log.setCreateTime(now);
        webUserLogService.save(log);

        /** 解锁虚拟货币 **/
        if (existUser.getVirtualBalance().doubleValue() >= 1) {
            String yyyyMMdd = DateUtil.format(now, "yyyyMMdd");
            Boolean unlock = redisTemplate.opsForValue().setIfAbsent("user:virtual:" + yyyyMMdd + ":" + existUser.getUserName(), "unlock", 1, TimeUnit.DAYS);
            if (unlock) {
                BigDecimal unlockAmount = MathUtil.scale(NumberUtil.mul(existUser.getVirtualBalance(), 0.01));
                if (unlockAmount.doubleValue() >= 1) {
                    webUserService.unlockVirtualBalance(existUser.getUserName(), unlockAmount);

                    //
                    WebVirtualRecordEntity virtual = new WebVirtualRecordEntity();
                    virtual.setUserName(existUser.getUserName());
                    virtual.setSerialNo(IdUtils.randomId());
                    virtual.setRefBillNo(null);
                    virtual.setAmount(unlockAmount.negate());
                    virtual.setBeforeAmount(existUser.getVirtualBalance());
                    virtual.setAfterAmount(NumberUtil.sub(existUser.getVirtualBalance(), unlockAmount));
                    virtual.setType(Constant.VirtualType.UNLOCK.getValue());
                    virtual.setCreateTime(now);
                    virtual.setAgent(existUser.getAgent());
                    virtual.setAgentNode(existUser.getAgentNode());
                    virtual.setAgentLevel(existUser.getAgentLevel());
                    webVirtualRecordService.save(virtual);
                }
            }
        }

        return R.ok(MsgUtil.get("system.user.login.pwd.success")).put("token", token);
    }

    @ApiOperation(value = "用户信息")
    @GetMapping("/info")
    public R info(HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);

        WebUserEntity user = webUserService.getUser(userName);

        String inviteUrl = webParamsService.getParamsValue("invite_url");
        JSONObject obj = new JSONObject();
        obj.put("userName", user.getUserName());
        obj.put("inviteUrl", inviteUrl);
        obj.put("inviteCode", user.getInviteCode());
        obj.put("balance", MathUtil.scale(user.getBalance()));
        obj.put("virtualBalance", MathUtil.scale(user.getVirtualBalance()));
        obj.put("unlockVirtualBalance", MathUtil.scale(user.getUnlockVirtualBalance()));

        // 等级
        WebLevelEntity level = webLevelService.getUserCurrLevel(webLevelService.getLevelsByType(1), user.getBalance());
        //WebLevelEntity level = webLevelService.getOne(
        //        new QueryWrapper<WebLevelEntity>().lambda()
        //                .eq(WebLevelEntity::getLevelType, 1)
        //                .le(WebLevelEntity::getMinBalance, user.getBalance())
        //                .ge(WebLevelEntity::getMaxBalance, user.getBalance())
        //                .orderByDesc(WebLevelEntity::getLevelValue)
        //);
        obj.put("level",
                JSONUtil.createObj().set("levelName", level.getLevelName())
                        .set("levelValue", level.getLevelValue())
                        .set("friend1", level.getAgent1())
                        .set("friend2", level.getAgent2())
                        .set("friend3", level.getAgent3())
        );
        // 总收益
        //List<WebDayReportEntity> list = webDayReportService.list(
        //        new QueryWrapper<WebDayReportEntity>().lambda()
        //                .eq(WebDayReportEntity::getUserName, user.getUserName())
        //                .eq(WebDayReportEntity::getToDay, new Date())
        //);
        // 今天收入代币
        List<WebDayReportEntity> todayReport = webDayReportService.list(
                new QueryWrapper<WebDayReportEntity>().lambda()
                        .eq(WebDayReportEntity::getToDay, DateUtil.today())
                        .eq(WebDayReportEntity::getUserName, user.getUserName())
        );
        double todayVirtual = todayReport.stream().mapToDouble(order -> order.getVirtualIncome().doubleValue()).sum();
        obj.put("todayVirtual", MathUtil.scale(todayVirtual));

        double totalIncome = todayReport.stream().mapToDouble(order -> order.getInCome().doubleValue()).sum();
        obj.put("totalIncome", MathUtil.scale(totalIncome));

        WebQuestionEntity question = webQuestionService.getOne(new QueryWrapper<WebQuestionEntity>().lambda().eq(WebQuestionEntity::getUserName, userName));
        obj.put("setQuestion", question != null);

        String agentTelegramStr = webParamsService.getParamsValue("agent_telegram");
        JSONObject jsonObject = JSONObject.parseObject(agentTelegramStr);
        String agentTelegram = jsonObject.getString("default");

        String[] userNode = user.getAgentNode().split("\\|");
        for (String s : userNode) {
            String string = jsonObject.getString(s);
            if (StringUtils.isNotEmpty(string)) {
                agentTelegram = string;
                break;
            }
        }
        obj.put("serviceLink", agentTelegram);

        return R.ok().put("data", obj);
    }


    @ApiOperation(value = "代币流动列表")
    @GetMapping("/virtualRecord/list")
    public R virtualRecordList(PageBaseRequest request, HttpServletRequest httpServletRequest) {

        //List<WebDictEntity> dict = webDictService.list(new QueryWrapper<WebDictEntity>().lambda().eq(WebDictEntity::getStatus, 1).eq(WebDictEntity::getType, 4));

        List<WebDictEntity> dict = webDictService.getAll();
        Map<String, String> dictMap = dict.stream().filter(temp -> temp.getType().intValue() == 4).collect(Collectors.toMap(WebDictEntity::getDictKey, dictEntity -> dictEntity.getDictValue()));

        String userName = JwtUtils.getUserName(httpServletRequest);

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("userName", userName);

        PageUtils page = webVirtualRecordService.queryPage(params);
        List<WebVirtualRecordEntity> list = (List<WebVirtualRecordEntity>) page.getList();
        if (CollUtil.isNotEmpty(list)) {
            JSONArray arr = new JSONArray();
            for (WebVirtualRecordEntity fund : list) {
                JSONObject obj = new JSONObject();
                obj.put("amount", fund.getAmount());
                obj.put("type", fund.getType());
                obj.put("createTime", fund.getCreateTime());
                obj.put("typeStr", dictMap.getOrDefault(fund.getType().toString(), "-"));
                arr.add(obj);
            }
            page.setList(arr);
        }
        return R.ok().put("page", page);
    }

    @Transactional
    @ApiOperation(value = "收集代币")
    @GetMapping("/virtual/collect")
    public R virtualCollect(HttpServletRequest httpServletRequest) throws Exception {
        String userName = JwtUtils.getUserName(httpServletRequest);
        return R.error("Para el tiempo de cambio de monedas, preste atención a nuestro tablón de anuncios.");
        //WebUserEntity user = webUserService.getUser(userName);
        //// 收集代币
        //webUserService.collectVirtualBalance(user.getUserName(), user.getUnlockVirtualBalance());
        //// 流水记录
        //WebFundRecordEntity fund = new WebFundRecordEntity();
        //fund.setUserName(user.getUserName());
        //fund.setSerialNo(IdUtils.randomId());
        //fund.setRefBillNo(null);
        //fund.setAmount(user.getUnlockVirtualBalance());
        //fund.setBeforeAmount(user.getBalance());
        //fund.setAfterAmount(NumberUtil.add(user.getBalance(), user.getUnlockVirtualBalance()));
        //fund.setType(Constant.FundType.MINING.getValue());
        //fund.setCreateTime(new Date());
        //fund.setAgent(user.getAgent());
        //fund.setAgentNode(user.getAgentNode());
        //fund.setAgentLevel(user.getAgentLevel());
        //webFundRecordService.save(fund);
        //return R.ok();
    }

    @ApiOperation(value = "资金流动列表")
    @GetMapping("/fundRecord/list")
    public R fundRecordList(PageBaseRequest request, HttpServletRequest httpServletRequest) {

        List<WebDictEntity> dict = webDictService.getAll();

        //List<WebDictEntity> dict = webDictService.list(new QueryWrapper<WebDictEntity>().lambda().eq(WebDictEntity::getStatus, 1).eq(WebDictEntity::getType, 3));
        Map<String, String> dictMap = dict.stream().filter(temp -> temp.getType().intValue() == 3).collect(Collectors.toMap(WebDictEntity::getDictKey, dictEntity -> dictEntity.getDictValue()));

        String userName = JwtUtils.getUserName(httpServletRequest);

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("userName", userName);

        PageUtils page = webFundRecordService.queryPage(params);
        List<WebFundRecordEntity> list = (List<WebFundRecordEntity>) page.getList();
        if (CollUtil.isNotEmpty(list)) {
            JSONArray arr = new JSONArray();
            for (WebFundRecordEntity fund : list) {
                JSONObject obj = new JSONObject();
                obj.put("amount", fund.getAmount());
                obj.put("type", fund.getType());
                obj.put("createTime", fund.getCreateTime());
                obj.put("typeStr", dictMap.getOrDefault(fund.getType().toString(), "-"));
                arr.add(obj);
            }
            page.setList(arr);
        }
        return R.ok().put("page", page);
    }

    @ApiOperation(value = "历史订单列表")
    @GetMapping("/orderHistory/list")
    public R orderHistoryList(PageBaseRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        WebUserEntity user = webUserService.getUser(userName);

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("userName", userName);

        PageUtils page = webOrderService.queryPage(params);
        List<WebOrderEntity> list = (List<WebOrderEntity>) page.getList();
        if (CollUtil.isNotEmpty(list)) {
            JSONArray arr = new JSONArray();
            for (WebOrderEntity order : list) {
                JSONObject obj = new JSONObject();
                obj.put("amount", order.getAmount());
                obj.put("income", order.getIncome());
                obj.put("orderNo", order.getOrderNo());
                obj.put("virtualIncome", order.getVirtualIncome());
                obj.put("time", order.getCreateTime());
                arr.add(obj);
            }
            page.setList(arr);
        }

        Date now = new Date();
        int betCount = webOrderService.count(
                new QueryWrapper<WebOrderEntity>().lambda()
                        .eq(WebOrderEntity::getUserName, user.getUserName())
                        .between(WebOrderEntity::getCreateTime, DateUtil.beginOfDay(now), DateUtil.endOfDay(now))
        );

        WebLevelEntity level = webLevelService.getUserCurrLevel(webLevelService.getLevelsByType(1), user.getBalance());
        //WebLevelEntity level = webLevelService.getOne(
        //        new QueryWrapper<WebLevelEntity>().lambda()
        //                .eq(WebLevelEntity::getLevelType, 1)
        //                .le(WebLevelEntity::getMinBalance, user.getBalance())
        //                .ge(WebLevelEntity::getMaxBalance, user.getBalance())
        //                .orderByDesc(WebLevelEntity::getLevelValue)
        //);

        return R.ok().put("page", page).put("balance", MathUtil.scale(user.getBalance())).put("orderOfToday", betCount).put("count", level.getDayCount());
    }


    @ApiOperation(value = "会员等级列表")
    @GetMapping("/level/list")
    public R levelList(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        WebUserEntity user = webUserService.getUser(userName);

        //List<WebLevelEntity> levels = webLevelService.list(new QueryWrapper<WebLevelEntity>().lambda().eq(WebLevelEntity::getLevelType, 1).eq(WebLevelEntity::getStatus, 1).orderByAsc(WebLevelEntity::getLevelValue));
        List<WebLevelEntity> levels = webLevelService.getLevelsByType(1);
        JSONArray arr = new JSONArray();

        for (WebLevelEntity level : levels) {
            if (level.getLevelValue() == 0) {
                continue;
            }
            JSONObject obj = new JSONObject();
            obj.put("levelName", level.getLevelName());
            obj.put("levelValue", level.getLevelValue());
            obj.put("level1", level.getAgent1());
            obj.put("level2", level.getAgent2());
            obj.put("level3", level.getAgent3());
            obj.put("minTopUpBalance", level.getMinBalance());
            double missing = 0;
            if (user.getBalance().doubleValue() < level.getMinBalance().doubleValue()) {
                missing = NumberUtil.sub(level.getMinBalance(), user.getBalance()).doubleValue();
            }
            obj.put("missing", MathUtil.scale(missing));
            arr.add(obj);
        }

        WebLevelEntity level = webLevelService.getUserCurrLevel(webLevelService.getLevelsByType(1), user.getBalance());
        //WebLevelEntity level = webLevelService.getOne(
        //        new QueryWrapper<WebLevelEntity>().lambda()
        //                .eq(WebLevelEntity::getLevelType, 1)
        //                .le(WebLevelEntity::getMinBalance, user.getBalance())
        //                .ge(WebLevelEntity::getMaxBalance, user.getBalance())
        //                .orderByDesc(WebLevelEntity::getLevelValue)
        //);

        return R.ok().put("balance", MathUtil.scale(user.getBalance())).put("levels", arr).put("currLevel", level.getLevelValue());
    }

    @ApiOperation(value = "设置密码问题")
    @PostMapping("/question/set")
    public R questionSet(@Validated QuestionSetRequest request, HttpServletRequest httpServletRequest) {
        if (StringUtils.isEmpty(request.getAnswer())) {
            return R.error();
        }
        String userName = JwtUtils.getUserName(httpServletRequest);

        int count = webQuestionService.count(new QueryWrapper<WebQuestionEntity>().lambda().eq(WebQuestionEntity::getUserName, userName));
        if (count == 0) {
            String questionArr = webParamsService.getParamsValue("question");
            JSONArray arr = JSONObject.parseArray(questionArr);

            boolean hasQuestion = false;
            for (Object o : arr) {
                JSONObject obj = (JSONObject) o;
                if (obj.getIntValue("id") == Integer.parseInt(request.getId())) {
                    hasQuestion  = true;
                    break;
                }
            }

            if (hasQuestion) {
                WebQuestionEntity question = new WebQuestionEntity();
                question.setUserName(userName);
                question.setQuestionId(request.getId());
                question.setAnswer(HtmlUtil.escape(request.getAnswer()));
                question.setCreateTime(new Date());
                webQuestionService.save(question);
                return R.ok();
            }
        }
        return R.error();
    }


    @ApiOperation(value = "重置密码")
    @PostMapping("/password/reset")
    public R passwordReset(@Validated PasswordSetRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        if (!StringUtils.equals(request.getPassword(), request.getConfirmPassword())) {
            return R.error(MsgUtil.get("system.pwd.different"));
        }

        WebQuestionEntity question = webQuestionService.getOne(new QueryWrapper<WebQuestionEntity>().lambda().eq(WebQuestionEntity::getUserName, userName));
        if (question == null) {
            return R.error();
        }

        if (!StringUtils.equals(request.getAnswer(), question.getAnswer())) {
            return R.error(MsgUtil.get("error.10001"));
        }

        webUserService.update(
                new UpdateWrapper<WebUserEntity>().lambda()
                        .eq(WebUserEntity::getUserName, userName)
                        .set(WebUserEntity::getLoginPwd, SecureUtil.md5(request.getConfirmPassword()))
        );
        return R.ok();
    }

    @ApiOperation(value = "好友返佣明细列表")
    @GetMapping("/commission/list")
    public R commissionList(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        List<WebCommissionRecordEntity> list = webCommissionRecordService.list(
                new QueryWrapper<WebCommissionRecordEntity>().lambda()
                        .eq(WebCommissionRecordEntity::getCommissionUser, userName)
                        .select(WebCommissionRecordEntity::getCommission, WebCommissionRecordEntity::getUserName)
        );

        Map<String,Double> map = new HashMap<>();
        for (WebCommissionRecordEntity record : list) {
            Double commission = map.getOrDefault(record.getUserName(), 0D);
            if (commission == 0) {
                map.put(record.getUserName(), record.getCommission().doubleValue());
            } else {
                map.put(record.getUserName(), record.getCommission().doubleValue() + commission);
            }
        }
        List<CommissionRank> ranks = new ArrayList<>();
        for (String s : map.keySet()) {
            CommissionRank rank = new CommissionRank();
            rank.setFriendName(s);
            rank.setIncome(MathUtil.scale(map.get(s)));
            ranks.add(rank);
        }
        ranks.sort(Comparator.comparing(CommissionRank::getIncome).reversed());
        return R.ok().put("list", ranks);
    }

    @ApiOperation(value = "排行榜(周榜)")
    @GetMapping("/commission/rank/week")
    public R commissionRankWeek(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        Date now = new Date();

        QueryWrapper<WebCommissionRecordEntity> query = new QueryWrapper<>();
        query.eq("commission_user", userName);
        query.between("create_time", DateUtil.offsetWeek(now, -1), now);
        query.select("sum(commission) as commission, user_name");
        query.groupBy("user_name");
        query.orderByDesc("commission");
        query.last("limit 10");
        List<WebCommissionRecordEntity> list = webCommissionRecordService.list(query);

        List<CommissionRank> ranks = new ArrayList<>();
        if (CollUtil.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                WebCommissionRecordEntity commission = list.get(i);
                CommissionRank rank = new CommissionRank();
                rank.setFriendName(commission.getUserName());
                rank.setIncome(commission.getCommission());
                ranks.add(rank);
            }
        }

        //List<WebCommissionRecordEntity> list = webCommissionRecordService.list(
        //        new QueryWrapper<WebCommissionRecordEntity>().lambda()
        //                .eq(WebCommissionRecordEntity::getCommissionUser, userName)
        //                .between(WebCommissionRecordEntity::getCreateTime, DateUtil.offsetWeek(now, -1), now)
        //                .select(WebCommissionRecordEntity::getCommission, WebCommissionRecordEntity::getUserName)
        //);
        //
        //Map<String,Double> map = new HashMap<>();
        //for (WebCommissionRecordEntity record : list) {
        //    Double commission = map.getOrDefault(record.getUserName(), 0D);
        //    if (commission == 0) {
        //        map.put(record.getUserName(), record.getCommission().doubleValue());
        //    } else {
        //        map.put(record.getUserName(), record.getCommission().doubleValue() + commission);
        //    }
        //}
        //List<CommissionRank> ranks = new ArrayList<>();
        //for (String s : map.keySet()) {
        //    CommissionRank rank = new CommissionRank();
        //    rank.setFriendName(s);
        //    rank.setIncome(MathUtil.scale(map.get(s)));
        //    ranks.add(rank);
        //}
        //ranks.sort(Comparator.comparing(CommissionRank::getIncome).reversed());
        return R.ok().put("list", ranks);
    }

    @ApiOperation(value = "排行榜(月榜)")
    @GetMapping("/commission/rank/month")
    public R commissionRankMonth(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        Date now = new Date();

        QueryWrapper<WebCommissionRecordEntity> query = new QueryWrapper<>();
        query.eq("commission_user", userName);
        query.between("create_time", DateUtil.offsetMonth(now, -1), now);
        query.select("sum(commission) as commission, user_name");
        query.groupBy("user_name");
        query.orderByDesc("commission");
        query.last("limit 10");
        List<WebCommissionRecordEntity> list = webCommissionRecordService.list(query);

        List<CommissionRank> ranks = new ArrayList<>();
        if (CollUtil.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                WebCommissionRecordEntity commission = list.get(i);
                CommissionRank rank = new CommissionRank();
                rank.setFriendName(commission.getUserName());
                rank.setIncome(commission.getCommission());
                ranks.add(rank);
            }
        }

        //List<WebCommissionRecordEntity> list = webCommissionRecordService.list(
        //        new QueryWrapper<WebCommissionRecordEntity>().lambda()
        //                .eq(WebCommissionRecordEntity::getCommissionUser, userName)
        //                .between(WebCommissionRecordEntity::getCreateTime, DateUtil.offsetMonth(now, -1), now)
        //                .select(WebCommissionRecordEntity::getCommission, WebCommissionRecordEntity::getUserName)
        //);
        //
        //Map<String,Double> map = new HashMap<>();
        //for (WebCommissionRecordEntity record : list) {
        //    Double commission = map.getOrDefault(record.getUserName(), 0D);
        //    if (commission == 0) {
        //        map.put(record.getUserName(), record.getCommission().doubleValue());
        //    } else {
        //        map.put(record.getUserName(), record.getCommission().doubleValue() + commission);
        //    }
        //}
        //for (String s : map.keySet()) {
        //    CommissionRank rank = new CommissionRank();
        //    rank.setFriendName(s);
        //    rank.setIncome(MathUtil.scale(map.get(s)));
        //    ranks.add(rank);
        //}

        return R.ok().put("list", ranks);
    }

    @ApiOperation(value = "团队数据")
    @GetMapping("/team")
    public R team(HttpServletRequest httpServletRequest) throws Exception{
        String userName = JwtUtils.getUserName(httpServletRequest);

        WebUserEntity user = webUserService.getUser(userName);
        // 获取团队人数
        List<WebUserEntity> users = webUserService.list(
                new QueryWrapper<WebUserEntity>().lambda()
                        .like(WebUserEntity::getAgentNode, "|" + userName + "|")
                        .in(WebUserEntity::getAgentLevel, Arrays.asList(user.getAgentLevel() + 1, user.getAgentLevel() + 2, user.getAgentLevel() + 3))
                        .select(WebUserEntity::getUserName,WebUserEntity::getAgentLevel)
        );
        int teamSize = users.size();

        List<WebCommissionRecordEntity> commissionList = webCommissionRecordService.list(
                new QueryWrapper<WebCommissionRecordEntity>().lambda()
                        .eq(WebCommissionRecordEntity::getCommissionUser, userName)
                        .select(WebCommissionRecordEntity::getCommission,WebCommissionRecordEntity::getAgentLevel)
        );
        // 总返点
        double teamCommission = commissionList.stream().mapToDouble(order -> order.getCommission().doubleValue()).sum();

        // 用户等级
        WebLevelEntity level = webLevelService.getUserCurrLevel(webLevelService.getLevelsByType(1), user.getBalance());

        List<TeamCommission> teams = new ArrayList<>();

        CompletableFuture<TeamCommission> Future1 = CompletableFuture.supplyAsync(() -> {
            TeamCommission team1 = new TeamCommission();
            List<WebUserEntity> userLv1 = users.stream().filter(u -> (u.getAgentLevel() == user.getAgentLevel() + 1)).collect(Collectors.toList());
            team1.setPeople(userLv1.size());
            // 1级返点
            List<WebCommissionRecordEntity> commissionLv1 = commissionList.stream().filter(commission -> (commission.getAgentLevel() == (user.getAgentLevel() + 1))).collect(Collectors.toList());
            double commission1 = commissionLv1.stream().mapToDouble(order -> order.getCommission().doubleValue()).sum();
            team1.setCommission(MathUtil.scale(commission1));
            team1.setRate(level.getAgent1());
            return team1;
        }, threadPoolExecutor);

        CompletableFuture<TeamCommission> Future2 = CompletableFuture.supplyAsync(() -> {
            // 2级返点
            List<WebUserEntity> userLv2 = users.stream().filter(u -> (u.getAgentLevel() == user.getAgentLevel() + 2)).collect(Collectors.toList());
            List<WebCommissionRecordEntity> commissionLv2 = commissionList.stream().filter(commission -> (commission.getAgentLevel() == (user.getAgentLevel() + 2))).collect(Collectors.toList());
            double commission2 = commissionLv2.stream().mapToDouble(order -> order.getCommission().doubleValue()).sum();
            TeamCommission team2 = new TeamCommission();
            team2.setPeople(userLv2.size());
            team2.setCommission(MathUtil.scale(commission2));
            team2.setRate(level.getAgent2());
            return team2;
        }, threadPoolExecutor);

        CompletableFuture<TeamCommission> Future3 = CompletableFuture.supplyAsync(() -> {
            // 3级返点
            List<WebUserEntity> userLv3 = users.stream().filter(u -> (u.getAgentLevel() == user.getAgentLevel() + 3)).collect(Collectors.toList());
            List<WebCommissionRecordEntity> commissionLv3 = commissionList.stream().filter(commission -> (commission.getAgentLevel() == (user.getAgentLevel() + 3))).collect(Collectors.toList());
            double commission3 = commissionLv3.stream().mapToDouble(order -> order.getCommission().doubleValue()).sum();
            TeamCommission team3 = new TeamCommission();
            team3.setPeople(userLv3.size());
            team3.setCommission(MathUtil.scale(commission3));
            team3.setRate(level.getAgent3());
            return team3;
        }, threadPoolExecutor);

        CompletableFuture.allOf(Future1,Future2,Future3).get();

        teams.add(Future1.get());
        teams.add(Future2.get());
        teams.add(Future3.get());

        String invite_url = webParamsService.getParamsValue("invite_url");
        JSONObject obj = new JSONObject();
        obj.put("teamSize", teamSize);
        obj.put("teamCommission", MathUtil.scale(teamCommission));
        obj.put("inviteUrl", invite_url);
        obj.put("inviteCode", user.getInviteCode());

        obj.put("teams", teams);
        return R.ok().put("data", obj);
    }

    @ApiOperation(value = "团队数据(层级数据)")
    @GetMapping("/team/level/{level}")
    public R team(HttpServletRequest httpServletRequest, @PathVariable("level") String levelStr) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        if (StringUtils.isBlank(levelStr) || !StringUtils.equalsAny(levelStr, "1", "2", "3")) {
            return R.error();
        }

        int level = NumberUtils.toInt(levelStr);
        WebUserEntity user = webUserService.getUser(userName);
        // 获取团队人数
        int teamSize = webUserService.count(
                new QueryWrapper<WebUserEntity>().lambda()
                        .like(WebUserEntity::getAgentNode, "|" + userName + "|")
                        .eq(WebUserEntity::getAgentLevel, user.getAgentLevel() + level));
        //int teamSize = users.size();

        QueryWrapper<WebCommissionRecordEntity> query = new QueryWrapper<>();
        query.eq("commission_user", userName);
        query.eq("agent_level", user.getAgentLevel() + level);
        query.select("sum(commission) as commission, user_name");
        query.groupBy("user_name");
        query.orderByDesc("commission");
        List<WebCommissionRecordEntity> commissionList = webCommissionRecordService.list(query);

        //List<WebCommissionRecordEntity> commissionList = webCommissionRecordService.list(
        //        new QueryWrapper<WebCommissionRecordEntity>().lambda()
        //                .eq(WebCommissionRecordEntity::getCommissionUser, userName)
        //                .eq(WebCommissionRecordEntity::getAgentLevel, user.getAgentLevel() + level)
        //);
        // 总返点
        double teamCommission = commissionList.stream().mapToDouble(order -> order.getCommission().doubleValue()).sum();

        List<CommissionDetail> details = new ArrayList<>();
        if (CollUtil.isNotEmpty(commissionList)) {
            //Map<String, Double> map = new HashMap<>();
            //for (WebCommissionRecordEntity commissionRecord : commissionList) {
            //    Double commission = map.getOrDefault(commissionRecord.getUserName(), 0D);
            //    map.put(commissionRecord.getUserName(), NumberUtil.add(commission.doubleValue(), commissionRecord.getCommission().doubleValue()));
            //}
            //for (String s : map.keySet()) {
            //    CommissionDetail detail = new CommissionDetail();
            //    detail.setFriendName(s);
            //    detail.setCommission(MathUtil.scale(map.get(s)));
            //    details.add(detail);
            //}
            //details.sort(Comparator.comparing(CommissionDetail::getCommission).reversed());

            for (WebCommissionRecordEntity entity : commissionList) {
                CommissionDetail detail = new CommissionDetail();
                detail.setFriendName(entity.getUserName());
                detail.setCommission(entity.getCommission());
                details.add(detail);
            }
        }
        JSONObject obj = new JSONObject();
        obj.put("teamSize", teamSize);
        obj.put("teamCommission", MathUtil.scale(teamCommission));
        obj.put("list", details);
        return R.ok().put("data", obj);
    }


    @Transactional
    @ApiOperation(value = "提币")
    @PostMapping("/withdraw")
    public R withdraw(@Validated WithdrawRequest request,HttpServletRequest httpServletRequest) throws Exception {
        Date now = new Date();
        if (!StringUtils.equalsAny(request.getType(), "1", "2")) {
            return R.error();
        }
        if (StringUtils.equals(request.getType(), "1") && StringUtils.isBlank(request.getRealName())) {
            return R.error();
        }

        String userName = JwtUtils.getUserName(httpServletRequest);
        BigDecimal money = new BigDecimal(request.getMoney());

        String withdrawal_blacklist = webParamsService.getParamsValue("withdrawal_blacklist");
        List<String> blacks = Arrays.asList(withdrawal_blacklist.split(","));
        if (blacks.contains(userName)) {
            return R.error();
        }

        String withdrawMinAmountStr = webParamsService.getParamsValue("withdraw_min_amount");
        if (money.doubleValue() < NumberUtils.toDouble(withdrawMinAmountStr)) {
            String s = MsgUtil.get("system.withdraw.minamount");
            return R.error(StrUtil.format(s, withdrawMinAmountStr));
        }

        // 查询是否有未完成的订单
        int count = webWithdrawService.count(new QueryWrapper<WebWithdraw>().lambda().eq(WebWithdraw::getUserName, userName).in(WebWithdraw::getStatus, Arrays.asList(0, -2)));
        if (count > 0) {
            return R.error(MsgUtil.get("error.20002"));
        }

        String withdrawCountStr = webParamsService.getParamsValue("withdraw_count");
        // 查询今日订单
        int dayCount = webWithdrawService.count(
                new QueryWrapper<WebWithdraw>().lambda()
                        .eq(WebWithdraw::getUserName, userName)
                        .in(WebWithdraw::getStatus, Arrays.asList(2, -1))
                        .between(WebWithdraw::getCreateTime, DateUtil.beginOfDay(now), DateUtil.endOfDay(now))
        );
        if (dayCount >= NumberUtils.toInt(withdrawCountStr, 3)) {
            return R.error(MsgUtil.get("error.20003"));
        }

        //SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getTgName, userName));
        //if (sysUser != null) {
        //    return R.error();
        //}


        WebUserEntity user = webUserService.getUser(userName);
        // 余额是否充足
        if (user.getBalance().doubleValue() < money.doubleValue()) {
            return R.error(MsgUtil.get("system.order.account.balance"));
        }

        // 手续费百分比
        Double withdrawFee = NumberUtils.toDouble(webParamsService.getParamsValue("withdraw_fee"));
        BigDecimal fee = NumberUtil.mul(money, NumberUtil.div(withdrawFee.doubleValue(), 100));
        /** 生成订单号 **/
        String withdrawNo = IdUtils.randomId();

        // 汇率
        double rate = NumberUtils.toDouble(webParamsService.getParamsValue("usdt_exchange_rate"));
        // 扣除金额
        webUserService.updateUserBalance(user.getUserName(), money.negate());

        WebWithdraw withdraw = new WebWithdraw();
        withdraw.setOrderNo(IdUtils.randomId());
        withdraw.setUserName(userName);
        withdraw.setMerchantCode(null);
        withdraw.setChannelCode(null);
        withdraw.setAmount(money);
        withdraw.setRealAmount(NumberUtil.sub(money, fee));
        withdraw.setVirtualAmount(NumberUtil.div(money, rate));
        withdraw.setVirtualRealAmount(NumberUtil.div(withdraw.getRealAmount(), rate));
        withdraw.setFee(fee);
        withdraw.setPayOrderNo(null);
        withdraw.setPaySign(null);
        withdraw.setType(NumberUtils.toInt(request.getType()));
        withdraw.setErrMsg(null);
        withdraw.setCreateTime(now);
        withdraw.setModifyTime(now);
        withdraw.setStatus(0);
        withdraw.setAgent(user.getAgent());
        withdraw.setAgentNode(user.getAgentNode());
        withdraw.setAgentLevel(user.getAgentLevel());
        withdraw.setAccount(request.getAccount());
        withdraw.setRealName(request.getRealName());
        if (StringUtils.equals(request.getType(), "1")) {
            withdraw.setIfscCode(StringUtils.isEmpty(request.getIfscCode()) ? "Gcash" : request.getIfscCode());
        }
        withdraw.setMobile(request.getMobile());
        webWithdrawService.save(withdraw);

        WebFundRecordEntity fund = new WebFundRecordEntity();
        fund.setUserName(user.getUserName());
        fund.setSerialNo(IdUtils.randomId());
        fund.setRefBillNo(withdrawNo);
        fund.setAmount(money.negate());
        fund.setBeforeAmount(user.getBalance());
        fund.setAfterAmount(NumberUtil.sub(user.getBalance(), money));
        fund.setType(Constant.FundType.WITHDRAW.getValue());
        fund.setCreateTime(now);
        fund.setAgent(user.getAgent());
        fund.setAgentNode(user.getAgentNode());
        fund.setAgentLevel(user.getAgentLevel());

        webFundRecordService.save(fund);

        return R.ok(MsgUtil.get("pg.system.withdraw.success"));
    }

    @ApiOperation(value = "提币订单列表")
    @GetMapping("/withdraw/order/list")
    public R withdraw(WithdrawOrderRequest request, HttpServletRequest httpServletRequest) {
        if (!StringUtils.equalsAny(request.getType(), "1", "2")) {
            return R.error();
        }

        List<WebDictEntity> dict = webDictService.getAll();
        //List<WebDictEntity> dict = webDictService.list(new QueryWrapper<WebDictEntity>().lambda().eq(WebDictEntity::getStatus, 1).eq(WebDictEntity::getType, 2));
        Map<String, String> dictMap = dict.stream().filter(temp -> temp.getType().intValue() == 2).collect(Collectors.toMap(WebDictEntity::getDictKey, dictEntity -> dictEntity.getDictValue()));

        String userName = JwtUtils.getUserName(httpServletRequest);

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("type", request.getType());
        params.put("userName", userName);

        PageUtils page = webWithdrawService.queryPage(params);
        List<WebWithdraw> list = (List<WebWithdraw>) page.getList();
        if (CollUtil.isNotEmpty(list)) {
            JSONArray arr = new JSONArray();
            for (WebWithdraw withdraw : list) {
                JSONObject obj = new JSONObject();
                //obj.put("amount", MathUtil.scale(withdraw.getType() == 1 ? withdraw.getAmount() : withdraw.getVirtualAmount()));
                obj.put("amount", MathUtil.scale(withdraw.getAmount()));
                obj.put("status", withdraw.getStatus());
                obj.put("createTime", withdraw.getCreateTime());
                obj.put("typeStr", dictMap.getOrDefault(withdraw.getStatus().toString(), "-"));
                arr.add(obj);
            }
            page.setList(arr);
        }
        return R.ok().put("page", page);
    }

    @ApiOperation(value = "邀请信息")
    @GetMapping("/inviteMission")
    public R inviteMission(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        List<WebFundRecordEntity> list = webFundRecordService.list(
                new QueryWrapper<WebFundRecordEntity>().lambda()
                        .eq(WebFundRecordEntity::getUserName, userName)
                        .eq(WebFundRecordEntity::getType, Constant.FundType.INVITATION_REWARD.getValue())
                        .select(WebFundRecordEntity::getAmount)
        );
        // 直属下级人数
        int users = webUserService.count(new QueryWrapper<WebUserEntity>().lambda().eq(WebUserEntity::getAgent, userName));
        // 总奖金
        double totalBonuses = list.stream().mapToDouble(order -> order.getAmount().doubleValue()).sum();
        JSONObject obj = new JSONObject();
        obj.put("totalBonuses", MathUtil.scale(totalBonuses));
        obj.put("firends", users);
        return R.ok().put("data", obj);
    }

    @ApiOperation(value = "邀请奖金列表")
    @GetMapping("/invite/bonuses/list")
    public R inviteBonusesList(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        List<WebFundRecordEntity> list = webFundRecordService.list(
                new QueryWrapper<WebFundRecordEntity>().lambda()
                        .eq(WebFundRecordEntity::getUserName, userName)
                        .eq(WebFundRecordEntity::getType, Constant.FundType.INVITATION_REWARD.getValue())
                        .orderByDesc(WebFundRecordEntity::getCreateTime)
        );

        List<WebTopup> tops = webTopupService.list(
                new QueryWrapper<WebTopup>().lambda()
                        .eq(WebTopup::getAgent, userName)
                        .eq(WebTopup::getStatus, 2)
                        .select(WebTopup::getOrderNo, WebTopup::getUserName)
        );
        Map<String, String> topMap = tops.stream().collect(Collectors.toMap(WebTopup::getOrderNo, top -> top.getUserName()));
        JSONArray arr = new JSONArray();
        for (WebFundRecordEntity fund : list) {
            JSONObject obj = new JSONObject();
            obj.put("time", fund.getCreateTime());
            obj.put("userName", topMap.getOrDefault(fund.getRefBillNo(), "-"));
            obj.put("bonuses", fund.getAmount());
            arr.add(obj);
        }
        return R.ok().put("list", arr);
    }

    @ApiOperation(value = "分享信息")
    @GetMapping("/share")
    public R share(HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);

        JSONObject obj = new JSONObject();
        String inviteUrl = webParamsService.getParamsValue("invite_url");

        WebUserEntity user = webUserService.getUser(userName);
        String inviteQrcode = user.getInviteQrcode();
        if (StringUtils.isEmpty(inviteQrcode)) {
            SysConfig config = sysConfigService.getOne(new QueryWrapper<SysConfig>().lambda().eq(SysConfig::getParamKey, "CLOUD_STORAGE_CONFIG_KEY"));
            JSONObject json = JSONObject.parseObject(config.getParamValue());
            String aliyunAccessKeyId = json.getString("aliyunAccessKeyId");
            String aliyunAccessKeySecret = json.getString("aliyunAccessKeySecret");
            String aliyunBucketName = json.getString("aliyunBucketName");
            String aliyunDomain = json.getString("aliyunDomain");
            String aliyunEndPoint = json.getString("aliyunEndPoint");
            String aliyunPrefix = "qrcode";
            ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
            conf.setSupportCname(true);
            OSS ossClient = new OSSClientBuilder().build(aliyunDomain, aliyunAccessKeyId, aliyunAccessKeySecret, conf);
            try {
                // 生成二维码
                String content = inviteUrl + "/pages/register?inviteCode=" + user.getInviteCode();
                BufferedImage qrcodeImg = QrCodeUtil.generate(content, 300, 300);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(qrcodeImg, "JPG",os);
                // 上传至阿里云
                String fileName = aliyunPrefix + "/" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + ".jpg";
                ossClient.putObject(aliyunBucketName, fileName, new ByteArrayInputStream(os.toByteArray()));
                // 修改用户信息
                inviteQrcode = aliyunDomain + "/" + fileName;
                webUserService.update(new UpdateWrapper<WebUserEntity>().lambda().eq(WebUserEntity::getUserName, userName).set(WebUserEntity::getInviteQrcode, inviteQrcode));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            }
        }
        obj.put("inviteQrcode", inviteQrcode);
        obj.put("inviteUrl", inviteUrl);
        obj.put("inviteCode", user.getInviteCode());

        // 今天收入代币
        List<WebDayReportEntity> todayReport = webDayReportService.list(
                new QueryWrapper<WebDayReportEntity>().lambda()
                        .eq(WebDayReportEntity::getToDay, DateUtil.today())
                        .eq(WebDayReportEntity::getUserName, user.getUserName())
        );
        double todayVirtual = todayReport.stream().mapToDouble(order -> order.getVirtualIncome().doubleValue()).sum();
        obj.put("todayVirtual", MathUtil.scale(todayVirtual));

        double totalIncome = todayReport.stream().mapToDouble(order -> order.getInCome().doubleValue()).sum();
        obj.put("totalIncome", MathUtil.scale(totalIncome));

        Date now = new Date();
        int ordersOfDay = webOrderService.count(
                new QueryWrapper<WebOrderEntity>().lambda()
                        .eq(WebOrderEntity::getUserName, user.getUserName())
                        .between(WebOrderEntity::getCreateTime, DateUtil.beginOfDay(now), DateUtil.endOfDay(now))
        );
        // 今日已完成任务次数
        obj.put("ordersOfDay", ordersOfDay);


        WebLevelEntity level = webLevelService.getUserCurrLevel(webLevelService.getLevelsByType(1), user.getBalance());
        //WebLevelEntity level = webLevelService.getOne(
        //        new QueryWrapper<WebLevelEntity>().lambda()
        //                .eq(WebLevelEntity::getLevelType, 1)
        //                .le(WebLevelEntity::getMinBalance, user.getBalance())
        //                .ge(WebLevelEntity::getMaxBalance, user.getBalance())
        //                .orderByDesc(WebLevelEntity::getLevelValue)
        //);
        obj.put("orderCount", level.getDayCount());
        return R.ok().put("data", obj);
    }
}
