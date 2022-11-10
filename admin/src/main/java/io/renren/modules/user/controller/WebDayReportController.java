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

import io.renren.modules.user.entity.WebDayReportEntity;
import io.renren.modules.user.service.WebDayReportService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 22:29:21
 */
@RestController
@RequestMapping("user/webdayreport")
public class WebDayReportController {
    @Autowired
    private WebDayReportService webDayReportService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("user:webdayreport:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webDayReportService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("user:webdayreport:info")
    public R info(@PathVariable("id") Integer id){
		WebDayReportEntity webDayReport = webDayReportService.getById(id);

        return R.ok().put("webDayReport", webDayReport);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("user:webdayreport:save")
    public R save(@RequestBody WebDayReportEntity webDayReport){
		webDayReportService.save(webDayReport);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("user:webdayreport:update")
    public R update(@RequestBody WebDayReportEntity webDayReport){
		webDayReportService.updateById(webDayReport);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("user:webdayreport:delete")
    public R delete(@RequestBody Integer[] ids){
		webDayReportService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
