package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;


/*
 * 考虑到User对象只包含账号密码，为了获取原始Admin对象，创造这个类对User类进行扩展
 * */
public class SecurityAdmin extends User {

    //原始Admin对象，包含所有Admin的所有属性
    private Admin originalAdmin;

    public SecurityAdmin(
            Admin originalAdmin,

            //创建角色、权限信息的集合
            List<GrantedAuthority> authorities
    ) {
        super(originalAdmin.getLoginAcct(), originalAdmin.getUserPswd(), authorities);

        this.originalAdmin = originalAdmin;

        //将原始Admin对象的密码擦除
        this.originalAdmin.setUserPswd(null);
    }

    //获取原始Admin对象的get方法
    public Admin getOriginalAdmin() {
        return originalAdmin;
    }
}
