package com.hitd.im.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author ZhangWeinan
 * @Date 2023/3/8 1:25
 * @DES
 * @Since Copyright(c)
 */
@SpringBootApplication
@MapperScan("com.hitd.im.service.*.dao.mapper")
public class ImServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImServiceApplication.class,args);
    }
}
