package com.yyw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MemberPayConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberPayConsumerApplication.class, args);
    }

}
