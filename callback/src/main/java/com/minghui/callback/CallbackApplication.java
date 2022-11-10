package com.minghui.callback;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@EnableDiscoveryClient
@Import(cn.hutool.extra.spring.SpringUtil.class)
@SpringBootApplication(scanBasePackages = "com.minghui")
@MapperScan({"com.minghui.commons.dao"})
public class CallbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(CallbackApplication.class, args);
    }

}
