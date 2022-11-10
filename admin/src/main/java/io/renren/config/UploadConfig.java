//package io.renren.config;
//
//import org.springframework.boot.web.servlet.MultipartConfigFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.unit.DataSize;
//
//import javax.servlet.MultipartConfigElement;
//
//@Configuration
//public class UploadConfig {
//    @Bean
//    public MultipartConfigElement multipartConfigElement() {
//        System.out.println("文件上传配置............");
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        //单个文件最大
//        factory.setMaxFileSize(DataSize.ofMegabytes(100)); //KB,MB
//        /// 设置总上传数据总大小
//        factory.setMaxRequestSize(DataSize.ofMegabytes(100));
//        return factory.createMultipartConfig();
//    }
//}
