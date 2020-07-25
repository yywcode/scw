package com.atguigu.crowd.mvc.handler;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class TestHandler2 {

    @ResponseBody
    @RequestMapping("/test/ajax/async.html")
    public String testAsync() throws InterruptedException {

        Thread.sleep(2000);
        return "success";
    }
}
