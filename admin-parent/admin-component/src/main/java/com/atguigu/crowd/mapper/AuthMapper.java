package com.atguigu.crowd.mapper;

import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.AuthExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthMapper {
    int countByExample(AuthExample example);

    int deleteByExample(AuthExample example);

    int deleteByPrimaryKey(Integer id);

    void deleteOldRelationship(Integer roleId);

    int insert(Auth record);

    int insertSelective(Auth record);

    void insertNewRelationship(@Param("roleId") Integer roleId, @Param("authIdList") List<Integer> authIdList);

    List<Auth> selectByExample(AuthExample example);

    Auth selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByExample(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByPrimaryKeySelective(Auth record);

    int updateByPrimaryKey(Auth record);


    List<Integer> selectAssignedAuthIdByRoleId(Integer roleId);

    List<String> selectAssignedAuthNameByAdminId(Integer adminId);

    List<Auth> selectAssignedAuthByAdminId(Integer adminId);

    List<Auth> selectAuthByRoleIdArray(List<Integer> roleArray);
}