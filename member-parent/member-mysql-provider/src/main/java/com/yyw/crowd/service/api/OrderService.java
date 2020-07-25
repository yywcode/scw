package com.yyw.crowd.service.api;

import com.yyw.crowd.entity.vo.AddressVO;
import com.yyw.crowd.entity.vo.OrderProjectVO;
import com.yyw.crowd.entity.vo.OrderVO;

import java.util.List;

public interface OrderService {

    OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId);


    List<AddressVO> getAddressVOList(Integer memberId);

    void saveAdress(AddressVO addressVO);

    void saveOrder(OrderVO orderVO);
}
