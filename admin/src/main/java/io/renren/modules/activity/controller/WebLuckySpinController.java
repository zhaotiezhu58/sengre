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

import io.renren.modules.activity.entity.WebLuckySpinEntity;
import io.renren.modules.activity.service.WebLuckySpinService;
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
@RequestMapping("activity/webluckyspin")
public class WebLuckySpinController {
    @Autowired
    private WebLuckySpinService webLuckySpinService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("activity:webluckyspin:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webLuckySpinService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("activity:webluckyspin:info")
    public R info(@PathVariable("id") Integer id){
		WebLuckySpinEntity webLuckySpin = webLuckySpinService.getById(id);

        return R.ok().put("webLuckySpin", webLuckySpin);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("activity:webluckyspin:save")
    public R save(@RequestBody WebLuckySpinEntity webLuckySpin){
		webLuckySpinService.save(webLuckySpin);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("activity:webluckyspin:update")
    public R update(@RequestBody WebLuckySpinEntity webLuckySpin){
		webLuckySpinService.updateById(webLuckySpin);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("activity:webluckyspin:delete")
    public R delete(@RequestBody Integer[] ids){
		webLuckySpinService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
