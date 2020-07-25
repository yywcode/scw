package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.constant.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//表示当前类是一个配置类
@Configuration

//启用Web环境下权限控制功能
@EnableWebSecurity

//启用全局方法权限控制,并且设置prePostEnabled = true，保证@PreAuthority,@PostAuthority，@PreFilter，@PosFilter生效
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;


    //登录验证和保存都需要用到该带盐值的加密算法
    //在这里声明Service中无法装配
/*    @Bean
    public BCryptPasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }*/
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        //临时使用内存版登录的模式测试代码
//        builder.inMemoryAuthentication().withUser("yasuo").password("456456").roles("ADMIN");

        //正式功能中使用基于数据库的认证
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {

        security
                //对请求进行授权
                .authorizeRequests()
                //针对登录设置
                .antMatchers("/admin/to/login/page.html").permitAll()
                //针对静态资源设置
                .antMatchers("/bootstrap/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/img/**").permitAll()
                .antMatchers("/jquery/**").permitAll()
                .antMatchers("/layer/**").permitAll()
                .antMatchers("/script/**").permitAll()
                .antMatchers("/ztree/**").permitAll()
                .antMatchers("/index.jsp").permitAll()
//                .antMatchers("/send/array/one.html").permitAll()
//                .antMatchers("/send/array/two.html").permitAll()
//                .antMatchers("/send/array/three.html").permitAll()
//                .antMatchers("/send/compose/object.json").permitAll()
                .antMatchers("/admin/get/page.html")//针对分页显示Admin数据设定访问控制
//                .hasRole("经理")
                .access("hasRole('经理') or hasAnyAuthority('user:get')")
                .anyRequest().authenticated()//任意请求登录后访问
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        httpServletRequest.setAttribute("exception", new Exception(CrowdConstant.MESSAGE_ACCESS_DENIED));
                        httpServletRequest.getRequestDispatcher("/WEB-INF/system-error.jsp").forward(httpServletRequest, httpServletResponse);
                    }
                })
                .and()
                .csrf()//防跨站请求
                .disable()//禁用
                .formLogin()//开启表单登录功能
                .loginPage("/admin/to/login/page.html")//指定登录页面
                .loginProcessingUrl("/security/do/login.html")//指定登录请求地址
                .defaultSuccessUrl("/admin/to/main/page.html")//指定登录成功后前往的地址
                .usernameParameter("loginAcct")//账号参数名称
                .passwordParameter("userPswd") //密码参数名称
                .and()
                .logout()            //开启退出登录功能
                .logoutUrl("/security/do/logout.html")       //指定退出登录请求地址
                .logoutSuccessUrl("/admin/to/login/page.html")//指定退出成功后前往的地址
        ;
    }
}
