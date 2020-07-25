package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Role;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminService {

    void saveAdmin(Admin admin);

    List<Admin> getAll();

    Admin getAdminByLoginAcct(String loginAcct, String userPswd);

    Admin getAdminByLoginAcct(String loginAcct);

    PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize);

    void remove(Integer adminId);


    Admin getAdminById(Integer adminId);

    int update(Admin admin);

    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList);

}
