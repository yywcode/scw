package com.yyw.crowd.handler;


import com.atguigu.crowd.util.ResultEntity;
import com.yyw.crowd.entity.vo.DetailProjectVO;
import com.yyw.crowd.entity.vo.PortalTypeVO;
import com.yyw.crowd.entity.vo.ProjectVO;
import com.yyw.crowd.service.api.ProjectService;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.List;

@RestController
public class ProjectProviderHandler {
    Logger logger = LoggerFactory.getLogger(ProjectProviderHandler.class);
    @Autowired
    private ProjectService projectService;

    @RequestMapping(value = "/get/project/detail/remote/{projectId}",produces = MediaType.APPLICATION_JSON_VALUE )
    public ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId){

        try {
            DetailProjectVO detailProjectVO = projectService.getDetailProjectVO(projectId);
            logger.info(detailProjectVO.toString());
            return ResultEntity.successWithData(detailProjectVO);


        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.FAILED(e.getMessage());
        }
    }



    @RequestMapping(value = "/get/portal/type/project/data/remote",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultEntity<List<PortalTypeVO>> getPortalTypeProjectData(){

        try {
            List<PortalTypeVO> portalTypeList = projectService.getPortalType();
            return ResultEntity.successWithData(portalTypeList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.FAILED(e.getMessage());
        }
    }

    @RequestMapping(value = "/save/project/vo/remot",produces = MediaType.APPLICATION_JSON_VALUE)
    ResultEntity<String> saveProjectVORemote(
            @RequestBody ProjectVO projectVO,
            @RequestParam("memberId") Integer memberId){
        try {
            //调用本地service执行保存
            projectService.saveProject(projectVO,memberId);

            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.FAILED(e.getMessage());
        }
    }

}
