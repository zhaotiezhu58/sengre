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

import io.renren.modules.user.entity.WebProductEntity;
import io.renren.modules.user.service.WebProductService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 16:54:59
 */
@RestController
@RequestMapping("user/webproduct")
public class WebProductController {
    @Autowired
    private WebProductService webProductService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("user:webproduct:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webProductService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("user:webproduct:info")
    public R info(@PathVariable("id") Integer id){
		WebProductEntity webProduct = webProductService.getById(id);

        return R.ok().put("webProduct", webProduct);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("user:webproduct:save")
    public R save(@RequestBody WebProductEntity webProduct){
		webProductService.save(webProduct);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("user:webproduct:update")
    public R update(@RequestBody WebProductEntity webProduct){
		webProductService.updateById(webProduct);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("user:webproduct:delete")
    public R delete(@RequestBody Integer[] ids){
		webProductService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
