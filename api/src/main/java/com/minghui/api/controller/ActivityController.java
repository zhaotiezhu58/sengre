package com.minghui.api.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.minghui.api.utils.JwtUtils;
import com.minghui.commons.constants.Constant;
import com.minghui.commons.entity.*;
import com.minghui.commons.service.*;
import com.minghui.commons.utils.IdUtils;
import com.minghui.commons.utils.MathUtil;
import com.minghui.commons.utils.MsgUtil;
import com.minghui.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Api(value = "活动相关", tags = "活动")
@RequestMapping("/activity")
@RestController
public class ActivityController {

    @Autowired
    private WebUserService webUserService;

    @Autowired
    private WebFundRecordService webFundRecordService;

    @Autowired
    private WebLevelService webLevelService;

    @Autowired
    private WebDayReportService webDayReportService;

    @Autowired
    private WebWeeksalaryRecordService webWeeksalaryRecordService;

    @Autowired
    private WebLuckySpinService webLuckySpinService;

    @Autowired
    private WebLuckySpinConfigService webLuckySpinConfigService;

    @Autowired
    private WebTopupService webTopupService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Transactional
    @ApiOperation(value = "周薪领取")
    @PostMapping("/weeklysalary")
    public R weeklysalary(HttpServletRequest request) throws Exception {
        Date now = new Date();
        int day = DateUtil.dayOfWeek(now);
        if (day != 4) {
            return R.error(MsgUtil.get("error.50001"));
        }
        String userName = JwtUtils.getUserName(request);
        WebUserEntity user = webUserService.getUser(userName);

        Date beginTime = DateUtil.beginOfDay(DateUtil.offsetDay(now, -8));
        Date endTime = DateUtil.endOfDay(DateUtil.offsetDay(now, -1));
        List<WebFundRecordEntity> funds = webFundRecordService.list(
                new QueryWrapper<WebFundRecordEntity>().lambda()
                        .eq(WebFundRecordEntity::getUserName, userName)
                        .between(WebFundRecordEntity::getCreateTime, beginTime, endTime)
                        .orderByAsc(WebFundRecordEntity::getAfterAmount)
        );
        BigDecimal balance = user.getBalance();
        if (CollUtil.isNotEmpty(funds)) {
            balance = funds.get(0).getAfterAmount();
        }

        // 判断是否是VIP
        List<WebDayReportEntity> reports = webDayReportService.list(
                new QueryWrapper<WebDayReportEntity>().lambda()
                        .eq(WebDayReportEntity::getAgent, userName)
        );
        double totalTopup = reports.stream().mapToDouble(report -> report.getTopUp().doubleValue()).sum();
        WebLevelEntity userLevel = webLevelService.getOne(
                new QueryWrapper<WebLevelEntity>().lambda()
                        .eq(WebLevelEntity::getLevelType, 1)
                        .le(WebLevelEntity::getMinBalance, totalTopup)
                        .ge(WebLevelEntity::getMaxBalance, totalTopup)
        );
        BigDecimal salary = new BigDecimal("0");
        if (userLevel != null && userLevel.getWeeklySalary().doubleValue() != 0) {
            salary = userLevel.getWeeklySalary();
        } else {
            userLevel = webLevelService.getOne(
                    new QueryWrapper<WebLevelEntity>().lambda()
                            .eq(WebLevelEntity::getLevelType, 1)
                            .le(WebLevelEntity::getMinBalance, balance)
                            .ge(WebLevelEntity::getMaxBalance, balance)
            );
            salary = userLevel != null ? userLevel.getWeeklySalary() : salary;
        }

        if (salary.doubleValue() == 0) {
            return R.error(MsgUtil.get("error.50002"));
        }
        // 验证每月上限
        DateTime beginMonth = DateUtil.beginOfMonth(now);
        DateTime endMonth = DateUtil.endOfMonth(now);
        List<WebWeeksalaryRecord> salarys = webWeeksalaryRecordService.list(
                new QueryWrapper<WebWeeksalaryRecord>().lambda()
                        .eq(WebWeeksalaryRecord::getUserName, user.getUserName())
                        .between(WebWeeksalaryRecord::getCreateTime, beginMonth, endMonth)
        );
        double salerySum = salarys.stream().mapToDouble(report -> report.getAmount().doubleValue()).sum();
        if (salerySum >= 195000) {
            return R.error(MsgUtil.get("error.50002"));
        }
        if (NumberUtil.add(salerySum, salary.doubleValue()) > 195000) {
            salary = NumberUtil.sub(new BigDecimal(195000), salerySum);
        }

        // 加钱
        webUserService.updateUserBalance(userName, salary);
        // 流水记录
        WebFundRecordEntity fund = new WebFundRecordEntity();
        fund.setUserName(userName);
        fund.setSerialNo(IdUtils.randomId());
        fund.setRefBillNo(null);
        fund.setAmount(salary);
        fund.setBeforeAmount(user.getBalance());
        fund.setAfterAmount(NumberUtil.add(user.getBalance(), salary));
        fund.setType(Constant.FundType.WEEK_SALARY.getValue());
        fund.setCreateTime(now);
        fund.setAgent(user.getAgent());
        fund.setAgentNode(user.getAgentNode());
        fund.setAgentLevel(user.getAgentLevel());
        webFundRecordService.save(fund);

        WebWeeksalaryRecord week = new WebWeeksalaryRecord();
        week.setUserName(userName);
        week.setToday(DateUtil.today());
        week.setAmount(salary);
        week.setCreateTime(now);
        webWeeksalaryRecordService.save(week);
        return R.ok().put("data", MathUtil.scale(salary));
    }

