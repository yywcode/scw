package com.yyw.crowd.service.impl;

import com.netflix.discovery.converters.Auto;
import com.yyw.crowd.entity.po.MemberConfirmInfoPO;
import com.yyw.crowd.entity.po.MemberLaunchInfoPO;
import com.yyw.crowd.entity.po.ProjectPO;
import com.yyw.crowd.entity.po.ReturnPO;
import com.yyw.crowd.entity.vo.*;
import com.yyw.crowd.mapper.*;
import com.yyw.crowd.service.api.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Transactional(readOnly = true)
@Service
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ReturnPOMapper returnPOMapper;

    @Resource
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;

    @Resource
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;

    @Resource
    private ProjectPOMapper projectPOMapper;

    @Resource
    private ProjectItemPicPOMapper projectItemPicPOMapper;

    @Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveProject(ProjectVO projectVO, Integer memberId) {

        //1.保存ProjectPO对象
        //①创建ProjectPO空对象
        ProjectPO projectPO = new ProjectPO();

        //2.把ProjectVO中的属性赋值到ProjectPO中
        BeanUtils.copyProperties(projectVO,projectPO);

        //修复bug：把memberId设置到projectPO中
        projectPO.setMemberid(memberId);

        //修复bug：生成创建时间存入
        String createDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        projectPO.setCreatedate(createDate);
        projectPO.setDeploydate(createDate);
        projectPO.setSupporter((int) (Math.random() * 9999));
        projectPO.setSupportmoney((long) (Math.random()* projectPO.getMoney()));
        projectPO.setFollower((int) (Math.random() * 9999));

        //修复bug，status可以设置成0，表示即将开始
        projectPO.setStatus(0);


        //3.保存projectPO
        //为了能够获取到projectPO保存后的自增主键，需要到ProjectPOMapper.xml中进行相应的设置
        projectPOMapper.insertSelective(projectPO);

        //4.从projectPO对象获取自增主键
        Integer projectId = projectPO.getId();

        //二.保存项目、分类关联信息
        //1.从ProjectVO对象中获取项目分类
        List<Integer> typeIdList = projectVO.getTypeIdList();
        projectPOMapper.insertTypeRelationship(typeIdList, projectId);

        //三.保存项目、标签的关联信息
        List<Integer> tagIdList = projectVO.getTagIdList();
        projectPOMapper.insertTagRelationship(tagIdList,projectId);

        //四.保存项目中详情图片路径的信息
        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
        projectItemPicPOMapper.insertPathList(projectId, detailPicturePathList);

        //五.保存项目发起人信息
        MemberLauchInfoVO memberLauchInfoVO = projectVO.getMemberLauchInfoVO();
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
        BeanUtils.copyProperties(memberLauchInfoVO, memberLaunchInfoPO);
        memberLaunchInfoPO.setMemberid(memberId);
        memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);

        //六.保存项目回报信息
        List<ReturnVO> returnVOList = projectVO.getReturnVOList();

        List<ReturnPO> returnPOList = new ArrayList<>();

        for(ReturnVO returnVO : returnVOList){

            ReturnPO returnPO = new ReturnPO();

            BeanUtils.copyProperties(returnVO, returnPO);

            returnPOList.add(returnPO);
        }

        returnPOMapper.insertReturnPOBatch(returnPOList, projectId);

        //七.保存项目确认信息
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
        BeanUtils.copyProperties(memberConfirmInfoVO,memberConfirmInfoPO);
        memberConfirmInfoPO.setMemberid(memberId);
        memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);

    }

    @Override
    public List<PortalTypeVO> getPortalType() {
        return projectPOMapper.selectPortalTypeVOList();
    }

    @Override
    public DetailProjectVO getDetailProjectVO(Integer projectId) {
        // 1.查询得到 DetailProjectVO 对象
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);
        // 2.根据 status 确定 statusText
        Integer status = detailProjectVO.getStatus();
        switch (status) {
            case 0:
                detailProjectVO.setStatusText("审核中");
                break;
            case 1:
                detailProjectVO.setStatusText("众筹中");
                break;
            case 2:
                detailProjectVO.setStatusText("众筹成功");
                break;
            case 3:
                detailProjectVO.setStatusText("已关闭");
                break;
            default:
                break;
        }
        // 3.根据 deployeDate 计算 lastDay
        // 2020-10-15
        String deployDate = detailProjectVO.getDeployDate();
        // 获取当前日期
        Date currentDay = new Date();
        // 把众筹日期解析成 Date 类型
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date deployDay = format.parse(deployDate);
            // 获取当前当前日期的时间戳
            long currentTimeStamp = currentDay.getTime();
            // 获取众筹日期的时间戳
            long deployTimeStamp = deployDay.getTime();
            // 两个时间戳相减计算当前已经过去的时间
            long pastDays = (currentTimeStamp - deployTimeStamp) / 1000 / 60 / 60 / 24;
            // 获取总的众筹天数
            Integer totalDays = detailProjectVO.getDay();
            // 使用总的众筹天数减去已经过去的天数得到剩余天数
            Integer lastDay = (int) (totalDays - pastDays);
            detailProjectVO.setLastDay(lastDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return detailProjectVO;
    }
}
