//package io.renren.modules.job.task;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.util.NumberUtil;
//import cn.hutool.crypto.SecureUtil;
//import cn.hutool.http.HttpRequest;
//import cn.hutool.http.HttpResponse;
//import cn.hutool.http.HttpUtil;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import io.renren.common.commons.Constant;
//import io.renren.common.utils.IdUtils;
//import io.renren.modules.order.entity.WebVirtualRecordEntity;
//import io.renren.modules.order.service.WebVirtualRecordService;
//import io.renren.modules.sys.service.WebParamsService;
//import io.renren.modules.user.entity.WebUserEntity;
//import io.renren.modules.user.service.WebUserService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.TransactionStatus;
//import org.springframework.transaction.support.DefaultTransactionDefinition;
//
//import java.math.BigDecimal;
//import java.util.*;
//
///**
// * 每周奖励
// * @author Administrator
// */
//@Slf4j
//@Component("weekRewardTask")
//public class WeekRewardTask implements ITask{
//
//    @Autowired
//    private WebParamsService webParamsService;
//
//    @Autowired
//    private WebUserService webUserService;
//
//    @Autowired
//    private WebVirtualRecordService webVirtualRecordService;
//
//    @Autowired
//    private PlatformTransactionManager transactionManager;
//
//    @Override
//    public void run(String params) {
//        log.info("每周奖励开始...");
//        String weekreward = webParamsService.getParamsValue("weekreward");
//        BigDecimal minAmount = new BigDecimal(weekreward);
//
//        // 查询满足条件的用户
//        List<WebUserEntity> users = webUserService.list(new QueryWrapper<WebUserEntity>().lambda().ge(WebUserEntity::getBalance, minAmount));
//        if (CollUtil.isNotEmpty(users)) {
//            List<WebVirtualRecordEntity> virtuals = new ArrayList<>();
//            List<WebUserEntity> updateUsers = new ArrayList<>();
//            Date date = new Date();
//            for (WebUserEntity user : users) {
//                BigDecimal amount = NumberUtil.mul(user.getBalance(), 2);
//                // 添加代币
//                //webUserService.updateUserBalance(;);
//
//                WebUserEntity temp = new WebUserEntity();
//                temp.setUserName(user.getUserName());
//                temp.setVirtualBalance(amount);
//                updateUsers.add(temp);
//
//                // 添加代币记录
//                WebVirtualRecordEntity virtual = new WebVirtualRecordEntity();
//                virtual.setUserName(user.getUserName());
//                virtual.setSerialNo(IdUtils.randomId());
//                virtual.setRefBillNo(null);
//                virtual.setAmount(amount);
//                virtual.setBeforeAmount(user.getVirtualBalance());
//                virtual.setAfterAmount(NumberUtil.add(user.getVirtualBalance(), amount));
//                virtual.setType(Constant.VirtualType.WEEKRAWARD.getValue());
//                virtual.setCreateTime(date);
//                virtual.setAgent(user.getAgent());
//                virtual.setAgentNode(user.getAgentNode());
//                virtual.setAgentLevel(user.getAgentLevel());
//                virtuals.add(virtual);
//            }
//            TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
//            try {
//                webUserService.batchUpdateUserVirtualBalances(updateUsers);
//                webVirtualRecordService.saveBatch(virtuals);
//                transactionManager.commit(transactionStatus);
//            } catch (Exception e) {
//                e.printStackTrace();
//                transactionManager.rollback(transactionStatus);
//            }
//        }
//        log.info("每周奖励结束...");
//    }
//}
