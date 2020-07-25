package com.yyw.crowd.handler;


import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.util.ResultEntity;
import com.yyw.crowd.entity.po.MemberPO;
import com.yyw.crowd.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberProviderHandler {

    @Autowired
    private MemberService memberService;

    @RequestMapping(value = "/save/member/remote",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO){

        try{
            memberService.saveMember(memberPO);
            return ResultEntity.successWithoutData();

        }catch (Exception e){
            if(e instanceof DuplicateKeyException)
                return ResultEntity.FAILED(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USER);
            return ResultEntity.FAILED(e.getMessage());
        }
    }



    @RequestMapping(value = "/get/memberpo/by/login/acct/remote",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct") String loginacct){

        try {
            //1.调用本地Servcie完成查询
            MemberPO memberPO = memberService.getMemberPOByLoginAcct(loginacct);

            //2.无异常，返回成功结果
            return ResultEntity.successWithData(memberPO);
        } catch (Exception e) {
            e.printStackTrace();

            //3.有异常，返回失败结果
            return ResultEntity.FAILED(e.getMessage());
        }
    }
}
