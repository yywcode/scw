package com.yyw.crowd.test;

import com.yyw.crowd.entity.vo.DetailProjectVO;
import com.yyw.crowd.entity.vo.DetailReturnVO;
import com.yyw.crowd.entity.vo.PortalProjectVO;
import com.yyw.crowd.entity.vo.PortalTypeVO;
import com.yyw.crowd.mapper.MemberPOMapper;
import com.yyw.crowd.mapper.ProjectPOMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private MemberPOMapper memberPOMapper;
    @Autowired
    private ProjectPOMapper projectPOMapper;

    private Logger logger = LoggerFactory.getLogger(MybatisTest.class);


    @Test
    public void testLoadDetailProjectVO(){
        Integer id = 21;
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(id);

        logger.info(detailProjectVO.getProjectId() + "");
        logger.info(detailProjectVO.getProjectName() + "");
        logger.info(detailProjectVO.getProjectDesc() + "");
        logger.info(detailProjectVO.getStatus() + "");
        logger.info(detailProjectVO.getMoney() + "");
        List<String> detailPicturePathList = detailProjectVO.getDetailPicturePathList();
        for (String s : detailPicturePathList) {
            logger.info(s);
        }
        List<DetailReturnVO> detailReturnVOList = detailProjectVO.getDetailReturnVOList();
        for(DetailReturnVO detailReturnVO : detailReturnVOList){
            logger.info(detailProjectVO.getDetailPicturePathList().toString());
        }
    }

    @Test
    public void testLoadTypeData() {
        List<PortalTypeVO> typeVOList = projectPOMapper.selectPortalTypeVOList();
        for (PortalTypeVO portalTypeVO : typeVOList) {
            Integer id = portalTypeVO.getId();
            String name = portalTypeVO.getName();
            String remark = portalTypeVO.getRemark();
            logger.info("name = " + name + "remark=" + remark);
            List<PortalProjectVO> portalProjectVOList = portalTypeVO.getPortalProjectVOList();
            for (PortalProjectVO portalProjectVO : portalProjectVOList) {
                if (portalProjectVO == null) {
                    continue;
                }
                logger.info(portalProjectVO.toString());
            }
        }
    }
}