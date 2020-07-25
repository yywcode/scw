package com.yyw.crowd.api;


import com.atguigu.crowd.util.ResultEntity;
import com.yyw.crowd.entity.po.MemberPO;
import com.yyw.crowd.entity.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("yyw-crowd-mysql")
public interface MySQLRemoteService {

    @RequestMapping(value = "/save/member/remote",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO);

    @RequestMapping(value = "/get/memberpo/by/login/acct/remote",produces = MediaType.APPLICATION_JSON_VALUE)
    ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct") String loginacct);

    @RequestMapping(value ="/save/project/vo/remot",produces = MediaType.APPLICATION_JSON_VALUE )
    ResultEntity<String> saveProjectVORemote(
            @RequestBody ProjectVO projectVO,
            @RequestParam("memberId") Integer memberId);


    @RequestMapping(value = "/get/portal/type/project/data/remote",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultEntity<List<PortalTypeVO>> getPortalTypeProjectData();

    @RequestMapping(value = "/get/project/detail/remote/{projectId}",produces = MediaType.APPLICATION_JSON_VALUE )
    public ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId);


    @RequestMapping(value = "/get/order/projectvo/remote",produces = MediaType.APPLICATION_JSON_VALUE)
    ResultEntity<OrderProjectVO> getOrderProjectVORemote(
            @RequestParam("projectId") Integer projectId,
            @RequestParam("returnId") Integer returnId);


    @RequestMapping(value = "get/addressvo/remote",produces = MediaType.APPLICATION_JSON_VALUE)
    ResultEntity<List<AddressVO>> getAddressVORemote(@RequestParam("memberId") Integer memberId);


    //远程方法传入实体类必须使用@RequestBody
    @RequestMapping(value = "/save/address/remote", produces = MediaType.APPLICATION_JSON_VALUE)
    ResultEntity<String> saveAddressRemote(@RequestBody AddressVO addressVO);


    @RequestMapping("/save/order/remote")
    ResultEntity<String> saveOrderRemote(@RequestBody OrderVO orderVO);


}
