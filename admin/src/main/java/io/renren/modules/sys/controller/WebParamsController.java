package io.renren.modules.sys.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.modules.sys.entity.WebParamsEntity;
import io.renren.modules.sys.service.WebParamsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-03-03 18:43:48
 */
@RestController
@RequestMapping("/sys/webparams")
public class WebParamsController {
    @Autowired
    private WebParamsService webParamsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:webparams:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webParamsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:webparams:info")
    public R info(@PathVariable("id") Integer id){
		WebParamsEntity webParams = webParamsService.getById(id);

        return R.ok().put("webParams", webParams);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:webparams:save")
    public R save(@RequestBody WebParamsEntity webParams){
		webParamsService.save(webParams);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:webparams:update")
    public R update(@RequestBody WebParamsEntity webParams){
		webParamsService.updateById(webParams);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:webparams:delete")
    public R delete(@RequestBody Integer[] ids){
		webParamsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
