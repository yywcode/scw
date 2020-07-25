package com.yyw.crowd.service.impl;

import com.yyw.crowd.entity.po.Address;
import com.yyw.crowd.entity.po.AddressExample;
import com.yyw.crowd.entity.po.OrderPO;
import com.yyw.crowd.entity.po.OrderProject;
import com.yyw.crowd.entity.vo.AddressVO;
import com.yyw.crowd.entity.vo.OrderProjectVO;
import com.yyw.crowd.entity.vo.OrderVO;
import com.yyw.crowd.mapper.AddressMapper;
import com.yyw.crowd.mapper.OrderPOMapper;
import com.yyw.crowd.mapper.OrderProjectMapper;
import com.yyw.crowd.service.api.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderProjectMapper orderProjectMapper;
    @Autowired
    private OrderPOMapper orderPOMapper;
    @Autowired
    private AddressMapper addressMapper;


    @Override
    public OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId) {


        return orderProjectMapper.selectOrderProjectVO(returnId);
    }

    @Override
    public List<AddressVO> getAddressVOList(Integer memberId) {

        AddressExample example = new AddressExample();

        example.createCriteria().andMemberIdEqualTo(memberId);

        List<Address> addressList = addressMapper.selectByExample(example);

        ArrayList<AddressVO> addressVOList = new ArrayList<>();

        for(Address address : addressList){
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(address, addressVO);
            addressVOList.add(addressVO);
        }
        return addressVOList;
    }

    @Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveAdress(AddressVO addressVO) {
        Address address = new Address();

        BeanUtils.copyProperties(addressVO, address);

        addressMapper.insert(address);

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveOrder(OrderVO orderVO) {
        OrderPO orderPO = new OrderPO();
        BeanUtils.copyProperties(orderVO, orderPO);
        OrderProject orderProject = new OrderProject();
        BeanUtils.copyProperties(orderVO.getOrderProjectVO(), orderProject);
        orderPOMapper.insert(orderPO);
        Integer id = orderPO.getId();
        orderProject.setOrderId(id);
        orderProjectMapper.insert(orderProject);
    }
}
