package com.yyw.handler;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class HelloHandler {

    @RequestMapping("/test/spring/session/save")
    public String testSession(HttpSession session){

        session.setAttribute("king","helloKing");

        return "数据存入session域";
    }

}
