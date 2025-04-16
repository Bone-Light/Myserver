package org.example;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BackServerApplicationTests {
//    @Resource
//    PasswordEncoder passwordEncoder;
//
    @Test
    void contextLoads() {
//        System.out.println("加密结果: " + passwordEncoder.encode("123123"));
    }
}
