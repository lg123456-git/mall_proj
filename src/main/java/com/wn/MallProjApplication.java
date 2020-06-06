package com.wn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@MapperScan("com.wn.mapper")
public class MallProjApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallProjApplication.class, args);
    }

}
