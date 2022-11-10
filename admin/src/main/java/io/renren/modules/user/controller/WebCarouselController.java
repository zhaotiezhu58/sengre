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

import io.renren.modules.user.entity.WebCarouselEntity;
import io.renren.modules.user.service.WebCarouselService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 17:44:20
 */
@RestController
@RequestMapping("user/webcarousel")
public class WebCarouselController {
    @Autowired
    private WebCarouselService webCarouselService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("user:webcarousel:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webCarouselService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("user:webcarousel:info")
    public R info(@PathVariable("id") Integer id){
		WebCarouselEntity webCarousel = webCarouselService.getById(id);

        return R.ok().put("webCarousel", webCarousel);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("user:webcarousel:save")
    public R save(@RequestBody WebCarouselEntity webCarousel){
		webCarouselService.save(webCarousel);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("user:webcarousel:update")
    public R update(@RequestBody WebCarouselEntity webCarousel){
		webCarouselService.updateById(webCarousel);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("user:webcarousel:delete")
    public R delete(@RequestBody Integer[] ids){
		webCarouselService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
