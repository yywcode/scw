package com.yyw.crowd.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.util.ResultEntity;
import com.yyw.crowd.api.MySQLRemoteService;
import com.yyw.crowd.entity.vo.PortalTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.model.IModel;

import java.util.List;


@Controller
public class PortalHandler {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    //显示首页
    @RequestMapping("/")
    public String showPortalPage(Model model){

        //1.调用MySqlRemoteService提供的方法查询首页要显示的数据
        ResultEntity<List<PortalTypeVO>> resultEntity = mySQLRemoteService.getPortalTypeProjectData();

        //2.检查查询结果
        String result = resultEntity.getResult();
        if(ResultEntity.SUCCESS.equals(result)){

            //3.获取查询结果的数据
            List<PortalTypeVO> list = resultEntity.getData();

            //4.存入模型
            model.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA, list);
        }

        //这里实际开发中需要加载数据
        return "portal";
    }




}
