package com.yyw.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class MemberAuthenticationConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberAuthenticationConsumerApplication.class, args);
    }

}
