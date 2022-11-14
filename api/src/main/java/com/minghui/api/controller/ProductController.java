package com.minghui.api.controller;
import java.util.Date;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.minghui.api.controller.request.LoginPhoneRequest;
import com.minghui.api.controller.request.RegisterPhoneRequest;
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

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Api(value = "产品相关", tags = "产品相关")
@RequestMapping("/product")
@RestController
public class ProductController {

    @Autowired
    private WebUserService webUserService;

    @Autowired
    private WebLevelService webLevelService;

    @Autowired
    private WebProductService webProductService;

    @Autowired
    private WebDayReportService webDayReportService;

    @Autowired
    private WebOrderService webOrderService;

    @Autowired
    private WebFundRecordService webFundRecordService;

    @Autowired
    private WebVirtualRecordService webVirtualRecordService;

    @Autowired
    private WebCommissionRecordService webCommissionRecordService;

    @Autowired
    private WebParamsService webParamsService;

    @ApiOperation(value = "产品列表")
    @GetMapping("/list")
    public R list(HttpServletRequest request) {

        String userName = JwtUtils.getUserName(request);

        //List<WebLevelEntity> lvs = webLevelService.list(new QueryWrapper<WebLevelEntity>().lambda().eq(WebLevelEntity::getLevelType, 1).orderByAsc(WebLevelEntity::getLevelValue));
        List<WebLevelEntity> lvs = webLevelService.getLevelsByType(1);

        //List<WebProductEntity> products = webProductService.list(new QueryWrapper<WebProductEntity>().lambda().eq(WebProductEntity::getStatus, 1));
        List<WebProductEntity> products = webProductService.getAll();
        Map<Integer, List<WebProductEntity>> productMaps = products.stream().collect(Collectors.groupingBy(WebProductEntity::getLevelValue));

        JSONObject object = new JSONObject();

        JSONArray lvArrs = new JSONArray();

        String virtual_multiple = webParamsService.getParamsValue("virtual_multiple");
        int multiple = NumberUtils.toInt(virtual_multiple, 2);
        for (WebLevelEntity lv : lvs) {
            if (lv.getLevelValue().intValue() == 0) {
                continue;
            }

            List<WebProductEntity> productByMap = productMaps.get(lv.getLevelValue());
            if (CollUtil.isEmpty(productByMap)) {
                continue;
            }
            Collections.shuffle(productByMap);
            JSONObject obj = new JSONObject();
            obj.put("levelValue", lv.getLevelValue());
            obj.put("levelName", lv.getLevelName());
            obj.put("dayCount", lv.getDayCount());
            obj.put("price", lv.getProductPrice());
            obj.put("dayCommission", NumberUtil.mul(lv.getDayCount(), lv.getIncome(), (multiple + 1)));

            WebProductEntity product = productByMap.get(0);
            obj.put("productName", product.getProductName());
            obj.put("productId", product.getId());
            obj.put("productImg", product.getProductImg());
            lvArrs.add(obj);
        }

        JSONArray vipArrs = new JSONArray();
        if (StringUtils.isNotEmpty(userName)) {
            //List<WebLevelEntity> vips = webLevelService.list(new QueryWrapper<WebLevelEntity>().lambda().eq(WebLevelEntity::getLevelType, 2).orderByAsc(WebLevelEntity::getLevelValue));
            List<WebLevelEntity> vips = webLevelService.getLevelsByType(2);
            // 查询下级充值量1
            List<WebDayReportEntity> reports = webDayReportService.list(new QueryWrapper<WebDayReportEntity>().lambda().eq(WebDayReportEntity::getAgent, userName));
            double totalTopup = reports.stream().mapToDouble(report -> report.getTopUp().doubleValue()).sum();
            for (WebLevelEntity vip : vips) {
                if (vip.getLevelValue() == 0) {
                    continue;
                }

                List<WebProductEntity> productByMap = productMaps.get(vip.getLevelValue());
                if (CollUtil.isEmpty(productByMap)) {
                    continue;
                }
                JSONObject obj = new JSONObject();
                obj.put("levelValue", vip.getLevelValue());
                obj.put("levelName", vip.getLevelName());
                obj.put("dayCount", vip.getDayCount());
                obj.put("price", vip.getProductPrice());
                obj.put("dayCommission", NumberUtil.mul(vip.getDayCount(), vip.getIncome(), (multiple + 1)));
                Collections.shuffle(productByMap);
                WebProductEntity product = productByMap.get(0);
                obj.put("productName", product.getProductName());
                obj.put("productId", product.getId());
                obj.put("productImg", product.getProductImg());

                double percentage = 0;
                if (totalTopup == 0) {
                    percentage = 0;
                } else if (totalTopup >= vip.getProductPrice()) {
                    percentage = 100;
                } else {
                    percentage = NumberUtil.mul(NumberUtil.div(totalTopup, vip.getProductPrice().doubleValue()), 100);
                }
                obj.put("percentage", MathUtil.scale(2, percentage));
                vipArrs.add(obj);
            }
        }
        object.put("lvs", lvArrs);
        object.put("vips", vipArrs);
        return R.ok().put("data", object);
    }

