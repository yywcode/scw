package com.yyw.crowd.security.config;

import com.yyw.crowd.crowd.security.entity.Admin;
import com.yyw.crowd.security.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class MyUsersDetailsService implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //总目标，根据表单提交的账号，用户名，去查询我们的user对象，并装配角色权限等信息
    @Override
    public UserDetails loadUserByUsername(

            //表单提交的用户名
            String username
    ) throws UsernameNotFoundException {

        String sql = "SELECT id, loginacct ,userpswd, username, email FROM t_admin WHERE loginacct = ?";

        List<Admin> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Admin.class), username);

        Admin admin = list.get(0);

        //给Admin设置角色权限信息
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("UPDATE"));

        //把admin对象和authorities封装到UserDetails中

        String userpswd = admin.getUserpswd();
        System.out.println("admin-->" + admin);
        return new User(username, userpswd, authorities);
    }
}
