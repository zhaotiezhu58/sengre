package com.minghui.commons.service.impl;

import com.minghui.commons.dao.WebUserDao;
import com.minghui.commons.entity.WebUserEntity;
import com.minghui.commons.service.WebUserService;
import com.minghui.commons.utils.PageUtils;
import com.minghui.commons.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("webUserService")
public class WebUserServiceImpl extends ServiceImpl<WebUserDao, WebUserEntity> implements WebUserService {

    @Autowired
    private WebUserDao webUserDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebUserEntity> page = this.page(
                new Query<WebUserEntity>().getPage(params),
                new QueryWrapper<WebUserEntity>()
        );

        return new PageUtils(page);
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

    @Override
    public void updateUserVirtualBalance(String userName, BigDecimal balance) throws Exception {
        int updateUserBalance = webUserDao.updateUserVirtualBalance(userName, balance);
        if (updateUserBalance <= 0) {
            throw new Exception("修改用户代币余额失败.");
        }
    }

    @Override
    public void unlockVirtualBalance(String userName, BigDecimal balance) throws Exception {
        int updateUserBalance = webUserDao.unlockVirtualBalance(userName, balance);
        if (updateUserBalance <= 0) {
            throw new Exception("解锁虚拟余额失败.");
        }
    }

    @Override
    public void collectVirtualBalance(String userName, BigDecimal balance) throws Exception {
        int updateUserBalance = webUserDao.collectVirtualBalance(userName, balance);
        if (updateUserBalance <= 0) {
            throw new Exception("收集代币余额失败.");
        }
    }

    @Override
    public WebUserEntity getUser(String userName) {
        return webUserDao.selectOne(
                new QueryWrapper<WebUserEntity>().lambda()
                        .eq(WebUserEntity::getUserName, userName)
        );
    }
}