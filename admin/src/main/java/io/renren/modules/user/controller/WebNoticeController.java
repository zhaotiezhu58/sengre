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

import io.renren.modules.user.entity.WebNoticeEntity;
import io.renren.modules.user.service.WebNoticeService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 16:24:51
 */
@RestController
@RequestMapping("user/webnotice")
public class WebNoticeController {
    @Autowired
    private WebNoticeService webNoticeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("user:webnotice:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webNoticeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("user:webnotice:info")
    public R info(@PathVariable("id") Integer id){
		WebNoticeEntity webNotice = webNoticeService.getById(id);

        return R.ok().put("webNotice", webNotice);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("user:webnotice:save")
    public R save(@RequestBody WebNoticeEntity webNotice){
		webNoticeService.save(webNotice);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("user:webnotice:update")
    public R update(@RequestBody WebNoticeEntity webNotice){
		webNoticeService.updateById(webNotice);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("user:webnotice:delete")
    public R delete(@RequestBody Integer[] ids){
		webNoticeService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
