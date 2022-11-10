package io.renren.modules.order.controller;
import java.math.BigDecimal;
import java.util.Date;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.utils.MathUtil;
import io.renren.modules.order.entity.WebFundRecordEntity;
import io.renren.modules.order.entity.WebWithdrawEntity;
import io.renren.modules.order.pojo.FundExport;
import io.renren.modules.order.pojo.TopUpExport;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.entity.WebDictEntity;
import io.renren.modules.sys.service.WebDictService;
import io.renren.modules.user.entity.WebUserEntity;
import io.renren.modules.user.service.WebUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.order.entity.WebTopupEntity;
import io.renren.modules.order.service.WebTopupService;
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
@RestController
@RequestMapping("order/webtopup")
public class WebTopupController extends AbstractController {
    @Autowired
    private WebTopupService webTopupService;

    @Autowired
    private WebUserService webUserService;

    @Autowired
    private WebDictService webDictService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("order:webtopup:list")
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

        PageUtils page = webTopupService.queryPage(params);

        JSONObject obj = new JSONObject();
        double legalLenderPage = 0;
        double VirtualCurrencyPage = 0;
        double legalLenderSum = 0;
        double VirtualCurrencySum = 0;
        if (CollUtil.isNotEmpty(page.getList())) {
            List<WebTopupEntity> pageList = (List<WebTopupEntity>) page.getList();
            legalLenderPage = pageList.stream().filter(order -> order.getType() == 1).mapToDouble(order -> order.getRealAmount().doubleValue()).sum();
            VirtualCurrencyPage = pageList.stream().filter(order -> order.getType() == 2).mapToDouble(order -> order.getRealAmount().doubleValue()).sum();

            List<WebTopupEntity> list = webTopupService.queryList(params);
            legalLenderSum = list.stream().filter(order -> order.getType() == 1).mapToDouble(order -> order.getRealAmount().doubleValue()).sum();
            VirtualCurrencySum = list.stream().filter(order -> order.getType() == 2).mapToDouble(order -> order.getRealAmount().doubleValue()).sum();
        }

        obj.set("legalLenderSum", MathUtil.scale(legalLenderSum));
        obj.set("VirtualCurrencySum", MathUtil.scale(VirtualCurrencySum));
        obj.set("legalLenderPage", MathUtil.scale(legalLenderPage));
        obj.set("VirtualCurrencyPage", MathUtil.scale(VirtualCurrencyPage));
        return R.ok().put("page", page).put("report", obj);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("order:webtopup:info")
    public R info(@PathVariable("id") Integer id){
		WebTopupEntity webTopup = webTopupService.getById(id);

        return R.ok().put("webTopup", webTopup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("order:webtopup:save")
    public R save(@RequestBody WebTopupEntity webTopup){
		webTopupService.save(webTopup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("order:webtopup:update")
    public R update(@RequestBody WebTopupEntity webTopup){
		webTopupService.updateById(webTopup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("order:webtopup:delete")
    public R delete(@RequestBody Integer[] ids){
		webTopupService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 导出
     */
    @RequestMapping("/export")
    @RequiresPermissions("order:webtopup:export")
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

        List<WebTopupEntity> list = webTopupService.queryList(params);
        if (CollUtil.isEmpty(list)) {
            return R.error("未查询到数据");
        }

        List<WebDictEntity> dict = webDictService.list(new QueryWrapper<WebDictEntity>().lambda().eq(WebDictEntity::getStatus, 1).eq(WebDictEntity::getType, 1));
        Map<String, String> dictMap = dict.stream().collect(Collectors.toMap(WebDictEntity::getDictKey, dictEntity -> dictEntity.getDictValue()));

        List<TopUpExport> tops = new ArrayList<>();
        for (WebTopupEntity top : list) {
            TopUpExport temp = new TopUpExport();
            temp.setOrderNo(top.getOrderNo());
            temp.setUserName(top.getUserName());
            temp.setAmount(top.getAmount());
            temp.setRealAmount(top.getRealAmount());
            if (top.getType().intValue() == 1) {
                temp.setType("法定货币");
            } else if (top.getType().intValue() == 2) {
                temp.setType("虚拟货币");
            } else {
                temp.setType("其他");
            }
            temp.setPayOrderNo(top.getPayOrderNo());
            temp.setIp(top.getIp());
            temp.setCreateTime(top.getCreateTime());
            temp.setModifyTime(top.getModifyTime());
            temp.setStatus(dictMap.getOrDefault(top.getStatus().toString(), "未知"));
            temp.setMerchantCode(top.getMerchantCode());
            temp.setChannelCode(top.getChannelCode());
            temp.setAgent(top.getAgent());
            tops.add(temp);
        }

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(DateUtil.format(new Date(), "yyyyMMddHHmmssSSS"), "UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
        EasyExcel.write(
                        response.getOutputStream(), TopUpExport.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("sheet1")
                .doWrite(tops);

        return R.ok();
    }
}
