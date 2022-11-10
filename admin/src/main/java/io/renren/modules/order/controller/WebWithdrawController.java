package io.renren.modules.order.controller;
import java.util.Date;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.renren.common.commons.Constant;
import io.renren.common.utils.IdUtils;
import io.renren.common.utils.MathUtil;
import io.renren.modules.order.entity.WebFundRecordEntity;
import io.renren.modules.order.entity.WebTopupEntity;
import io.renren.modules.order.pojo.FundExport;
import io.renren.modules.order.pojo.WithdrawCheck;
import io.renren.modules.order.pojo.WithdrawExport;
import io.renren.modules.order.service.WebFundRecordService;
import io.renren.modules.pay.entity.WebPayChannelEntity;
import io.renren.modules.pay.entity.WebPayMerchantEntity;
import io.renren.modules.pay.service.WebPayChannelService;
import io.renren.modules.pay.service.WebPayMerchantService;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.entity.WebDictEntity;
import io.renren.modules.sys.service.WebDictService;
import io.renren.modules.user.entity.WebDayReportEntity;
import io.renren.modules.user.entity.WebUserEntity;
import io.renren.modules.user.service.WebDayReportService;
import io.renren.modules.user.service.WebUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.order.entity.WebWithdrawEntity;
import io.renren.modules.order.service.WebWithdrawService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 18:21:18
 */
@Slf4j
@RestController
@RequestMapping("order/webwithdraw")
public class WebWithdrawController extends AbstractController {
    @Autowired
    private WebWithdrawService webWithdrawService;

    @Autowired
    private WebUserService webUserService;

    @Autowired
    private WebFundRecordService webFundRecordService;

    @Autowired
    private WebPayChannelService webPayChannelService;

    @Autowired
    private WebPayMerchantService webPayMerchantService;

    @Autowired
    private WebDictService webDictService;

    @Autowired
    private WebDayReportService webDayReportService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("order:webwithdraw:list")
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

        PageUtils page = webWithdrawService.queryPage(params);

        JSONObject obj = new JSONObject();
        double legalLenderPage = 0;
        double legalLenderSum = 0;
        double feePage = 0;
        double feeSum = 0;
        if (CollUtil.isNotEmpty(page.getList())) {
            List<WebWithdrawEntity> pageList = (List<WebWithdrawEntity>) page.getList();
            legalLenderPage = pageList.stream().mapToDouble(order -> order.getAmount().doubleValue()).sum();
            feePage = pageList.stream().mapToDouble(order -> order.getFee().doubleValue()).sum();

            List<WebWithdrawEntity> list = webWithdrawService.queryList(params);
            legalLenderSum = list.stream().mapToDouble(order -> order.getAmount().doubleValue()).sum();
            feeSum = list.stream().mapToDouble(order -> order.getFee().doubleValue()).sum();
        }

