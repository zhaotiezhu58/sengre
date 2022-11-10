package io.renren.modules.pay.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.pay.entity.WebPayMerchantEntity;
import io.renren.modules.pay.service.WebPayMerchantService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 17:55:19
 */
@RestController
@RequestMapping("pay/webpaymerchant")
public class WebPayMerchantController {
    @Autowired
    private WebPayMerchantService webPayMerchantService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("pay:webpaymerchant:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webPayMerchantService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("pay:webpaymerchant:info")
    public R info(@PathVariable("id") Integer id){
		WebPayMerchantEntity webPayMerchant = webPayMerchantService.getById(id);

        return R.ok().put("webPayMerchant", webPayMerchant);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("pay:webpaymerchant:save")
    public R save(@RequestBody WebPayMerchantEntity webPayMerchant){
		webPayMerchantService.save(webPayMerchant);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("pay:webpaymerchant:update")
    public R update(@RequestBody WebPayMerchantEntity webPayMerchant){
		webPayMerchantService.updateById(webPayMerchant);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("pay:webpaymerchant:delete")
    public R delete(@RequestBody Integer[] ids){
		webPayMerchantService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
