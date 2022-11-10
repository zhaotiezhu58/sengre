package io.renren.modules.pay.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.pay.entity.WebPayChannelEntity;
import io.renren.modules.pay.service.WebPayChannelService;
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
@RequestMapping("pay/webpaychannel")
public class WebPayChannelController {
    @Autowired
    private WebPayChannelService webPayChannelService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("pay:webpaychannel:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webPayChannelService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 获取代付通道
     */
    @RequestMapping("/withdrawList")
    @RequiresPermissions("pay:webpaychannel:list")
    public R withdrawList(@RequestParam Map<String, Object> params){
        String payType = MapUtil.getStr(params, "payType");
        List<WebPayChannelEntity> list = webPayChannelService.list(
                new QueryWrapper<WebPayChannelEntity>().lambda()
                        .eq(WebPayChannelEntity::getChannelType, 2)
                        .eq(StringUtils.isNotEmpty(payType), WebPayChannelEntity::getPayType, payType)
        );
        return R.ok().put("list", list);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("pay:webpaychannel:info")
    public R info(@PathVariable("id") Integer id){
		WebPayChannelEntity webPayChannel = webPayChannelService.getById(id);

        return R.ok().put("webPayChannel", webPayChannel);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("pay:webpaychannel:save")
    public R save(@RequestBody WebPayChannelEntity webPayChannel){
		webPayChannelService.save(webPayChannel);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("pay:webpaychannel:update")
    public R update(@RequestBody WebPayChannelEntity webPayChannel){
		webPayChannelService.updateById(webPayChannel);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("pay:webpaychannel:delete")
    public R delete(@RequestBody Integer[] ids){
		webPayChannelService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
