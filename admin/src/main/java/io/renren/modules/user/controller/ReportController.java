package io.renren.modules.user.controller;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.utils.MathUtil;
import io.renren.common.utils.R;
import io.renren.modules.order.entity.WebTopupEntity;
import io.renren.modules.order.entity.WebWithdrawEntity;
import io.renren.modules.order.service.WebTopupService;
import io.renren.modules.order.service.WebWithdrawService;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.user.entity.WebUserEntity;
import io.renren.modules.user.service.WebUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-03-03 19:02:51
 */
@RestController
@RequestMapping("report/report")
public class ReportController extends AbstractController {
    @Autowired
    private WebUserService webUserService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private WebWithdrawService webWithdrawService;

    @Autowired
    private WebTopupService webTopupService;

    /**
     * 报表详情
     */
    @RequestMapping("/detail")
    @RequiresPermissions("report:report:list")
    public R list(@RequestParam Map<String, Object> params){

        SysUserEntity sysUser = getUser();
        String tgName = sysUser.getTgName();
        WebUserEntity queryUser = webUserService.getOne(
                new QueryWrapper<WebUserEntity>().lambda()
                        .eq(WebUserEntity::getUserName, tgName)
        );
        if (queryUser == null) {
            return R.error("当前后台账号未绑定推广账号.");
        }
        //String userName = MapUtil.getStr(params, "userName", "");
        //WebUserEntity queryUser = null;
        //if (StringUtils.isBlank(userName)) {
        //    queryUser = webUserService.getOne(new QueryWrapper<WebUserEntity>().lambda().eq(WebUserEntity::getAgentLevel, 1));
        //}else {
        //    queryUser = webUserService.getOne(new QueryWrapper<WebUserEntity>().lambda().eq(WebUserEntity::getUserName, userName));
        //    if (queryUser == null) {
        //        return R.error("用户不存在");
        //    }
        //}

        String queryUserName = "";
        String queryUserNode = "|" + queryUser.getUserName() + "|";

        String startTime = MapUtil.getStr(params, "startTime");
        String endTime = MapUtil.getStr(params, "endTime");

        JSONObject obj = new JSONObject();
        // 线下会员总数
        int memberCount = webUserService.count(
                new QueryWrapper<WebUserEntity>().lambda()
                        .like(StringUtils.isNotBlank(queryUserNode), WebUserEntity::getAgentNode, queryUserNode)
        );
        obj.put("memberCount", memberCount);

        // 注册人数
        int registerCount = webUserService.count(
                new QueryWrapper<WebUserEntity>().lambda()
                        .like(StringUtils.isNotBlank(queryUserNode), WebUserEntity::getAgentNode, queryUserNode)
                        .gt(StringUtils.isNotBlank(startTime), WebUserEntity::getRegTime, startTime)
                        .lt(StringUtils.isNotBlank(endTime), WebUserEntity::getRegTime, endTime)
        );
        obj.put("registerCount", registerCount);

        // 当前在线人数
        Set<String> onlineCount = stringRedisTemplate.keys("user:online:token:*");
        obj.put("onlineCount", onlineCount.size());

        // 会员总金额
        List<WebUserEntity> users = webUserService.list(
                new QueryWrapper<WebUserEntity>().lambda()
                        .like(StringUtils.isNotBlank(queryUserNode), WebUserEntity::getAgentNode, queryUserNode)
        );
        double memberBalance = users.stream().mapToDouble(user -> user.getBalance().doubleValue()).sum();
        obj.put("memberBalance", MathUtil.scale(memberBalance));

        // 提现总计
        List<WebWithdrawEntity> withdraws = webWithdrawService.list(
                new QueryWrapper<WebWithdrawEntity>().lambda()
                        .like(StringUtils.isNotBlank(queryUserNode), WebWithdrawEntity::getAgentNode, queryUserNode)
                        .eq(WebWithdrawEntity::getStatus, 1)
                        .gt(StringUtils.isNotBlank(startTime), WebWithdrawEntity::getCreateTime, startTime)
                        .lt(StringUtils.isNotBlank(endTime), WebWithdrawEntity::getCreateTime, endTime)
        );
        double withdrawBalance = withdraws.stream().mapToDouble(withdraw -> withdraw.getAmount().doubleValue()).sum();
        obj.put("withdrawBalance", MathUtil.scale(withdrawBalance));
        double withdrawFee = withdraws.stream().mapToDouble(withdraw -> withdraw.getFee().doubleValue()).sum();
        obj.put("withdrawFee", MathUtil.scale(withdrawFee));
        Set<String> withdrawMembers = withdraws.stream().map(WebWithdrawEntity::getUserName).collect(Collectors.toSet());
        obj.put("withdrawMembers", withdrawMembers.size());
        obj.put("withdraws", withdraws.size());

        // 充值
        List<WebTopupEntity> topups = webTopupService.list(
                new QueryWrapper<WebTopupEntity>().lambda()
                        .like(StringUtils.isNotBlank(queryUserNode), WebTopupEntity::getAgentNode, queryUserNode)
                        .eq(WebTopupEntity::getStatus, 2)
                        .gt(StringUtils.isNotBlank(startTime), WebTopupEntity::getCreateTime, startTime)
                        .lt(StringUtils.isNotBlank(endTime), WebTopupEntity::getCreateTime, endTime)
        );
        double topup = topups.stream().mapToDouble(topUp -> topUp.getRealAmount().doubleValue()).sum();
        obj.put("topup", MathUtil.scale(topup));
        Set<String> topupMembers = topups.stream().map(WebTopupEntity::getUserName).collect(Collectors.toSet());
        obj.put("topupMembers", topupMembers.size());
        obj.put("topups", topups.size());

        obj.put("income", MathUtil.scale(topup - (withdrawBalance - withdrawFee)));
        return R.ok().put("data", obj);
    }

}