    /**
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "抽手机活动详情")
    @PostMapping("/luckyspin/detail")
    public R luckyspinDetail(HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        // 抽奖初始化次数
        int count = 6;
        // 获取抽奖次数
        List<WebLuckySpin> list = webLuckySpinService.list(new QueryWrapper<WebLuckySpin>().lambda().eq(WebLuckySpin::getUserName, userName).orderByAsc(WebLuckySpin::getId));

        Date now = new Date();
        // 获取任务进度
        int mission1 = webUserService.count(new QueryWrapper<WebUserEntity>().lambda().eq(WebUserEntity::getAgent, userName));
        mission1 = mission1 >= 3 ? 3 : mission1;
        int mission2 = webTopupService.count(new QueryWrapper<WebTopup>().lambda().eq(WebTopup::getUserName, userName).eq(WebTopup::getStatus, 2));
        mission2 = mission2 >= 5 ? 5 : mission2;
        int mission3 = 0;

        JSONObject obj = new JSONObject();
        obj.put("activityStatus", false);
        obj.put("time", 0);
        if (CollUtil.isNotEmpty(list)) {
            WebLuckySpin lucky = list.get(0);
            // 活动结束时间
            DateTime dateTime = DateUtil.offsetDay(lucky.getCreateTime(), 1);
            System.out.println(now.getTime() < dateTime.getTime());
            obj.put("activityStatus", now.getTime() < dateTime.getTime());
            obj.put("time", now.getTime() < dateTime.getTime() ? DateUtil.between(now, dateTime, DateUnit.SECOND) : 0);
        }

        obj.put("mission1", mission1);
        obj.put("mission2", mission2);
        obj.put("mission3", mission3);

        count = count + mission1 + mission2 + mission3;
        obj.put("count", count);
        obj.put("useCount", list.size());

        JSONObject obj1 = new JSONObject();
        obj1.put("prize1", list.stream().filter(prize -> StringUtils.equals(prize.getPrize(), "1")).count());
        obj1.put("prize2", list.stream().filter(prize -> StringUtils.equals(prize.getPrize(), "2")).count());
        obj1.put("prize3", list.stream().filter(prize -> StringUtils.equals(prize.getPrize(), "3")).count());
        obj1.put("prize4", list.stream().filter(prize -> StringUtils.equals(prize.getPrize(), "4")).count());
        obj1.put("prize5", list.stream().filter(prize -> StringUtils.equals(prize.getPrize(), "5")).count());
        obj1.put("prize6", list.stream().filter(prize -> StringUtils.equals(prize.getPrize(), "6")).count());
        obj1.put("prize7", list.stream().filter(prize -> StringUtils.equals(prize.getPrize(), "7")).count());
        obj1.put("prize8", list.stream().filter(prize -> StringUtils.equals(prize.getPrize(), "8")).count());
        obj1.put("prize9", list.stream().filter(prize -> StringUtils.equals(prize.getPrize(), "9")).count());
        obj.put("prize", obj1);

        return R.ok().put("data", obj);
    }

    /**
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "抽手机活动开始")
    @PostMapping("/luckyspin")
    public R luckyspin(HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        String key = "luckyspin:user:lock:" + userName;
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(key, "1", 5, TimeUnit.SECONDS);
        if (!lock) {
            return R.error();
        }

        // 验证次数
        int count = 6;
        // 获取次数
        // 注册下级
        int mission1 = webUserService.count(new QueryWrapper<WebUserEntity>().lambda().eq(WebUserEntity::getAgent, userName));
        mission1 = mission1 >= 3 ? 3 : mission1;
        int mission2 = webTopupService.count(new QueryWrapper<WebTopup>().lambda().eq(WebTopup::getUserName, userName).eq(WebTopup::getStatus, 2));
        mission2 = mission2 >= 5 ? 5 : mission2;
        int mission3 = 0;
        count = mission1 + mission2 + mission3 + count;

        int useCount = webLuckySpinService.count(new QueryWrapper<WebLuckySpin>().lambda().eq(WebLuckySpin::getUserName, userName));
        if (useCount >= count) {
            return R.error(MsgUtil.get("error.50003"));
        }

        // 获取奖品列表
        List<WebLuckySpinConfig> configs = webLuckySpinConfigService.list();
        List<WeightRandom.WeightObj<WebLuckySpinConfig>> weightObjs = new ArrayList<>();
        for (WebLuckySpinConfig config : configs) {
            // 概率为0不参与抽奖
            if (config.getProbability().intValue() == 0) {
                continue;
            }
            weightObjs.add(new WeightRandom.WeightObj(config, config.getProbability()));
        }
        // 根据权重获取对应奖品
        WeightRandom<WebLuckySpinConfig> random = RandomUtil.weightRandom(weightObjs);
        // 对应奖品
        WebLuckySpinConfig prize = random.next();

        WebLuckySpin lucky = new WebLuckySpin();
        lucky.setUserName(userName);
        lucky.setPrize(prize.getPrize());
        lucky.setCreateTime(new Date());
        webLuckySpinService.save(lucky);

        // 解锁
        redisTemplate.delete(key);
        return R.ok().put("prize", prize.getPrize());
    }

    /**
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "抽手机活动收集")
    @PostMapping("/luckyspin/collect")
    public R luckyspinCollect(HttpServletRequest request) {
        return R.error("Insufficient pieces, collection failed");
    }

    /**
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "抽手机中奖列表")
    @PostMapping("/luckyspin/WinnersList")
    public R luckyspinWinnerList(HttpServletRequest request) {
        String prize = "SAMSUNG Galaxy S21 Ultra";
        JSONArray arr = new JSONArray();
        JSONObject obj1 = new JSONObject();
        obj1.put("phone", "57***61");
        obj1.put("prize", prize);
        arr.add(obj1);

        JSONObject obj2 = new JSONObject();
        obj2.put("phone", "60***83");
        obj2.put("prize", prize);
        arr.add(obj2);

        JSONObject obj3 = new JSONObject();
        obj3.put("phone", "51***43");
        obj3.put("prize", prize);
        arr.add(obj3);

        JSONObject obj4 = new JSONObject();
        obj4.put("phone", "74***23");
        obj4.put("prize", prize);
        arr.add(obj4);

        JSONObject obj5 = new JSONObject();
        obj5.put("phone", "67***00");
        obj5.put("prize", prize);
        arr.add(obj5);

        JSONObject obj6 = new JSONObject();
        obj6.put("phone", "68***97");
        obj6.put("prize", prize);
        arr.add(obj6);

        JSONObject obj7 = new JSONObject();
        obj7.put("phone", "76***74");
        obj7.put("prize", prize);
        arr.add(obj7);

        JSONObject obj8 = new JSONObject();
        obj8.put("phone", "53***06");
        obj8.put("prize", prize);
        arr.add(obj8);

        JSONObject obj9 = new JSONObject();
        obj9.put("phone", "59***91");
        obj9.put("prize", prize);
        arr.add(obj9);

        JSONObject obj10 = new JSONObject();
        obj10.put("phone", "79***75");
        obj10.put("prize", prize);
        arr.add(obj10);
        return R.ok().put("list", arr);
    }
}
