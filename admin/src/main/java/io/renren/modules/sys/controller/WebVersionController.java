package io.renren.modules.sys.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.sys.entity.WebVersionEntity;
import io.renren.modules.sys.service.WebVersionService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 21:38:25
 */
@RestController
@RequestMapping("sys/webversion")
public class WebVersionController {
    @Autowired
    private WebVersionService webVersionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:webversion:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webVersionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:webversion:info")
    public R info(@PathVariable("id") Integer id){
		WebVersionEntity webVersion = webVersionService.getById(id);

        return R.ok().put("webVersion", webVersion);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:webversion:save")
    public R save(@RequestBody WebVersionEntity webVersion){
		webVersionService.save(webVersion);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:webversion:update")
    public R update(@RequestBody WebVersionEntity webVersion){
		webVersionService.updateById(webVersion);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:webversion:delete")
    public R delete(@RequestBody Integer[] ids){
		webVersionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
