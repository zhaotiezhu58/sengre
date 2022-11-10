package io.renren.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class TimeHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        // TODO Auto-generated method stub
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("modifyTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("modifyTime", new Date(), metaObject);
    }
}
