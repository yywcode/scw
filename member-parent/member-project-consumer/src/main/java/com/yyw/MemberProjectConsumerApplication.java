package com.yyw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//开启这个才能调用FeignClients中的接口
@EnableFeignClients
@SpringBootApplication
public class MemberProjectConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberProjectConsumerApplication.class, args);
    }



}
