package com.yyw.crowd.handler;


import com.atguigu.crowd.util.ResultEntity;
import com.yyw.crowd.entity.vo.AddressVO;
import com.yyw.crowd.entity.vo.OrderProjectVO;
import com.yyw.crowd.entity.vo.OrderVO;
import com.yyw.crowd.service.api.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderProviderHandler {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/save/order/remote")
    ResultEntity<String> saveOrderRemote(@RequestBody OrderVO orderVO) {
        try {
            orderService.saveOrder(orderVO);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.FAILED(e.getMessage());
        }
    }


    @RequestMapping("/save/address/remote")
    ResultEntity<String> saveAddressRemote(@RequestBody AddressVO addressVO){

        try {
            orderService.saveAdress(addressVO);

            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.FAILED(e.getMessage());
        }

    }

    @RequestMapping(value = "get/addressvo/remote",produces = MediaType.APPLICATION_JSON_VALUE)
    ResultEntity<List<AddressVO>> getAddressVORemote(@RequestParam("memberId") Integer memberId){

        try {
            List<AddressVO> addressVOList = orderService.getAddressVOList(memberId);

            return ResultEntity.successWithData(addressVOList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.FAILED(e.getMessage());
        }

    }

    @RequestMapping(value = "/get/order/projectvo/remote",produces = MediaType.APPLICATION_JSON_VALUE)
    ResultEntity<OrderProjectVO> getOrderProjectVORemote(
            @RequestParam("projectId") Integer projectId,
            @RequestParam("returnId") Integer returnId){


        try {
            OrderProjectVO orderProjectVO = orderService.getOrderProjectVO(projectId, returnId);

            return ResultEntity.successWithData(orderProjectVO);
        } catch (Exception e) {
            e.printStackTrace();

            return ResultEntity.FAILED(e.getMessage());
        }

    }
}
