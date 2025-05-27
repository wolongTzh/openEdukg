package com.edukg.open;

import com.edukg.open.user.service.IUserService;
import com.edukg.open.util.Md5Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OpenEduKgApplicationTests {

    @Autowired
    IUserService userService;

    @Test
    void contextLoads() {
        String phone = "15210985093";
        String password = "666666";
        String md5Code = Md5Util.getMD5String(phone + password);
        System.out.println(md5Code);
    }
    @Test
    void greenPath() {
        int num = 50;
        for (int i = 1; i < num+1; i++) {
            String phone = "user" + i;
            String email = phone + "@test.com";
            String password = "666666";
            userService.register(phone, email, password, "person", "student" + i, null);
        }
    }

}
