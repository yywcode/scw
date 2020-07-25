package com.yyw.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@EnableEurekaServer
@SpringBootApplication
public class MemberEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberEurekaApplication.class, args);
    }

}
