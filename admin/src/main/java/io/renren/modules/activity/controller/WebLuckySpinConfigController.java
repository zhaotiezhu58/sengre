package io.renren.modules.activity.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.activity.entity.WebLuckySpinConfigEntity;
import io.renren.modules.activity.service.WebLuckySpinConfigService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 21:49:29
 */
@RestController
@RequestMapping("activity/webluckyspinconfig")
public class WebLuckySpinConfigController {
    @Autowired
    private WebLuckySpinConfigService webLuckySpinConfigService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("activity:webluckyspinconfig:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webLuckySpinConfigService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("activity:webluckyspinconfig:info")
    public R info(@PathVariable("id") Integer id){
		WebLuckySpinConfigEntity webLuckySpinConfig = webLuckySpinConfigService.getById(id);

        return R.ok().put("webLuckySpinConfig", webLuckySpinConfig);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("activity:webluckyspinconfig:save")
    public R save(@RequestBody WebLuckySpinConfigEntity webLuckySpinConfig){
		webLuckySpinConfigService.save(webLuckySpinConfig);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("activity:webluckyspinconfig:update")
    public R update(@RequestBody WebLuckySpinConfigEntity webLuckySpinConfig){
		webLuckySpinConfigService.updateById(webLuckySpinConfig);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("activity:webluckyspinconfig:delete")
    public R delete(@RequestBody Integer[] ids){
		webLuckySpinConfigService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
