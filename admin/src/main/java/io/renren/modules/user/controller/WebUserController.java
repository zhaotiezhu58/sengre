package io.renren.modules.user.controller;
import java.util.Date;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.renren.common.commons.Constant;
import io.renren.common.utils.*;
import io.renren.modules.order.entity.WebFundRecordEntity;
import io.renren.modules.order.entity.WebWithdrawEntity;
import io.renren.modules.order.pojo.WithdrawExport;
import io.renren.modules.order.service.WebFundRecordService;
import io.renren.modules.order.service.WebTopupService;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.entity.WebDictEntity;
import io.renren.modules.sys.service.WebParamsService;
import io.renren.modules.user.entity.UserAgentCountExport;
import io.renren.modules.user.entity.UserExport;
import io.renren.modules.user.entity.WebDayReportEntity;
import io.renren.modules.user.pojo.KoukuanRequest;
import io.renren.modules.user.pojo.TopupRequest;
import io.renren.modules.user.service.WebDayReportService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.user.entity.WebUserEntity;
import io.renren.modules.user.service.WebUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 16:07:36
 */
@RestController
@RequestMapping("user/webuser")
public class WebUserController extends AbstractController {
    @Autowired
    private WebUserService webUserService;

    @Autowired
    private WebDayReportService webDayReportService;

    @Autowired
    private WebFundRecordService webFundRecordService;

    @Autowired
    private WebTopupService webTopupService;

    @Autowired
    private WebParamsService webParamsService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("user:webuser:list")
    public R list(@RequestParam Map<String, Object> params){
        SysUserEntity sysUser = getUser();
        String tgName = sysUser.getTgName();
        WebUserEntity agentUser = webUserService.getOne(
                new QueryWrapper<WebUserEntity>().lambda()
                        .eq(WebUserEntity::getUserName, tgName)
        );
        if (agentUser == null) {
            return R.error("当前后台账号未绑定推广账号.");
        }
        if (agentUser.getAgentLevel() > 1) {
            params.put("userNode", "|" + agentUser.getUserName() + "|");
        }

        PageUtils page = webUserService.queryPage(params);

        List<WebUserEntity> list = (List<WebUserEntity>) page.getList();
        if (CollUtil.isNotEmpty(list)) {
            List<String> userNames = list.stream().map(WebUserEntity::getUserName).collect(Collectors.toList());
            List<WebDayReportEntity> reports = webDayReportService.list(new QueryWrapper<WebDayReportEntity>().lambda().in(WebDayReportEntity::getUserName, userNames));
            String today = DateUtil.today();
            for (WebUserEntity user : list) {
                JSONObject other = new JSONObject();
                // 今日充值
                double toDayTopUp = reports.stream().filter(
                        report -> StringUtils.equals(user.getUserName(), report.getUserName()) && StringUtils.equals(DateUtil.formatDate(report.getToDay()), today)).mapToDouble(report -> report.getTopUp().doubleValue()).sum();
                other.put("toDayTopUp", MathUtil.scale(toDayTopUp));

                // 总充值
                double totalTopUp = reports.stream().filter(report -> StringUtils.equals(user.getUserName(), report.getUserName())).mapToDouble(report -> report.getTopUp().doubleValue()).sum();
                other.put("totalTopUp", MathUtil.scale(totalTopUp));

                // 今日投注
                double toDayBet = reports.stream().filter(
                        report -> StringUtils.equals(user.getUserName(), report.getUserName()) && StringUtils.equals(DateUtil.formatDate(report.getToDay()), today)).mapToDouble(report -> report.getBet().doubleValue()).sum();
                other.put("toDayBet", MathUtil.scale(toDayBet));
                // 总投注
                double totalBet = reports.stream().filter(report -> StringUtils.equals(user.getUserName(), report.getUserName())).mapToDouble(report -> report.getBet().doubleValue()).sum();
                other.put("totalBet", MathUtil.scale(totalBet));

                // 今日收益
                double toDayIncome = reports.stream().filter(
                        report -> StringUtils.equals(user.getUserName(), report.getUserName()) && StringUtils.equals(DateUtil.formatDate(report.getToDay()), today)).mapToDouble(report -> report.getInCome().doubleValue()).sum();
                other.put("toDayIncome", MathUtil.scale(toDayIncome));
                // 总收益
                double totalIncome = reports.stream().filter(report -> StringUtils.equals(user.getUserName(), report.getUserName())).mapToDouble(report -> report.getInCome().doubleValue()).sum();
                other.put("totalIncome", MathUtil.scale(totalIncome));

                // 今日佣金
                double toDayCommission = reports.stream().filter(
                        report -> StringUtils.equals(user.getUserName(), report.getUserName()) && StringUtils.equals(DateUtil.formatDate(report.getToDay()), today)).mapToDouble(report -> report.getCommission().doubleValue()).sum();
                other.put("toDayCommission", MathUtil.scale(toDayCommission));
                // 总佣金
                double totalCommission = reports.stream().filter(report -> StringUtils.equals(user.getUserName(), report.getUserName())).mapToDouble(report -> report.getCommission().doubleValue()).sum();
                other.put("totalCommission", MathUtil.scale(totalCommission));

                // 今日提现
                double toDayWithdraw = reports.stream().filter(
                        report -> StringUtils.equals(user.getUserName(), report.getUserName()) && StringUtils.equals(DateUtil.formatDate(report.getToDay()), today)).mapToDouble(report -> report.getWithdraw().doubleValue()).sum();
                other.put("toDayWithdraw", MathUtil.scale(toDayWithdraw));
                // 总提现
                double totalWithdraw = reports.stream().filter(report -> StringUtils.equals(user.getUserName(), report.getUserName())).mapToDouble(report -> report.getCommission().doubleValue()).sum();
                other.put("totalWithdraw", MathUtil.scale(totalWithdraw));

                user.setOther(other);
            }
        }
        return R.ok().put("page", page);
    }

