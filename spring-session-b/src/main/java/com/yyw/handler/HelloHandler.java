package com.yyw.handler;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class HelloHandler {

    @RequestMapping("/test/spring/session/retrieve")
    public String testSession(HttpSession session){

        String value = (String) session.getAttribute("king");

        return value;
    }
}
