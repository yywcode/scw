package com.yyw.crowd.handler;


import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.yyw.crowd.api.MySQLRemoteService;
import com.yyw.crowd.api.RedisRemoteService;
import com.yyw.crowd.config.ShortMessageProperties;
import com.yyw.crowd.entity.po.MemberPO;
import com.yyw.crowd.entity.vo.MemberLoginVO;
import com.yyw.crowd.entity.vo.MemberVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;



@Controller
public class MemberHandler {

    private Logger logger = LoggerFactory.getLogger(MemberHandler.class);

    @Autowired
    private ShortMessageProperties shortMessageProperties;

    @Resource
    private RedisRemoteService redisRemoteService;

    @Resource
    private MySQLRemoteService mySQLRemoteService;


    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:http://localhost:521/";
    }


    @RequestMapping("/auth/member/do/login")
    public String login(
            @RequestParam("loginacct") String loginacct,
            @RequestParam("userpswd") String userpswd,
            ModelMap modelMap,
            HttpSession session

    ){

        ResultEntity<MemberPO> resultEntity = mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);

        if(ResultEntity.FAILED.equals(resultEntity.getResult())){

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-login";
        }
        MemberPO memberPO = resultEntity.getData();

        if(memberPO == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }

        String userpswdDatabase = memberPO.getUserpswd();
        String userpswdForm = userpswd;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        //盐值随机生成，所以必须用matches
        boolean matchResult = passwordEncoder.matches(userpswd, userpswdDatabase);
        if(!matchResult){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }

        //创建MemberLoginVO存入Session域
        MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail());

        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);
        return "redirect:http://localhost:521/auth/member/to/center/page";
    }

    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap){

        //1.获取用户输入手机号
        String phoneNum = memberVO.getPhoneNum();

        //2.拼Redis中验证码的key
        String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

        //3.读取key对应的Value
        ResultEntity<String> resultEntity = redisRemoteService.getRedisStringValueByKey(key);

        //4.检查操作是否有效
        String result = resultEntity.getResult();
        if(ResultEntity.FAILED.equals(result)){

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-reg";
        }

        String redisCode = resultEntity.getData();
        if(redisCode == null){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
            return "member-reg";
        }
        //5.如果从Redis能够查询到value，则比较表单验证码和Redis验证码
        String formCode = memberVO.getCode();
        logger.info("formCode====>"+formCode);
        logger.info("RedisCode====>"+redisCode);
        if(!Objects.equals(formCode, redisCode)){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_INVALID);
            return "member-reg";
        }
        //6.如果验证码一致，则从Redis中删除
        ResultEntity<String> removeResultEntity = redisRemoteService.removeRedisKeyRemote(key);


        //7.执行密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String userpswd = memberVO.getUserpswd();
        String encode = passwordEncoder.encode(userpswd);
        memberVO.setUserpswd(encode);

        //8.执行保存
        //①创建空的MemberPO对象
        MemberPO memberPO = new MemberPO();

        //②复制属性
        BeanUtils.copyProperties(memberVO,memberPO);

        //③调用远程方法保存注册对象
        ResultEntity<String> saveMemberResultEntity = mySQLRemoteService.saveMember(memberPO);
        if(ResultEntity.FAILED.equals(saveMemberResultEntity.getResult())){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveMemberResultEntity.getMessage());
            return "member-reg";
        }

        //避免刷新浏览器重新进行注册流程
        return "redirect:http://localhost:521/auth/member/to/login/page";
    }



    @ResponseBody
    @RequestMapping(value = "auth/member/send/short/message.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultEntity<String> sendMessage(@RequestParam("phoneNum") String phoneNum) {
        logger.info("开始发送短信");
        //1.发送验证码到phoneNum
        ResultEntity<String> sendMessageResultEntity = CrowdUtil.sendShortMessage2(
                shortMessageProperties.getHost(),
                shortMessageProperties.getPath(),
                phoneNum,
                shortMessageProperties.getAppCode(),
                shortMessageProperties.getSign(),
                shortMessageProperties.getSkin());

        //2.判断验证码发送结果
        if(ResultEntity.SUCCESS.equals(sendMessageResultEntity.getResult())) {
            //3.如果发送成功，将验证码存入redis

            //获取验证码
            String code = sendMessageResultEntity.getData();
            logger.info("code="+code);
            //拼接redis的key
            String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;


            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeout(key, code, 15, TimeUnit.MINUTES);

            if(saveCodeResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())){
                return ResultEntity.successWithoutData();
            }else{
                return saveCodeResultEntity;
            }
        }else {
            return sendMessageResultEntity;
        }

    }

}