    @ApiOperation(value = "产品详情")
    @GetMapping("/detail/{id}")
    public R list(HttpServletRequest request,@PathVariable("id") String id) throws Exception {
        String userName = JwtUtils.getUserName(request);
        WebUserEntity user = webUserService.getUser(userName);
        // 查询商品信息
        //WebProductEntity product = webProductService.getOne(new QueryWrapper<WebProductEntity>().lambda().eq(WebProductEntity::getStatus, 1).eq(WebProductEntity::getId, id));
        WebProductEntity product = webProductService.getProductById(NumberUtils.toInt(id));
        if (product == null) {
            throw new Exception("未查询到ID[" + id + "]商品");
        }
        // 获取等级信息
        //WebLevelEntity level = webLevelService.getOne(new QueryWrapper<WebLevelEntity>().lambda().eq(WebLevelEntity::getLevelValue, product.getLevelValue()));
        WebLevelEntity level = webLevelService.getLevelByLevelValue(product.getLevelValue());

        JSONObject obj = new JSONObject();

        String virtual_multiple = webParamsService.getParamsValue("virtual_multiple");
        int multiple = NumberUtils.toInt(virtual_multiple, 2);

        // 商品名称
        obj.put("productName", product.getProductName());
        // 商品ID
        obj.put("productId", product.getId());
        // 等级值
        obj.put("levelValue", level.getLevelValue());
        // 等级名称
        obj.put("levelName", level.getLevelName());
        // 商品价格
        obj.put("price", level.getProductPrice());
        // 商品图片
        obj.put("productImg", product.getProductImg());
        // 今日完成任务总佣金
        obj.put("tasksCommission", MathUtil.scale(level.getIncome().doubleValue() * level.getDayCount()));
        // 今日完成任务总代币佣金
        obj.put("virtualCommission", MathUtil.scale(level.getIncome().doubleValue() * multiple * level.getDayCount()));
        // 1级朋友返点
        obj.put("Level1Friends", level.getAgent1());
        // 2级朋友返点
        obj.put("Level2Friends", level.getAgent2());
        // 3级朋友返点
        obj.put("Level3Friends", level.getAgent3());
        List<WebDayReportEntity> reports = webDayReportService.list(new QueryWrapper<WebDayReportEntity>().lambda().eq(WebDayReportEntity::getToDay, DateUtil.today()).eq(WebDayReportEntity::getAgent, user.getUserName()));
        double todayTopup = reports.stream().mapToDouble(order -> order.getTopUp().doubleValue()).sum();
        int dayCount = 0;
        if (todayTopup >= 500) {
            dayCount = 1;
        }
        // 一天多少次任务
        obj.put("orderCount", level.getDayCount() + dayCount);

        Date now = new Date();
        int ordersOfDay = webOrderService.count(
                new QueryWrapper<WebOrderEntity>().lambda()
                        .eq(WebOrderEntity::getUserName, user.getUserName())
                        .between(WebOrderEntity::getCreateTime, DateUtil.beginOfDay(now), DateUtil.endOfDay(now))
        );
        // 今日已完成任务次数
        obj.put("ordersOfDay", ordersOfDay);

        WebDayReportEntity todayReport = webDayReportService.getOne(
                new QueryWrapper<WebDayReportEntity>().lambda()
                        .eq(WebDayReportEntity::getToDay, DateUtil.today())
                        .eq(WebDayReportEntity::getUserName, user.getUserName())
        );
        // 今日完成任务后已获得多少佣金
        obj.put("incomeOfDay", todayReport == null ? 0 : MathUtil.scale(todayReport.getInCome()));
        // 今日完成任务后已获得多少代币佣金
        obj.put("virtualIncomeOfDay", todayReport == null ? 0 : MathUtil.scale(todayReport.getVirtualIncome()));
        // 用户代币余额
        obj.put("virtualBalance", user.getVirtualBalance());
        // 今日获得多少下级返佣
        obj.put("todayCommission", todayReport == null ? 0 : MathUtil.scale(todayReport.getCommission()));
        return R.ok().put("data", obj);
    }

