package com.yyw.handler;


import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.netflix.discovery.converters.Auto;
import com.netflix.ribbon.proxy.annotation.Http;
import com.yyw.config.OSSProperties;

import com.yyw.crowd.api.MySQLRemoteService;
import com.yyw.crowd.entity.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Result;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProjectConsumerHandler {

    @Autowired
    private OSSProperties ossProperties;
    private Logger logger = LoggerFactory.getLogger(ProjectConsumerHandler.class);

    @Resource
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/get/project/detail/{projectId}")
    public String getProjectDetail(@PathVariable("projectId") Integer projectId, Model model){

        ResultEntity<DetailProjectVO> resultEntity = mySQLRemoteService.getDetailProjectVORemote(projectId);

        if(ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            DetailProjectVO detailProjectVO = resultEntity.getData();

            model.addAttribute("detailProjectVO", detailProjectVO);
            logger.info(detailProjectVO.toString());
        }
        return "project-show-detail";
    }


    @RequestMapping("/create/confirm")
    public String saveConfirm(
            ModelMap modelMap,
            HttpSession session,
            MemberConfirmInfoVO memberConfirmInfoVO
    ){

        //1.从session域中读取临时存储projectVO对象
        ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

        //2.如果projectVO为null
        if(projectVO == null) {
            throw new RuntimeException(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
        }
        //3.将确认信息数据设置到projectVO对象中
        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);

        //4.从session域读取当前登录的用户
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        Integer memberId = memberLoginVO.getId();

        //5.调用远程方法保存projectVO这个对象
        ResultEntity<String> saveResultEntity = mySQLRemoteService.saveProjectVORemote(projectVO, memberId);

        //6.去判断远程的保存操作是否成功
        String result = saveResultEntity.getResult();
        if(ResultEntity.FAILED.equals(result)){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,saveResultEntity.getMessage());
            return "project-confirm";
        }
        //7.将临时的ProjectVO对象从Session域中移除
        session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

        //7.如果远程保存成功，则跳转成功页面
        return "redirect:http://localhost:521/project/create/success";
    }

    @ResponseBody
    @RequestMapping(value = "/create/save/return.json",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultEntity<String> saveReturn(ReturnVO returnVO, HttpSession session) {

        try {
            //1.从session域中读取之前缓存的projectVO对象
            ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

            //2.判断projectVO是否为null
            if(projectVO == null){
                return ResultEntity.FAILED(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
            }

            //3.从projectVO对象中获取存储回报信息的集合
            List<ReturnVO> returnVOList = projectVO.getReturnVOList();

            //4.判断returnVOList集合是否有效
            if(returnVOList == null || returnVOList.size()==0) {

                //5.创建集合对象对returnVOList初始化
                returnVOList = new ArrayList<>();

                //6.为了正常使用returnVOList，设置到projectVO对象中
                projectVO.setReturnVOList(returnVOList);
            }

            //7.将收集了表单数据的returnVO对象存入集合
            returnVOList.add(returnVO);

            //8.把数据有变化的projectVO对象重新存入session域以确保新的数据可以存入Redis
            session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);

            //9.所有操作成功完成
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();

            return ResultEntity.FAILED(e.getMessage());
        }



    }



    /*
    * JS中formData.append("returnPicture", file);
    * returnPicture就是请求参数的名字
    * */
    @ResponseBody
    @RequestMapping("/create/upload/return/picture.json")
    public ResultEntity<String> uploadReturnPicture(
            @RequestParam("returnPicture") MultipartFile returnPicture
            ) throws IOException {
        //1.执行文件上传
        ResultEntity<String> uploadReturnPicResultEntity = CrowdUtil.uploadFileToOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                returnPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                returnPicture.getOriginalFilename()
        );

        //2.返回上传的结果

        return uploadReturnPicResultEntity;
    }




    @RequestMapping("/create/project/information")
    public String saveProjectBasicInfo(
            //用于接收除了上传图片外的其他普通数据
            ProjectVO projectVO,

            //接收上传的头图
            MultipartFile headerPicture,

            //接收上传详情的图片
            List<MultipartFile> detailPictureList,

            //用来收集一部分数据的ProjectVO对象存入Session域
            HttpSession session,

            //用来在当前操作失败后返回上一个表单页面并携带提示消息
            ModelMap modelMap
    ) throws IOException {

        //1.完成头图的上传

        //①获取当前headerPicture对象是否为空
        boolean headerPictureEmpty = headerPicture.isEmpty();
        //②如果headPicture为空返回表单页面并返回错误消息
        if(headerPictureEmpty) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_EMPTY);

            return "project-launch";

        }
        //2.若用户确实上传了有内容的文件则执行上传
        ResultEntity<String> uploadHeaderPicResultEntity = CrowdUtil.uploadFileToOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                headerPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                headerPicture.getOriginalFilename()
        );
        String result = uploadHeaderPicResultEntity.getResult();

        //3.判断头图是否上传成功
        if(ResultEntity.SUCCESS.equals(result)){

            //4.从返回的数据中获取图片访问路径
            String headerPicturePath = uploadHeaderPicResultEntity.getData();

            //5.存入ProjectVO对象中
            projectVO.setHeaderPicturePath(headerPicturePath);
        }else {
            //6.如果上传失败就返回上一个页面并携带错误消息
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);

            return "project-launch";
        }

        //上传详情图片
        //创建一个用来存放详情图片路径的集合
        List<String> detailPicturePathList = new ArrayList<>();

        //检查detailPictureList是否有效
        if(detailPictureList == null || detailPictureList.size() == 0){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);

            return "project-launch";
        }

        //遍历detailPictureList集合
        for(MultipartFile detailPicture : detailPictureList) {

            //当前detailPicture是否为空
            if(detailPicture.isEmpty()){
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);

                return "project-launch";
            }

            //执行上传
            ResultEntity<String> uploadDetailPicResultEntity = CrowdUtil.uploadFileToOss(
                    ossProperties.getEndPoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    detailPicture.getInputStream(),
                    ossProperties.getBucketName(),
                    ossProperties.getBucketDomain(),
                    detailPicture.getOriginalFilename()
            );
            //检查上传的结果
            String detailUploadResult = uploadDetailPicResultEntity.getResult();

            if(ResultEntity.SUCCESS.equals(detailUploadResult)){

                //收集上传的详情图片的路径
                String detailPicturePath = uploadDetailPicResultEntity.getData();
                detailPicturePathList.add(detailPicturePath);
            }else{
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_UPLOAD_FAILED);

                return "project-launch";
            }
        }
        //将存放了详情图片访问路径的集合存入ProjectVO中
        projectVO.setDetailPicturePathList(detailPicturePathList);

        //将ProjectVO对象存入session域
        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);

        //以完整的访问路径，前往下一个收集回报信息的页面
        return "redirect:http://localhost:521/project/return/info/page";
    }


}

