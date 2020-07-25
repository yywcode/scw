package com.yyw.crowd.handler;


import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class RedisHandler {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @RequestMapping(value = "/set/redis/key/value/remote",produces = MediaType.APPLICATION_JSON_VALUE)
    ResultEntity<String> setRedisKeyValueRemote(
            @RequestParam("key") String key,
            @RequestParam("value") String value
    ){
        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();

            operations.set(key, value);

            return ResultEntity.successWithoutData();

        } catch (Exception e) {
            e.printStackTrace();

            return ResultEntity.FAILED(e.getMessage());
        }

    }

    @RequestMapping(value = "/set/redis/key/value/remote/with/timeout",produces = MediaType.APPLICATION_JSON_VALUE)
    ResultEntity<String> setRedisKeyValueRemoteWithTimeout(
            @RequestParam("key") String key,
            @RequestParam("value") String value,
            @RequestParam("time") long time,
            @RequestParam("timeUnit") TimeUnit timeUnit
    ){
        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();

            operations.set(key, value, time, timeUnit);

            return ResultEntity.successWithoutData();

        } catch (Exception e) {
            e.printStackTrace();

            return ResultEntity.FAILED(e.getMessage());
        }
    }


    @RequestMapping(value = "/get/redis/string/value/by/key",produces = MediaType.APPLICATION_JSON_VALUE)
    ResultEntity<String> getRedisStringValueByKey(@RequestParam("key") String key){
        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();

            String value = operations.get(key);

            return ResultEntity.successWithData(value);

        } catch (Exception e) {
            e.printStackTrace();

            return ResultEntity.FAILED(e.getMessage());
        }
    }

    @RequestMapping(value = "/remove/Redis/Key/Remote",produces = MediaType.APPLICATION_JSON_VALUE)
    ResultEntity<String> removeRedisKeyRemote(@RequestParam("key") String key){

        try {
            redisTemplate.delete(key);

            return ResultEntity.successWithoutData();

        } catch (Exception e) {
            e.printStackTrace();

            return ResultEntity.FAILED(e.getMessage());
        }
    }
}
