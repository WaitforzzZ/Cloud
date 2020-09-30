package com.waitfor.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

// 扫描mybatis哪些包里面的接口
@MapperScan("com.waitfor.cloud.dao")
@SpringBootApplication
public class CloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudApplication.class, args);
    }

}
