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

import io.renren.modules.user.entity.WebQuestionEntity;
import io.renren.modules.user.service.WebQuestionService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 16:34:07
 */
@RestController
@RequestMapping("user/webquestion")
public class WebQuestionController {
    @Autowired
    private WebQuestionService webQuestionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("user:webquestion:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webQuestionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("user:webquestion:info")
    public R info(@PathVariable("id") Integer id){
		WebQuestionEntity webQuestion = webQuestionService.getById(id);

        return R.ok().put("webQuestion", webQuestion);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("user:webquestion:save")
    public R save(@RequestBody WebQuestionEntity webQuestion){
		webQuestionService.save(webQuestion);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("user:webquestion:update")
    public R update(@RequestBody WebQuestionEntity webQuestion){
		webQuestionService.updateById(webQuestion);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("user:webquestion:delete")
    public R delete(@RequestBody Integer[] ids){
		webQuestionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
