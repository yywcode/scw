package com.yyw.crowd.api;


import com.atguigu.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.TimeUnit;

@FeignClient("yyw-crowd-redis")
public interface RedisRemoteService {

    @RequestMapping(value = "/set/redis/key/value/remote",produces = MediaType.APPLICATION_JSON_VALUE)
    ResultEntity<String> setRedisKeyValueRemote(
            @RequestParam("key") String key,
            @RequestParam("value") String value
    );

    @RequestMapping(value = "/set/redis/key/value/remote/with/timeout",produces = MediaType.APPLICATION_JSON_VALUE)
    ResultEntity<String> setRedisKeyValueRemoteWithTimeout(
            @RequestParam("key") String key,
            @RequestParam("value") String value,
            @RequestParam("time") long time,
            @RequestParam("timeUnit") TimeUnit timeUnit
    );


    @RequestMapping(value = "/get/redis/string/value/by/key",produces = MediaType.APPLICATION_JSON_VALUE)
    ResultEntity<String> getRedisStringValueByKey(@RequestParam("key") String key);

    @RequestMapping(value = "/remove/Redis/Key/Remote",produces = MediaType.APPLICATION_JSON_VALUE)
    ResultEntity<String> removeRedisKeyRemote(@RequestParam("key") String key);
}
