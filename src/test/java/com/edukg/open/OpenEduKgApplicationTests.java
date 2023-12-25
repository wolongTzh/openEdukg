package com.edukg.open;

import com.edukg.open.util.Md5Util;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OpenEduKgApplicationTests {

    @Test
    void contextLoads() {
        String phone = "kgadmin";
        String password = "kgadmin";
        String md5Code = Md5Util.getMD5String(phone + password);
        System.out.println(md5Code);
    }

}
