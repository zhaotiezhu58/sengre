package io.renren.modules.order.controller;

import java.util.Arrays;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.user.entity.WebUserEntity;
import io.renren.modules.user.service.WebUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.order.entity.WebCommissionRecordEntity;
import io.renren.modules.order.service.WebCommissionRecordService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 21:26:55
 */
@RestController
@RequestMapping("order/webcommissionrecord")
public class WebCommissionRecordController extends AbstractController {
    @Autowired
    private WebCommissionRecordService webCommissionRecordService;

    @Autowired
    private WebUserService webUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("order:webcommissionrecord:list")
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

        PageUtils page = webCommissionRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("order:webcommissionrecord:info")
    public R info(@PathVariable("id") Integer id){
		WebCommissionRecordEntity webCommissionRecord = webCommissionRecordService.getById(id);

        return R.ok().put("webCommissionRecord", webCommissionRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("order:webcommissionrecord:save")
    public R save(@RequestBody WebCommissionRecordEntity webCommissionRecord){
		webCommissionRecordService.save(webCommissionRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("order:webcommissionrecord:update")
    public R update(@RequestBody WebCommissionRecordEntity webCommissionRecord){
		webCommissionRecordService.updateById(webCommissionRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("order:webcommissionrecord:delete")
    public R delete(@RequestBody Integer[] ids){
		webCommissionRecordService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