    /**
     * 节点
     */
    @RequestMapping("/manage")
    @RequiresPermissions("user:webuser:manage")
    public R manage(@RequestParam Map<String, Object> params){
        String userName = MapUtil.getStr(params, "userName");
        if (StringUtils.isEmpty(userName)) {
            SysUserEntity sysUser = getUser();
            userName = sysUser.getTgName();

        }
        JSONObject repose = new JSONObject();
        WebUserEntity user = webUserService.getOne(
                new QueryWrapper<WebUserEntity>().lambda()
                        .eq(WebUserEntity::getUserName, userName)
        );
        JSONObject currUser = new JSONObject();
        currUser.put("userName", user.getUserName());
        // 查询所有直接下级
        List<WebUserEntity> agent1 = webUserService.list(new QueryWrapper<WebUserEntity>().lambda().eq(WebUserEntity::getAgent, user.getUserName()));
        currUser.put("count", agent1.size());
        repose.put("currUser", currUser);

        JSONArray arr = new JSONArray();
        if (CollUtil.isNotEmpty(agent1)) {
            Set<String> names = agent1.stream().map(WebUserEntity::getUserName).collect(Collectors.toSet());
            // 查询直接下级是否还有下级
            List<WebUserEntity> agent2 = webUserService.list(new QueryWrapper<WebUserEntity>().lambda().in(WebUserEntity::getAgent, names));
            Map<String, Long> countMap = agent2.stream().collect(Collectors.groupingBy(WebUserEntity::getAgent, Collectors.counting()));

            for (WebUserEntity temp : agent1) {
                JSONObject obj = new JSONObject();
                obj.put("userName", temp.getUserName());
                obj.put("count", countMap.getOrDefault(temp.getUserName(), 0L));
                arr.add(obj);
            }
        }
        repose.put("nextLevelUsers", arr);
        return R.ok().put("data", repose);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("user:webuser:info")
    public R info(@PathVariable("id") Integer id){
		WebUserEntity webUser = webUserService.getById(id);

        return R.ok().put("webUser", webUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("user:webuser:save")
    public R save(HttpServletRequest request, @RequestBody WebUserEntity webUser){

        String countryCode = webParamsService.getParamsValue("country_code");
        String userName = webUser.getUserName();
        // 校验手机号码格式
        boolean phoneValid = PhoneUtil.validPhone(countryCode, userName);
        boolean emailValid = Validator.isEmail(userName);
        if (!phoneValid && !emailValid) {
            return R.error(StrUtil.format("请输入符合国家区号[{}]的手机号码或邮箱", countryCode));
        }

        boolean loginPwdMatch = ReUtil.isMatch("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$", webUser.getLoginPwd());
        if (!loginPwdMatch) {
            return R.error("请输入6-12位数字、字母组合的密码");
        }

        SysUserEntity sysUser = getUser();
        String tgName = sysUser.getTgName();
        WebUserEntity agentUser = webUserService.getOne(
                new QueryWrapper<WebUserEntity>().lambda()
                        .eq(WebUserEntity::getUserName, tgName)
        );
        if (agentUser == null) {
            return R.error("当前后台账号未绑定推广账号.");
        }

        WebUserEntity temp = new WebUserEntity();
        temp.setAreaCode(countryCode);
        temp.setUserName(webUser.getUserName());
        temp.setPhone(null);
        temp.setEmail(null);
        temp.setBalance(new BigDecimal("0"));
        temp.setVirtualBalance(new BigDecimal("0"));
        temp.setUnlockVirtualBalance(new BigDecimal("0"));
        temp.setLoginPwd(SecureUtil.md5(webUser.getLoginPwd()));
        temp.setInviteCode(RandomUtil.randomString(6));
        temp.setStatus(1);
        temp.setAgent(agentUser.getUserName());
        temp.setAgentNode(agentUser.getAgentNode() + webUser.getUserName() + "|");
        temp.setAgentLevel(agentUser.getAgentLevel() + 1);
        temp.setRegTime(new Date());
        temp.setRegIp(ServletUtil.getClientIP(request));
        temp.setLoginTime(null);
        temp.setLoginIp(null);
        temp.setRemake(null);
        //temp.setOther(new JSONObject());

		webUserService.save(temp);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("user:webuser:update")
    public R update(@RequestBody WebUserEntity webUser){
		//webUserService.updateById(webUser);



        if (StringUtils.isNotBlank(webUser.getLoginPwd())) {
            boolean loginPwdMatch = ReUtil.isMatch("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$", webUser.getLoginPwd());
            if (!loginPwdMatch) {
                return R.error("请输入6-12位数字、字母组合的密码");
            }
        }

        webUserService.update(
                new UpdateWrapper<WebUserEntity>().lambda()
                        .eq(WebUserEntity::getId, webUser.getId())
                        .set(StringUtils.isNotEmpty(webUser.getLoginPwd()), WebUserEntity::getLoginPwd, SecureUtil.md5(webUser.getLoginPwd()))
                        .set(StringUtils.isNotEmpty(webUser.getRemake()), WebUserEntity::getRemake, webUser.getRemake())
                        .set(webUser != null, WebUserEntity::getStatus, webUser.getStatus())
                        .set(WebUserEntity::getModifyTime, new Date())
        );

        WebUserEntity user = webUserService.getById(webUser.getId());
        String incKey = RedisKeyUtil.LoginPwdErrorKey(user.getUserName());
        stringRedisTemplate.delete(incKey);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/updateStatus")
    @RequiresPermissions("user:webuser:update")
    public R updateStatus(@RequestBody WebUserEntity webUser){
        webUserService.updateById(webUser);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("user:webuser:delete")
    public R delete(@RequestBody Integer[] ids){
		webUserService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 扣款
     */
    @Transactional
    @RequestMapping("/koukuan")
    @RequiresPermissions("user:webuser:koukuan")
    public R koukuan(@RequestBody KoukuanRequest request) throws Exception {

        String amountStr = request.getAmount();
        WebUserEntity user = webUserService.getById(request.getId());
        if (user == null) {
            return R.error("用户不存在!");
        }
        BigDecimal amount = new BigDecimal(amountStr);
        if(user.getBalance().doubleValue() < amount.doubleValue()) {
            return R.error("扣款余额不能大于用户余额");
        }
        Date now = new Date();
        // 扣钱
        webUserService.updateUserBalance(user.getUserName(), amount.negate());
        // 流水
        WebFundRecordEntity fund = new WebFundRecordEntity();
        fund.setUserName(user.getUserName());
        fund.setSerialNo(IdUtils.randomId());
        fund.setRefBillNo(null);
        fund.setAmount(amount.negate());
        fund.setBeforeAmount(user.getBalance());
        fund.setAfterAmount(NumberUtil.sub(user.getBalance(), amount));
        fund.setType(Constant.FundType.ADMIN_SUB.getValue());
        fund.setCreateTime(now);
        fund.setAgent(user.getAgent());
        fund.setAgentNode(user.getAgentNode());
        fund.setAgentLevel(user.getAgentLevel());
        webFundRecordService.save(fund);
        return R.ok();
    }

    /**
     * 充值
     */
    @Transactional
    @RequestMapping("/topup")
    @RequiresPermissions("user:webuser:topup")
    public R topup(@RequestBody TopupRequest request) throws Exception {
        String amountStr = request.getAmount();

        Date now = new Date();

        WebUserEntity user = webUserService.getById(request.getId());
        if (user == null) {
            return R.error("用户不存在!");
        }
        BigDecimal amount = new BigDecimal(amountStr);

        // 加钱
        webUserService.updateUserBalance(user.getUserName(), amount);
        // 流水
        WebFundRecordEntity fund = new WebFundRecordEntity();
        fund.setUserName(user.getUserName());
        fund.setSerialNo(IdUtils.randomId());
        fund.setRefBillNo(null);
        fund.setAmount(amount);
        fund.setBeforeAmount(user.getBalance());
        fund.setAfterAmount(NumberUtil.add(user.getBalance(), amount));
        fund.setType(Constant.FundType.ADMIN_ADD.getValue());
        fund.setCreateTime(now);
        fund.setAgent(user.getAgent());
        fund.setAgentNode(user.getAgentNode());
        fund.setAgentLevel(user.getAgentLevel());
        webFundRecordService.save(fund);
        return R.ok();
    }

    /**
     * 导出
     */
    @RequestMapping("/export")
    @RequiresPermissions("user:webuser:export")
    public R export(@RequestParam Map<String, Object> params, HttpServletResponse response) throws IOException {
        SysUserEntity sysUser = getUser();
        String tgName = sysUser.getTgName();
        WebUserEntity agentUser = webUserService.getOne(
                new QueryWrapper<WebUserEntity>().lambda()
                        .eq(WebUserEntity::getUserName, tgName)
        );
        if (agentUser == null) {
            return R.error("当前后台账号未绑定推广账号.");
        }
        if (agentUser.getAgentLevel() > 1) {
            params.put("userNode", "|" + agentUser.getUserName() + "|");
        }

        List<WebUserEntity> list = webUserService.queryList(params);
        if (CollUtil.isEmpty(list)) {
            return R.error("未查询到数据");
        }

        List<UserExport> users = new ArrayList<>();
        for (WebUserEntity user : list) {
            UserExport temp = new UserExport();
            temp.setUserName(user.getUserName());
            temp.setBalance(user.getBalance());
            temp.setVirtualBalance(user.getVirtualBalance());
            temp.setInviteCode(user.getInviteCode());
            temp.setAgent(user.getAgent());
            temp.setRegTime(user.getRegTime());
            temp.setRegIp(user.getRegIp());
            temp.setLoginTime(user.getLoginTime());
            temp.setLoginIp(user.getLoginIp());
            temp.setRemake(user.getRemake());
            users.add(temp);
        }

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(DateUtil.format(new Date(), "yyyyMMddHHmmssSSS"), "UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
        EasyExcel.write(
                        response.getOutputStream(), UserExport.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("sheet1")
                .doWrite(users);

        return R.ok();
    }

    /**
     * 导出
     */
    @RequestMapping("/exportAgentCount")
    @RequiresPermissions("user:webuser:exportcount")
    public R exportAgentCount(@RequestParam Map<String, Object> params, HttpServletResponse response) throws IOException {
        SysUserEntity sysUser = getUser();
        String tgName = sysUser.getTgName();
        WebUserEntity agentUser = webUserService.getOne(
                new QueryWrapper<WebUserEntity>().lambda()
                        .eq(WebUserEntity::getUserName, tgName)
        );
        if (agentUser == null) {
            return R.error("当前后台账号未绑定推广账号.");
        }
        if (agentUser.getAgentLevel() > 1) {
            params.put("userNode", "|" + agentUser.getUserName() + "|");
        }

        List<WebUserEntity> list = webUserService.list(
                new QueryWrapper<WebUserEntity>().lambda()
                        .like(WebUserEntity::getAgentNode, "|" + agentUser.getUserName() + "|")
                        .orderByAsc(WebUserEntity::getAgentLevel)
        );

        if (CollUtil.isEmpty(list)) {
            return R.error("未查询到数据");
        }

        List<UserAgentCountExport> agents = new ArrayList<>();
        for (WebUserEntity user : list) {
            UserAgentCountExport temp = new UserAgentCountExport();
            temp.setUserName(user.getUserName());
            long count = list.stream().filter(webUserEntity -> StringUtils.equals(webUserEntity.getAgent(), user.getUserName())).count();
            temp.setCount(count);
            if (count <= 0) {
                continue;
            }
            agents.add(temp);
        }

        agents.sort(Comparator.comparing(UserAgentCountExport::getCount).reversed());

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(DateUtil.format(new Date(), "yyyyMMddHHmmssSSS"), "UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
        EasyExcel.write(
                        response.getOutputStream(), UserAgentCountExport.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("sheet1")
                .doWrite(agents);

        return R.ok();
    }
}
