package com.itmuch.contentcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 扫描mybatis哪些包里面的接口
 * @Author guanqing
 * @Date 2021/12/5 19:25
 **/
@MapperScan("com.itmuch.**.mapper")
@SpringBootApplication
@EnableFeignClients
public class ContentCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication.class, args);
    }

}
