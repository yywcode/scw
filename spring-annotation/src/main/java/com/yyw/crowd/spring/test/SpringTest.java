package com.yyw.crowd.spring.test;

import com.yyw.crowd.crowd.entity.Employee;
import com.yyw.crowd.crowd.spring.MyAnnotationConfiguration;
import com.yyw.crowd.entity.Employee;
import com.yyw.crowd.spring.MyAnnotationConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


@SuppressWarnings("resources")
public class SpringTest {

    public static void main(String[] args) {
        //以前使用new ClassPathXmlApplicationContext("")方式加载xml配置文件

        //现在基于注解配置类创建IOC容器对象
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(MyAnnotationConfiguration.class);

        //从IOC容器获取bean
        Employee employee = applicationContext.getBean(Employee.class);
        System.out.println(employee);
        applicationContext.close();
    }


}
