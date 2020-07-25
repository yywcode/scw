package com.yyw.crowd.handler;


import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.util.ResultEntity;
import com.netflix.discovery.converters.Auto;
import com.yyw.crowd.api.MySQLRemoteService;
import com.yyw.crowd.entity.vo.AddressVO;
import com.yyw.crowd.entity.vo.MemberLoginVO;
import com.yyw.crowd.entity.vo.OrderProjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OrderHandler {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;
    private Logger logger = LoggerFactory.getLogger(OrderHandler.class);

    @RequestMapping("/save/address")
    public String saveAddress(
            AddressVO addressVO,
            HttpSession session
            ) {
        //1.執行地址信息的保存
        ResultEntity<String> resultEntity = mySQLRemoteService.saveAddressRemote(addressVO);
        logger.debug("地址保存处理结果"+resultEntity.getResult());
        //2.Session域中获取对象
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");

        //3.从orderProject对象中获取returnCount
        Integer returnCount = orderProjectVO.getReturnCount();

        //4.重定向到指定地址，重新进入确认订单的页面
        return "redirect:http://localhost:521/order/confirm/order/" + returnCount;
    }


    @RequestMapping("/confirm/return/info/{projectId}/{returnId}")
    public String showReturnConfirmInfo(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("returnId") Integer returnId,
            HttpSession session
    ){


        ResultEntity<OrderProjectVO> resultEntity =
                mySQLRemoteService.getOrderProjectVORemote(projectId, returnId);
        if(ResultEntity.SUCCESS.equals(resultEntity.getResult())){

            OrderProjectVO orderProjectVO = resultEntity.getData();

            //为了能在后续操作中保持orderProjectVO，保存在session域中
            session.setAttribute("orderProjectVO", orderProjectVO);
        }

        return "confirm_return";
    }

    @RequestMapping("/confirm/order/{returnCount}")
    public String showConfirmOrderInfo(
            @PathVariable("returnCount") Integer returnCount,
            HttpSession session
            ){

        //1.把接收到的回报数量合并到Session域
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");

        orderProjectVO.setReturnCount(returnCount);

        //更新session中orderProjectVO对象
        session.setAttribute("orderProjectVO", orderProjectVO);

        //2.取出当前已登录用户的id
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        Integer memberId = memberLoginVO.getId();

        //3.查询目前有的收获地址数据
        ResultEntity<List<AddressVO>> resultEntity = mySQLRemoteService.getAddressVORemote(memberId);

        if(ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            List<AddressVO> list = resultEntity.getData();
            session.setAttribute("addressVOList", list);
        }

        return "confirm_order";
    }
}
