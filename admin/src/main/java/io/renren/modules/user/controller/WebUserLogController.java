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

import io.renren.modules.user.entity.WebUserLogEntity;
import io.renren.modules.user.service.WebUserLogService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 16:13:51
 */
@RestController
@RequestMapping("user/webuserlog")
public class WebUserLogController {
    @Autowired
    private WebUserLogService webUserLogService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("user:webuserlog:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webUserLogService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("user:webuserlog:info")
    public R info(@PathVariable("id") Integer id){
		WebUserLogEntity webUserLog = webUserLogService.getById(id);

        return R.ok().put("webUserLog", webUserLog);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("user:webuserlog:save")
    public R save(@RequestBody WebUserLogEntity webUserLog){
		webUserLogService.save(webUserLog);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("user:webuserlog:update")
    public R update(@RequestBody WebUserLogEntity webUserLog){
		webUserLogService.updateById(webUserLog);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("user:webuserlog:delete")
    public R delete(@RequestBody Integer[] ids){
		webUserLogService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
