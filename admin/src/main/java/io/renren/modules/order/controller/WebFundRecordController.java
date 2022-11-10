package io.renren.modules.order.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.order.pojo.FundExport;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.entity.WebDictEntity;
import io.renren.modules.sys.service.WebDictService;
import io.renren.modules.user.entity.WebUserEntity;
import io.renren.modules.user.service.WebUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.order.entity.WebFundRecordEntity;
import io.renren.modules.order.service.WebFundRecordService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 * 
 *
 * @author wangjie
 * @email 649206445@gmail.com
 * @date 2022-05-18 20:50:42
 */
@RestController
@RequestMapping("order/webfundrecord")
public class WebFundRecordController extends AbstractController {
    @Autowired
    private WebFundRecordService webFundRecordService;

    @Autowired
    private WebUserService webUserService;

    @Autowired
    private WebDictService webDictService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("order:webfundrecord:list")
    public R list(@RequestParam Map<String, Object> params){
        SysUserEntity sysUser = getUser();
        String tgName = sysUser.getTgName();
        WebUserEntity agentUser = webUserService.getOne(
                new QueryWrapper<WebUserEntity>().lambda()
                        .eq(WebUserEntity::getUserName, tgName)
        );
        if (agentUser == null) {
            return R.error("当前后台账号未绑定推广账号.");
        }
        if (agentUser.getAgentLevel() > 1) {
            params.put("userNode", "|" + agentUser.getUserName() + "|");
        }

        PageUtils page = webFundRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("order:webfundrecord:info")
    public R info(@PathVariable("id") Integer id){
		WebFundRecordEntity webFundRecord = webFundRecordService.getById(id);

        return R.ok().put("webFundRecord", webFundRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("order:webfundrecord:save")
    public R save(@RequestBody WebFundRecordEntity webFundRecord){
		webFundRecordService.save(webFundRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("order:webfundrecord:update")
    public R update(@RequestBody WebFundRecordEntity webFundRecord){
		webFundRecordService.updateById(webFundRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("order:webfundrecord:delete")
    public R delete(@RequestBody Integer[] ids){
		webFundRecordService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 导出
     */
    @RequestMapping("/export")
    @RequiresPermissions("order:webfundrecord:export")
    public R export(@RequestParam Map<String, Object> params, HttpServletResponse response) throws IOException {
        SysUserEntity sysUser = getUser();
        String tgName = sysUser.getTgName();
        WebUserEntity agentUser = webUserService.getOne(
                new QueryWrapper<WebUserEntity>().lambda()
                        .eq(WebUserEntity::getUserName, tgName)
        );
        if (agentUser == null) {
            return R.error("当前后台账号未绑定推广账号.");
        }
        if (agentUser.getAgentLevel() > 1) {
            params.put("userNode", "|" + agentUser.getUserName() + "|");
        }

        List<WebFundRecordEntity> list = webFundRecordService.queryList(params);
        if (CollUtil.isEmpty(list)) {
            return R.error("未查询到数据");
        }
        List<WebDictEntity> dict = webDictService.list(new QueryWrapper<WebDictEntity>().lambda().eq(WebDictEntity::getStatus, 1).eq(WebDictEntity::getType, 3));
        Map<String, String> dictMap = dict.stream().collect(Collectors.toMap(WebDictEntity::getDictKey, dictEntity -> dictEntity.getDictValue()));

        List<FundExport> funds = new ArrayList<>();
        for (WebFundRecordEntity fund : list) {
            FundExport temp = new FundExport();
            temp.setUserName(fund.getUserName());
            temp.setSerialNo(fund.getSerialNo());
            temp.setRefBillNo(fund.getRefBillNo());
            temp.setAmount(fund.getAmount());
            temp.setBeforeAmount(fund.getBeforeAmount());
            temp.setAfterAmount(fund.getAfterAmount());
            temp.setType(dictMap.getOrDefault(fund.getType().toString(), "未知"));
            temp.setCreateTime(fund.getCreateTime());
            temp.setAgent(fund.getAgent());
            funds.add(temp);
        }

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(DateUtil.format(new Date(), "yyyyMMddHHmmssSSS"), "UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
        EasyExcel.write(
                response.getOutputStream(), FundExport.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("sheet1")
                .doWrite(funds);

        return R.ok();
    }
}
