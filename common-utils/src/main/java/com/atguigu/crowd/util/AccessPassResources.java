package com.atguigu.crowd.util;

import com.atguigu.crowd.constant.CrowdConstant;

import java.util.HashSet;
import java.util.Set;

public class AccessPassResources {

    public static final Set<String> PASS_RES_SET =  new HashSet<>();

    static{

        PASS_RES_SET.add("/");
        PASS_RES_SET.add("/auth/member/to/login/page");
        PASS_RES_SET.add("/auth/member/logout");
        PASS_RES_SET.add("/auth/member/to/reg/page");
        PASS_RES_SET.add("/auth/member/do/login");
        PASS_RES_SET.add("/auth/do/member/register");
        PASS_RES_SET.add("/auth/member/send/short/message.json");

    }

    public static final Set<String> STATIC_RES_SET = new HashSet<>();


    /*放行的静态资源*/
    static {
        STATIC_RES_SET.add("bootstrap");
        STATIC_RES_SET.add("css");
        STATIC_RES_SET.add("fonts");
        STATIC_RES_SET.add("img");
        STATIC_RES_SET.add("jquery");
        STATIC_RES_SET.add("layer");
        STATIC_RES_SET.add("script");
        STATIC_RES_SET.add("ztree");
    }

    /*
    * 用于判断某个ServletPath是否对应一个静态资源
    * */
    public static boolean judgeCurrentServletPathWhetherStaticResource(String servletPath){

        //排除字符串无效
        if(servletPath == null || servletPath.length()==0){
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }

        //请求地址按照/去切割
        String[] split = servletPath.split("/");

        //考虑到第一个斜杠左边经过拆分后得到一个空字符串，所以需要使用下标1取第二个元素
        String firstLevelPath = split[1];

        //判断是否在静态资源集合中
        return STATIC_RES_SET.contains(firstLevelPath);
    }


}