        obj.set("legalLenderPage", MathUtil.scale(legalLenderPage));
        obj.set("legalLenderSum", MathUtil.scale(legalLenderSum));
        obj.set("feePage", MathUtil.scale(feePage));
        obj.set("feeSum", MathUtil.scale(feeSum));
        return R.ok().put("page", page).put("report", obj);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("order:webwithdraw:info")
    public R info(@PathVariable("id") Integer id){
		WebWithdrawEntity webWithdraw = webWithdrawService.getById(id);

        return R.ok().put("webWithdraw", webWithdraw);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("order:webwithdraw:save")
    public R save(@RequestBody WebWithdrawEntity webWithdraw){
		webWithdrawService.save(webWithdraw);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("order:webwithdraw:update")
    public R update(@RequestBody WebWithdrawEntity webWithdraw){
		webWithdrawService.updateById(webWithdraw);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("order:webwithdraw:delete")
    public R delete(@RequestBody Integer[] ids){
		webWithdrawService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 拒绝
     */
    @Transactional
    @RequestMapping("/reject")
    @RequiresPermissions("order:webwithdraw:check")
    public R reject(@RequestBody Map<String, Object> params) throws Exception {
        String remake = MapUtil.getStr(params, "remake");
        String id = MapUtil.getStr(params, "id");
        WebWithdrawEntity withdraw = webWithdrawService.getOne(
                new QueryWrapper<WebWithdrawEntity>().lambda()
                        .eq(WebWithdrawEntity::getId, id)
                        .eq(WebWithdrawEntity::getStatus, 0)
        );
        if (withdraw == null) {
            return R.error("订单不存在.");
        }
        // 修改订单状态
        boolean update = webWithdrawService.update(
                new UpdateWrapper<WebWithdrawEntity>().lambda()
                        .eq(WebWithdrawEntity::getId, withdraw.getId())
                        .eq(WebWithdrawEntity::getStatus, 0)
                        .set(WebWithdrawEntity::getStatus, -1)
                        .set(WebWithdrawEntity::getModifyTime, new Date())
                        .set(StringUtils.isNotBlank(remake), WebWithdrawEntity::getRemake, remake)
        );
        if (!update) {
            return R.error("修改订单失败");
        }
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
        return R.ok();
    }

    /**
     * 线下支付
     */
    @Transactional
    @RequestMapping("/xianxia")
    @RequiresPermissions("order:webwithdraw:check")
    public R xianxia(@RequestBody Map<String, Object> params) throws Exception {
        String remake = MapUtil.getStr(params, "remake");
        String id = MapUtil.getStr(params, "id");
        WebWithdrawEntity withdraw = webWithdrawService.getOne(
                new QueryWrapper<WebWithdrawEntity>().lambda()
                        .eq(WebWithdrawEntity::getId, id)
                        .eq(WebWithdrawEntity::getStatus, 0)
        );
        if (withdraw == null) {
            return R.error("订单不存在.");
        }
        // 修改订单状态
        boolean update = webWithdrawService.update(
                new UpdateWrapper<WebWithdrawEntity>().lambda()
                        .eq(WebWithdrawEntity::getId, withdraw.getId())
                        .eq(WebWithdrawEntity::getStatus, 0)
                        .set(WebWithdrawEntity::getStatus, 1)
                        .set(WebWithdrawEntity::getModifyTime, new Date())
                        .set(StringUtils.isNotBlank(remake), WebWithdrawEntity::getRemake, remake)
        );
        if (!update) {
            return R.error("修改订单失败");
        }
        WebUserEntity user = webUserService.getOne(new QueryWrapper<WebUserEntity>().lambda().eq(WebUserEntity::getUserName, withdraw.getUserName()));

        // 添加报表
        WebDayReportEntity report = new WebDayReportEntity();
        report.setUserName(user.getUserName());
        report.setToDay(withdraw.getCreateTime());
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
        return R.ok();
    }

    /**
     * 拒绝
     */
    @Transactional
    @RequestMapping("/daifu")
    @RequiresPermissions("order:webwithdraw:check")
    public R daifu(@RequestBody WithdrawCheck check) throws Exception {

        WebWithdrawEntity withdraw = webWithdrawService.getOne(
                new QueryWrapper<WebWithdrawEntity>().lambda()
                        .eq(WebWithdrawEntity::getId, check.getId())
                        .eq(WebWithdrawEntity::getStatus, 0)
        );
        if (withdraw == null) {
            return R.error("订单不存在.");
        }

        // 查询代付通道信息
        WebPayChannelEntity channel = webPayChannelService.getOne(
                new QueryWrapper<WebPayChannelEntity>().lambda()
                        .eq(WebPayChannelEntity::getId, check.getChannelId())
                        .eq(WebPayChannelEntity::getChannelType, 2)
                        .eq(WebPayChannelEntity::getStatus, 1)
        );
        if (channel == null) {
            return R.error("通道不存在");
        }
        double realAmount = channel.getPayType() == 1 ? withdraw.getRealAmount().doubleValue() : withdraw.getVirtualRealAmount().doubleValue();
        if (realAmount < channel.getMinAmount().doubleValue() || realAmount > channel.getMaxAmount().doubleValue()) {
            return R.error("通道额度限制" + channel.getMinAmount() + "-" + channel.getMaxAmount());
        }
        // 获取商户信息
        WebPayMerchantEntity merchant = webPayMerchantService.getOne(new QueryWrapper<WebPayMerchantEntity>().lambda().eq(WebPayMerchantEntity::getMerchantCode, channel.getMerchantCode()));
        if (merchant == null) {
            return R.error("商户不存在");
        }

        String body = request(merchant, channel, withdraw);
        JSONObject obj = JSONUtil.parseObj(body);
        Boolean success = obj.getBool("success");
        if (success) {
            JSONObject data = obj.getJSONObject("data");
            String sysOrderNum = data.getStr("sysOrderNum");
            String sign = data.getStr("sign");
            // 修改订单状态
            webWithdrawService.update(
                    new UpdateWrapper<WebWithdrawEntity>().lambda()
                            .eq(WebWithdrawEntity::getId, withdraw.getId())
                            .eq(WebWithdrawEntity::getStatus, 0)
                            .set(WebWithdrawEntity::getPaySign, sign)
                            .set(WebWithdrawEntity::getPayOrderNo, sysOrderNum)
                            .set(WebWithdrawEntity::getMerchantCode, channel.getMerchantCode())
                            .set(WebWithdrawEntity::getChannelCode, channel.getChannelCode())
                            .set(WebWithdrawEntity::getStatus, -2)
            );
            return R.ok();
        } else {
            return R.error(obj.getStr("msg"));
        }
    }

    private String request(WebPayMerchantEntity merchant, WebPayChannelEntity channel, WebWithdrawEntity withdraw) {
        BigDecimal amount = withdraw.getType() == 1 ? withdraw.getRealAmount() : withdraw.getVirtualRealAmount();
        Map<String, Object> params = new TreeMap<>();
        params.put("merchantId", merchant.getMerchantCode());
        params.put("payMethod", channel.getChannelCode());
        params.put("money", (int)(amount.doubleValue() * 100));
        params.put("bizNum", withdraw.getOrderNo());
        params.put("notifyAddress", merchant.getWithdrawNotifyUrl());
        params.put("name", withdraw.getRealName());
        params.put("mobile", withdraw.getMobile());
        params.put("account", withdraw.getAccount());
        params.put("ifscCode", withdraw.getIfscCode());
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            String value = MapUtil.getStr(params, key);
            if (StringUtils.isNotEmpty(value)) {
                sb.append(key).append("=").append(value.trim()).append("&");
            }
        }

        String queryStr = sb.substring(0, sb.length() - 1);
        String queryStr1 = sb + "key=" + merchant.getMerchantKey();
        String sign = SecureUtil.md5(queryStr1);
        queryStr += "&sign=" + sign.toUpperCase(Locale.ROOT);

        String url = merchant.getWithdrawUrl() + "?" + queryStr;
        HttpRequest request = HttpUtil.createPost(url);
        request.contentType("application/x-www-form-urlencoded");
        log.info("请求参数:{}", request);
        HttpResponse response = request.execute();
        log.info("请求响应:{}", response.body());
        return response.body();
    }

    /**
     * 导出
     */
    @RequestMapping("/export")
    @RequiresPermissions("order:webwithdraw:export")
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

        List<WebWithdrawEntity> list = webWithdrawService.queryList(params);
        if (CollUtil.isEmpty(list)) {
            return R.error("未查询到数据");
        }

        List<WebDictEntity> dict = webDictService.list(new QueryWrapper<WebDictEntity>().lambda().eq(WebDictEntity::getStatus, 1).eq(WebDictEntity::getType, 2));
        Map<String, String> dictMap = dict.stream().collect(Collectors.toMap(WebDictEntity::getDictKey, dictEntity -> dictEntity.getDictValue()));

        List<WithdrawExport> withdraws = new ArrayList<>();
        for (WebWithdrawEntity withdraw : list) {
            WithdrawExport temp = new WithdrawExport();
            temp.setOrderNo(withdraw.getOrderNo());
            temp.setUserName(withdraw.getUserName());
            temp.setMerchantCode(withdraw.getMerchantCode());
            temp.setChannelCode(withdraw.getChannelCode());
            temp.setAmount(withdraw.getAmount());
            temp.setRealAmount(withdraw.getRealAmount());
            temp.setFee(withdraw.getFee());
            temp.setCreateTime(withdraw.getCreateTime());
            temp.setModifyTime(withdraw.getModifyTime());
            temp.setStatus(dictMap.getOrDefault(withdraw.getStatus().toString(), "未知"));
            temp.setAgent(withdraw.getAgent());
            temp.setRemake(withdraw.getRemake());
            temp.setAccount(withdraw.getAccount());
            withdraws.add(temp);
        }

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(DateUtil.format(new Date(), "yyyyMMddHHmmssSSS"), "UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
        EasyExcel.write(
                        response.getOutputStream(), WithdrawExport.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("sheet1")
                .doWrite(withdraws);

        return R.ok();
    }
}