    @ApiOperation(value = "下订单前的详情")
    @GetMapping("/order/detail/{id}")
    public R orderDetail(HttpServletRequest request,@PathVariable("id") String id) throws Exception {
        Date now = new Date();
        String userName = JwtUtils.getUserName(request);
        WebUserEntity user = webUserService.getUser(userName);
        // 查询商品信息
        //WebProductEntity product = webProductService.getOne(new QueryWrapper<WebProductEntity>().lambda().eq(WebProductEntity::getStatus, 1).eq(WebProductEntity::getId, id));
        WebProductEntity product = webProductService.getProductById(NumberUtils.toInt(id));
        if (product == null) {
            throw new Exception("未查询到ID[" + id + "]商品");
        }
        // 获取商品等级信息
        //WebLevelEntity produceLevel = webLevelService.getOne(new QueryWrapper<WebLevelEntity>().lambda().eq(WebLevelEntity::getLevelValue, product.getLevelValue()));
        WebLevelEntity produceLevel = webLevelService.getLevelByLevelValue(product.getLevelValue());
        // 验证金额
        if (user.getBalance().doubleValue() < produceLevel.getProductPrice()) {
            return R.error(MsgUtil.get("system.order.account.balance"));
        }

        List<WebDayReportEntity> dayReports = webDayReportService.list(new QueryWrapper<WebDayReportEntity>().lambda().eq(WebDayReportEntity::getToDay, DateUtil.today()).eq(WebDayReportEntity::getAgent, user.getUserName()));
        double todayTopup = dayReports.stream().mapToDouble(order -> order.getTopUp().doubleValue()).sum();
        int dayCount = 0;
        if (todayTopup >= 500) {
            dayCount = 1;
        }

        // 验证一天投注测试
        int betCount = webOrderService.count(
                new QueryWrapper<WebOrderEntity>().lambda()
                        .eq(WebOrderEntity::getUserName, user.getUserName())
                        .between(WebOrderEntity::getCreateTime, DateUtil.beginOfDay(now), DateUtil.endOfDay(now))
        );
        if (betCount >= produceLevel.getDayCount() + dayCount) {
            return R.error(MsgUtil.get("error.20001"));
        }

        // 验证等级是否满足
        if (produceLevel.getLevelType() == 1) {
            // 普通商品
            webLevelService.getLevelsByType(1);
            WebLevelEntity userLevel = webLevelService.getUserCurrLevel(webLevelService.getLevelsByType(1), user.getBalance());
            //WebLevelEntity userLevel = webLevelService.getOne(
            //        new QueryWrapper<WebLevelEntity>().lambda()
            //                .eq(WebLevelEntity::getLevelType, 1)
            //                .le(WebLevelEntity::getMinBalance, user.getBalance())
            //                .ge(WebLevelEntity::getMaxBalance, user.getBalance())
            //);
            if (userLevel.getLevelValue() < produceLevel.getLevelValue()) {
                throw new Exception("用户当前等级不足以购买该产品");
                //return R.error();
            }
        } else {
            // VIP商品
            // 查询所有下级充值量
            List<WebDayReportEntity> reports = webDayReportService.list(new QueryWrapper<WebDayReportEntity>().lambda().eq(WebDayReportEntity::getAgent, userName));
            double totalTopup = reports.stream().mapToDouble(report -> report.getTopUp().doubleValue()).sum();
            WebLevelEntity userLevel = webLevelService.getUserCurrLevel(webLevelService.getLevelsByType(2), new BigDecimal(totalTopup));
            //WebLevelEntity userLevel = webLevelService.getOne(
            //        new QueryWrapper<WebLevelEntity>().lambda()
            //                .eq(WebLevelEntity::getLevelType, 2)
            //                .le(WebLevelEntity::getMinBalance, totalTopup)
            //                .ge(WebLevelEntity::getMaxBalance, totalTopup)
            //);
            if (userLevel == null || userLevel.getLevelValue() < produceLevel.getLevelValue()) {
                throw new Exception("用户当前等级不足以购买该产品");
                //return R.error();
            }
        }

        String virtual_multiple = webParamsService.getParamsValue("virtual_multiple");
        int multiple = NumberUtils.toInt(virtual_multiple, 2);

        JSONObject obj = new JSONObject();
        obj.put("orderNo", IdUtils.randomId());
        obj.put("time", DateUtil.formatDateTime(new Date()));
        obj.put("amount", produceLevel.getProductPrice());
        obj.put("income", produceLevel.getIncome());
        obj.put("virtualAmount", MathUtil.scale(produceLevel.getIncome().doubleValue() * multiple));

        return R.ok().put("data", obj);
    }

