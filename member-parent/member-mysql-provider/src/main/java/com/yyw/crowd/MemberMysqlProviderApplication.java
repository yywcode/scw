package com.yyw.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//扫描Mybatis的Mapper接口所在的包
@MapperScan("com.yyw.crowd.mapper")
@SpringBootApplication
public class MemberMysqlProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberMysqlProviderApplication.class, args);
    }

}
