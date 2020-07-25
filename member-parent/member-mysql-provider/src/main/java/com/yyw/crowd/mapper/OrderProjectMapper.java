package com.yyw.crowd.mapper;

import com.yyw.crowd.entity.po.OrderProject;
import com.yyw.crowd.entity.po.OrderProjectExample;
import com.yyw.crowd.entity.vo.OrderProjectVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderProjectMapper {
    int countByExample(OrderProjectExample example);

    int deleteByExample(OrderProjectExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OrderProject record);

    int insertSelective(OrderProject record);

    List<OrderProject> selectByExample(OrderProjectExample example);

    OrderProject selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OrderProject record, @Param("example") OrderProjectExample example);

    int updateByExample(@Param("record") OrderProject record, @Param("example") OrderProjectExample example);

    int updateByPrimaryKeySelective(OrderProject record);

    int updateByPrimaryKey(OrderProject record);

    OrderProjectVO selectOrderProjectVO(Integer returnId);
}