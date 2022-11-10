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

import io.renren.modules.order.entity.WebVirtualRecordEntity;
import io.renren.modules.order.service.WebVirtualRecordService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 20:50:42
 */
@RestController
@RequestMapping("order/webvirtualrecord")
public class WebVirtualRecordController extends AbstractController {
    @Autowired
    private WebVirtualRecordService webVirtualRecordService;

    @Autowired
    private WebUserService webUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("order:webvirtualrecord:list")
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

        PageUtils page = webVirtualRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("order:webvirtualrecord:info")
    public R info(@PathVariable("id") Integer id){
		WebVirtualRecordEntity webVirtualRecord = webVirtualRecordService.getById(id);

        return R.ok().put("webVirtualRecord", webVirtualRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("order:webvirtualrecord:save")
    public R save(@RequestBody WebVirtualRecordEntity webVirtualRecord){
		webVirtualRecordService.save(webVirtualRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("order:webvirtualrecord:update")
    public R update(@RequestBody WebVirtualRecordEntity webVirtualRecord){
		webVirtualRecordService.updateById(webVirtualRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("order:webvirtualrecord:delete")
    public R delete(@RequestBody Integer[] ids){
		webVirtualRecordService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
