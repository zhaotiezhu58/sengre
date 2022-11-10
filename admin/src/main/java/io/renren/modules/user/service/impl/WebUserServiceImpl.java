package io.renren.modules.user.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.renren.modules.user.entity.WebUserLogEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.user.dao.WebUserDao;
import io.renren.modules.user.entity.WebUserEntity;
import io.renren.modules.user.service.WebUserService;


@Service("webUserService")
public class WebUserServiceImpl extends ServiceImpl<WebUserDao, WebUserEntity> implements WebUserService {
    @Autowired
    private WebUserDao webUserDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WebUserEntity> lambda = new QueryWrapper<WebUserEntity>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        lambda.eq(StringUtils.isNotEmpty(userName), WebUserEntity::getUserName, userName);

        String agent = MapUtil.getStr(params, "agent");
        lambda.eq(StringUtils.isNotEmpty(agent), WebUserEntity::getAgent, agent);

        Date beginTime = MapUtil.getDate(params, "beginTime");
        lambda.ge(beginTime != null, WebUserEntity::getRegTime, beginTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        lambda.le(endTime != null, WebUserEntity::getRegTime, endTime);

        String userNode = MapUtil.getStr(params, "userNode");
        lambda.like(StringUtils.isNotEmpty(userNode), WebUserEntity::getAgentNode, userNode);

        lambda.orderByDesc(WebUserEntity::getRegTime);

        IPage<WebUserEntity> page = this.page(
                new Query<WebUserEntity>().getPage(params),
                lambda
        );

        return new PageUtils(page);
    }

    @Override
    public List<WebUserEntity> queryList(Map<String, Object> params) {
        LambdaQueryWrapper<WebUserEntity> lambda = new QueryWrapper<WebUserEntity>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        lambda.eq(StringUtils.isNotEmpty(userName), WebUserEntity::getUserName, userName);

        String agent = MapUtil.getStr(params, "agent");
        lambda.eq(StringUtils.isNotEmpty(agent), WebUserEntity::getAgent, agent);

        Date beginTime = MapUtil.getDate(params, "beginTime");
        lambda.ge(beginTime != null, WebUserEntity::getRegTime, beginTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        lambda.le(endTime != null, WebUserEntity::getRegTime, endTime);

        String userNode = MapUtil.getStr(params, "userNode");
        lambda.like(StringUtils.isNotEmpty(userNode), WebUserEntity::getAgentNode, userNode);

        lambda.orderByDesc(WebUserEntity::getRegTime);

        return this.list(lambda);
    }

    @Override
    public void updateUserBalance(String userName, BigDecimal balance) throws Exception {
        int updateUserBalance = webUserDao.updateUserBalance(userName, balance);
        if (updateUserBalance <= 0) {
            throw new Exception("修改用户余额失败.");
        }
    }

    @Override
    public void batchUpdateUserVirtualBalances(List<WebUserEntity> list) throws Exception {
        int updateUserBalance = webUserDao.batchUpdateUserVirtualBalances(list);
        if (updateUserBalance <= 0) {
            throw new Exception("修改用户余额失败.");
        }
    }
}