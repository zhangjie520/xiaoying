package com.example.xiaoying;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.example.xiaoying.mapper")
public class XiaoyingApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaoyingApplication.class, args);
    }

}
