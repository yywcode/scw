package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.AuthExample;
import com.atguigu.crowd.mapper.AuthMapper;
import com.atguigu.crowd.service.api.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    @Override
    public List<Auth> getAll() {
        return authMapper.selectByExample(new AuthExample());
    }

    @Override
    public List<Integer> getAssignedAuthIdByRoleId(Integer roleId) {
        return authMapper.selectAssignedAuthIdByRoleId(roleId);
    }

    @Override
    public void saveRoleAuthRelationship(Map<String, List<Integer>> map) {

        //"roleId":List<Integer> roleIdList
        //"authIdArray":List<Integer> authIdArray
        //1.获取roleId的值
        List<Integer> roleIdList = map.get("roleId");
        Integer roleId = roleIdList.get(0);

        //2.删除旧的关联关系数据
        authMapper.deleteOldRelationship(roleId);

        //3.获取authIdList
        List<Integer> authIdList = map.get("authIdArray");
        Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
        logger.info(authIdList.toString());

        //4.判断authIdList是否有效
        if (authIdList != null && authIdList.size() > 0) {
            authMapper.insertNewRelationship(roleId, authIdList);
        }
    }

    @Override
    public List<String> getAssignedAuthNameByAdminId(Integer adminId) {
        return authMapper.selectAssignedAuthNameByAdminId(adminId);
    }

    @Override
    public List<Auth> getAssignedAuthByAdminId(Integer adminId) {
        return authMapper.selectAssignedAuthByAdminId(adminId);
    }

    @Override
    public List<Auth> getAuthByRoleIdArray(List<Integer> roleArray) {
        return authMapper.selectAuthByRoleIdArray(roleArray);
    }

}
