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

import io.renren.modules.sys.entity.WebDictEntity;
import io.renren.modules.sys.service.WebDictService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-06-03 00:33:06
 */
@RestController
@RequestMapping("sys/webdict")
public class WebDictController {
    @Autowired
    private WebDictService webDictService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:webdict:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webDictService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:webdict:info")
    public R info(@PathVariable("id") Integer id){
		WebDictEntity webDict = webDictService.getById(id);

        return R.ok().put("webDict", webDict);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:webdict:save")
    public R save(@RequestBody WebDictEntity webDict){
		webDictService.save(webDict);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:webdict:update")
    public R update(@RequestBody WebDictEntity webDict){
		webDictService.updateById(webDict);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:webdict:delete")
    public R delete(@RequestBody Integer[] ids){
		webDictService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
