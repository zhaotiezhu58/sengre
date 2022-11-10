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

import io.renren.modules.activity.entity.WebWeeksalaryRecordEntity;
import io.renren.modules.activity.service.WebWeeksalaryRecordService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 21:42:14
 */
@RestController
@RequestMapping("activity/webweeksalaryrecord")
public class WebWeeksalaryRecordController {
    @Autowired
    private WebWeeksalaryRecordService webWeeksalaryRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("activity:webweeksalaryrecord:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webWeeksalaryRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("activity:webweeksalaryrecord:info")
    public R info(@PathVariable("id") Integer id){
		WebWeeksalaryRecordEntity webWeeksalaryRecord = webWeeksalaryRecordService.getById(id);

        return R.ok().put("webWeeksalaryRecord", webWeeksalaryRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("activity:webweeksalaryrecord:save")
    public R save(@RequestBody WebWeeksalaryRecordEntity webWeeksalaryRecord){
		webWeeksalaryRecordService.save(webWeeksalaryRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("activity:webweeksalaryrecord:update")
    public R update(@RequestBody WebWeeksalaryRecordEntity webWeeksalaryRecord){
		webWeeksalaryRecordService.updateById(webWeeksalaryRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("activity:webweeksalaryrecord:delete")
    public R delete(@RequestBody Integer[] ids){
		webWeeksalaryRecordService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
