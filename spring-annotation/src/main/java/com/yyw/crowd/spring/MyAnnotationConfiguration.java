package com.yyw.crowd.spring;

import com.yyw.crowd.crowd.entity.Employee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//表示当前这个类是一个配置类，作用大致相当于以前的spring-context.xml这样的配置文件


@Configuration
public class MyAnnotationConfiguration {

    //@Bean相当于以下配置，把方法返回值放入ioc容器
    //<bean id="Employee" class="com.yyw.spring.entity.Employee">

    @Bean
    public Employee getEmployee() {
        return new Employee();
    }


}