    @Transactional
    @ApiOperation(value = "下订单")
    @GetMapping("/order/{id}")
    public R order(HttpServletRequest request,@PathVariable("id") String id) throws Exception {

        String userName = JwtUtils.getUserName(request);
        WebUserEntity user = webUserService.getUser(userName);
        Date now = new Date();
        // 查询商品信息
        //WebProductEntity product = webProductService.getOne(new QueryWrapper<WebProductEntity>().lambda().eq(WebProductEntity::getStatus, 1).eq(WebProductEntity::getId, id));
        WebProductEntity product = webProductService.getProductById(NumberUtils.toInt(id));
        if (product == null) {
            throw new Exception("未查询到ID[" + id + "]商品");
        }
        // 获取商品等级信息
        //WebLevelEntity produceLevel = webLevelService.getOne(new QueryWrapper<WebLevelEntity>().lambda().eq(WebLevelEntity::getLevelValue, product.getLevelValue()));
        WebLevelEntity produceLevel = webLevelService.getLevelByLevelValue(product.getLevelValue());
        // 验证金额
        if (user.getBalance().doubleValue() < produceLevel.getProductPrice()) {
            return R.error(MsgUtil.get("system.order.account.balance"));
        }

        List<WebDayReportEntity> dayReports = webDayReportService.list(new QueryWrapper<WebDayReportEntity>().lambda().eq(WebDayReportEntity::getToDay, DateUtil.today()).eq(WebDayReportEntity::getAgent, user.getUserName()));
        double todayTopup = dayReports.stream().mapToDouble(order -> order.getTopUp().doubleValue()).sum();
        int dayCount = 0;
        if (todayTopup >= 500) {
            dayCount = 1;
        }

        // 验证一天投注数量
        int betCount = webOrderService.count(
                new QueryWrapper<WebOrderEntity>().lambda()
                        .eq(WebOrderEntity::getUserName, user.getUserName())
                        .between(WebOrderEntity::getCreateTime, DateUtil.beginOfDay(now), DateUtil.endOfDay(now))
        );
        if (betCount >= produceLevel.getDayCount() + dayCount) {
            return R.error(MsgUtil.get("error.20001"));
        }
        // 验证等级是否满足
        if (produceLevel.getLevelType() == 1) {
            // 普通商品
            //WebLevelEntity userLevel = webLevelService.getOne(
            //        new QueryWrapper<WebLevelEntity>().lambda()
            //                .eq(WebLevelEntity::getLevelType, 1)
            //                .le(WebLevelEntity::getMinBalance, user.getBalance())
            //                .ge(WebLevelEntity::getMaxBalance, user.getBalance())
            //);
            WebLevelEntity userLevel = webLevelService.getUserCurrLevel(webLevelService.getLevelsByType(1), user.getBalance());
            if (userLevel == null || userLevel.getLevelValue() < produceLevel.getLevelValue()) {
                //return R.error();
                throw new Exception("用户当前等级不足以购买该产品");
            }
        } else {
            // VIP商品
            // 查询所有下级充值量
            List<WebDayReportEntity> reports = webDayReportService.list(new QueryWrapper<WebDayReportEntity>().lambda().eq(WebDayReportEntity::getAgent, userName));
            double totalTopup = reports.stream().mapToDouble(report -> report.getTopUp().doubleValue()).sum();

            WebLevelEntity userLevel = webLevelService.getUserCurrLevel(webLevelService.getLevelsByType(2), new BigDecimal(totalTopup));
            //WebLevelEntity userLevel = webLevelService.getOne(
            //        new QueryWrapper<WebLevelEntity>().lambda()
            //                .eq(WebLevelEntity::getLevelType, 2)
            //                .le(WebLevelEntity::getMinBalance, totalTopup)
            //                .ge(WebLevelEntity::getMaxBalance, totalTopup)
            //);
            if (userLevel == null || userLevel.getLevelValue() < produceLevel.getLevelValue()) {
                //return R.error();
                throw new Exception("用户当前等级不足以购买该产品");
            }
        }

        String virtual_multiple = webParamsService.getParamsValue("virtual_multiple");
        int multiple = NumberUtils.toInt(virtual_multiple, 2);
        BigDecimal virtualAmount = NumberUtil.mul(produceLevel.getIncome(), multiple);

        JSONObject obj = new JSONObject();
        // 添加订单
        String orderNo = IdUtils.randomId();
        WebOrderEntity order = new WebOrderEntity();
        order.setUserName(user.getUserName());
        order.setOrderNo(orderNo);
        order.setAmount(new BigDecimal(produceLevel.getProductPrice()));
        order.setIncome(produceLevel.getIncome());
        order.setVirtualIncome(virtualAmount);
        order.setProductName(product.getProductName());
        order.setProductUrl(product.getProductImg());
        order.setCreateTime(now);
        order.setModifyTime(now);
        order.setAgent(user.getAgent());
        order.setAgentNode(user.getAgentNode());
        order.setAgentLevel(user.getAgentLevel());
        webOrderService.save(order);
        // 修改余额 扣除订单金额 增加盈利金额
        webUserService.updateUserBalance(user.getUserName(), produceLevel.getIncome());
        // 添加流水
        List<WebFundRecordEntity> funds = new ArrayList<>();
        WebFundRecordEntity fund2 = new WebFundRecordEntity();
        fund2.setUserName(user.getUserName());
        fund2.setSerialNo(IdUtils.randomId());
        fund2.setRefBillNo(orderNo);
        fund2.setAmount(produceLevel.getIncome());
        fund2.setBeforeAmount(user.getBalance());
        fund2.setAfterAmount(NumberUtil.add(user.getBalance(), produceLevel.getIncome()));
        fund2.setType(Constant.FundType.INCOME.getValue());
        fund2.setCreateTime(now);
        fund2.setAgent(user.getAgent());
        fund2.setAgentNode(user.getAgentNode());
        fund2.setAgentLevel(user.getAgentLevel());
        funds.add(fund2);
        //webFundRecordService.saveBatch(funds);
        // 添加代币
        webUserService.updateUserVirtualBalance(user.getUserName(), virtualAmount);

        List<WebVirtualRecordEntity> virtuals = new ArrayList<>();
        // 添加代币流水
        WebVirtualRecordEntity virtual = new WebVirtualRecordEntity();
        virtual.setUserName(user.getUserName());
        virtual.setSerialNo(IdUtils.randomId());
        virtual.setRefBillNo(orderNo);
        virtual.setAmount(virtualAmount);
        virtual.setBeforeAmount(user.getVirtualBalance());
        virtual.setAfterAmount(NumberUtil.add(user.getVirtualBalance(), virtualAmount));
        virtual.setType(Constant.VirtualType.MINING.getValue());
        virtual.setCreateTime(now);
        virtual.setAgent(user.getAgent());
        virtual.setAgentNode(user.getAgentNode());
        virtual.setAgentLevel(user.getAgentLevel());
        virtuals.add(virtual);
        //webVirtualRecordService.save(virtual);

        List<WebDayReportEntity> reports = new ArrayList<>();
        // 添加报表
        WebDayReportEntity dayReport = new WebDayReportEntity();
        dayReport.setUserName(user.getUserName());
        dayReport.setToDay(now);
        dayReport.setTopUp(new BigDecimal("0"));
        dayReport.setWithdraw(new BigDecimal("0"));
        dayReport.setBet(new BigDecimal(produceLevel.getProductPrice()));
        dayReport.setInCome(produceLevel.getIncome());
        dayReport.setCommission(new BigDecimal("0"));
        dayReport.setVirtualIncome(virtualAmount);
        dayReport.setAgent(user.getAgent());
        dayReport.setAgentNode(user.getAgentNode());
        dayReport.setAgentLevel(user.getAgentLevel());
        //webDayReportService.insertOrUpdate(dayReport);
        reports.add(dayReport);
        // 返佣

        // 获取直接上级
        List<WebCommissionRecordEntity> commissions = new ArrayList<>();
        List<WebUserEntity> userAgents = getUserAgents(user);
        if (CollUtil.isNotEmpty(userAgents)) {
            for (int i = 0; i < userAgents.size(); i++) {
                WebUserEntity agent = userAgents.get(i);
                double rebate = 0;
                if (i + 1 == 1) {
                    rebate = produceLevel.getAgent1();
                } else if (i + 1 == 2) {
                    rebate = produceLevel.getAgent2();
                } else if (i + 1 == 3) {
                    rebate = produceLevel.getAgent3();
                } else {}
                if (rebate >= 0) {
                    // 计算返点
                    BigDecimal commission = NumberUtil.mul(produceLevel.getIncome(), NumberUtil.div(rebate, 100));
                    // 代理加钱
                    webUserService.updateUserBalance(agent.getUserName(), commission);

                    // 代币返点
                    BigDecimal virtualCommission = NumberUtil.mul(virtualAmount, NumberUtil.div(rebate, 100));
                    // 代币加钱
                    webUserService.updateUserVirtualBalance(agent.getUserName(),virtualCommission);
                    // 添加报表
                    WebDayReportEntity report = new WebDayReportEntity();
                    report.setUserName(agent.getUserName());
                    report.setToDay(now);
                    report.setTopUp(new BigDecimal("0"));
                    report.setWithdraw(new BigDecimal("0"));
                    report.setBet(new BigDecimal("0"));
                    report.setInCome(new BigDecimal("0"));
                    report.setCommission(commission);
                    report.setVirtualIncome(virtualCommission);
                    report.setAgent(agent.getAgent());
                    report.setAgentNode(agent.getAgentNode());
                    report.setAgentLevel(agent.getAgentLevel());
                    reports.add(report);
                    // 添加流水记录
                    WebFundRecordEntity fund = new WebFundRecordEntity();
                    fund.setUserName(agent.getUserName());
                    fund.setSerialNo(IdUtils.randomId());
                    fund.setRefBillNo(orderNo);
                    fund.setAmount(commission);
                    fund.setBeforeAmount(agent.getBalance());
                    fund.setAfterAmount(NumberUtil.add(agent.getBalance(), commission));
                    fund.setType(Constant.FundType.COMMISSION.getValue());
                    fund.setCreateTime(now);
                    fund.setAgent(agent.getAgent());
                    fund.setAgentNode(agent.getAgentNode());
                    fund.setAgentLevel(agent.getAgentLevel());
                    funds.add(fund);

                    // 添加代币记录
                    WebVirtualRecordEntity virtualRecord = new WebVirtualRecordEntity();
                    virtualRecord.setUserName(agent.getUserName());
                    virtualRecord.setSerialNo(IdUtils.randomId());
                    virtualRecord.setRefBillNo(orderNo);
                    virtualRecord.setAmount(virtualCommission);
                    virtualRecord.setBeforeAmount(agent.getVirtualBalance());
                    virtualRecord.setAfterAmount(NumberUtil.add(agent.getVirtualBalance(), virtualCommission));
                    virtualRecord.setType(Constant.VirtualType.COMMISSION.getValue());
                    virtualRecord.setCreateTime(now);
                    virtualRecord.setAgent(agent.getAgent());
                    virtualRecord.setAgentNode(agent.getAgentNode());
                    virtualRecord.setAgentLevel(agent.getAgentLevel());
                    virtuals.add(virtualRecord);
                    // 添加返点记录
                    WebCommissionRecordEntity commissionRecordEntity = new WebCommissionRecordEntity();
                    commissionRecordEntity.setUserName(user.getUserName());
                    commissionRecordEntity.setCommissionUser(agent.getUserName());
                    commissionRecordEntity.setCommission(commission);
                    commissionRecordEntity.setRefBillNo(orderNo);
                    commissionRecordEntity.setCreateTime(now);
                    commissionRecordEntity.setAgentNode(user.getAgentNode());
                    commissionRecordEntity.setAgentLevel(user.getAgentLevel());
                    commissions.add(commissionRecordEntity);
                }
            }
        }
        if (CollUtil.isNotEmpty(commissions)) {
            webCommissionRecordService.saveBatch(commissions);
        }
        webFundRecordService.saveBatch(funds);
        webVirtualRecordService.saveBatch(virtuals);
        webDayReportService.batchInsertOrUpdate(reports);
        obj.put("income", produceLevel.getIncome());
        obj.put("virtualAmount", MathUtil.scale(virtualAmount));
        return R.ok().put("data", obj);
    }

    private List<WebUserEntity> getUserAgents(WebUserEntity user) {
        List<WebUserEntity> list = new ArrayList<>();
        // 获取直接上级
        WebUserEntity agent1 = webUserService.getOne(new QueryWrapper<WebUserEntity>().lambda().eq(WebUserEntity::getUserName, user.getAgent()));
        if (agent1 == null) {
            return list;
        }
        list.add(agent1);
        // 获取二级上级
        WebUserEntity agent2 = webUserService.getOne(new QueryWrapper<WebUserEntity>().lambda().eq(WebUserEntity::getUserName, agent1.getAgent()));
        if (agent2 == null) {
            return list;
        }
        list.add(agent2);
        // 获取三级三级
        WebUserEntity agent3 = webUserService.getOne(new QueryWrapper<WebUserEntity>().lambda().eq(WebUserEntity::getUserName, agent2.getAgent()));
        if (agent3 == null) {
            return list;
        }
        list.add(agent3);
        return list;
    }
}
