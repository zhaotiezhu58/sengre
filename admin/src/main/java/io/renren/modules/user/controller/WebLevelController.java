package io.renren.modules.user.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.user.entity.WebLevelEntity;
import io.renren.modules.user.service.WebLevelService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 16:40:10
 */
@RestController
@RequestMapping("user/weblevel")
public class WebLevelController {
    @Autowired
    private WebLevelService webLevelService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("user:weblevel:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webLevelService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("user:weblevel:info")
    public R info(@PathVariable("id") Integer id){
		WebLevelEntity webLevel = webLevelService.getById(id);

        return R.ok().put("webLevel", webLevel);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("user:weblevel:save")
    public R save(@RequestBody WebLevelEntity webLevel){
		webLevelService.save(webLevel);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("user:weblevel:update")
    public R update(@RequestBody WebLevelEntity webLevel){
		webLevelService.updateById(webLevel);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("user:weblevel:delete")
    public R delete(@RequestBody Integer[] ids){
		webLevelService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
