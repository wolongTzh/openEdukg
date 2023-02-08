package com.edukg.open;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.edukg.open.**.mapper")
@ComponentScan(basePackages = {"com.edukg.open.controller","com.edukg.open.queue","com.edukg.open.shiro","com.edukg.open.config","com.edukg.open.util", "com.edukg.open.user.*"})
public class OpenEduKgApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenEduKgApplication.class, args);
    }

}
