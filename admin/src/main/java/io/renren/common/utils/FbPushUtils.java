package io.renren.common.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FbPushUtils {

    /**
     * 规定每个帐号半小时之内只能操作一次,如果继续操作,要把时间续期
     * @return
     */
    public static Date getUserPushTime(RedisTemplate redisTemplate, String userName, Date pushTime) {
        Object o = redisTemplate.opsForValue().get("fb:push:user:" + userName);
        log.info("user:{},date:{}", userName, o);
        if (o != null) {
            Date time = DateUtil.parse((String) o, DatePattern.NORM_DATETIME_FORMAT);
            pushTime = DateUtil.offsetMinute(time, 30);
        }
        redisTemplate.opsForValue().set("fb:push:user:" + userName, DateUtil.format(pushTime, DatePattern.NORM_DATETIME_FORMAT), 30L, TimeUnit.MINUTES);
        return pushTime;
    }
}
