package com.yyw.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MemberOrderConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberOrderConsumerApplication.class, args);
    }

}
