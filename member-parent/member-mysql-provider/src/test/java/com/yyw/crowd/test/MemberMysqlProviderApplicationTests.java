package com.yyw.crowd.test;

import com.yyw.crowd.entity.po.MemberPO;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberMysqlProviderApplicationTests {

    private Logger logger = LoggerFactory.getLogger(MemberMysqlProviderApplicationTests.class);
    @Autowired
    private DataSource dataSource;
    @Resource
    private MemberPOMapper memberMapper;

    @Resource
    private ProjectPOMapper projectPOMapper;

    @Test
    public void testLoadTypeData(){
        List<PortalTypeVO> typeVOList = projectPOMapper.selectPortalTypeVOList();

        for(PortalTypeVO portalTypeVO : typeVOList) {
            String name = portalTypeVO.getName();
            String remark = portalTypeVO.getRemark();
            logger.info("name = "+ name + "remark=" + remark);

            List<PortalProjectVO> portalProjectVOList = portalTypeVO.getPortalProjectVOList();
            for(PortalProjectVO portalProjectVO : portalProjectVOList){

                if(portalProjectVO == null){
                    continue;
                }
                logger.info(portalProjectVO.toString());
            }
        }

    }



    @Test
    public void testMapper(){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = "123123";

        String encode = passwordEncoder.encode(rawPassword);

        MemberPO memberPO = new MemberPO(null, "jack", encode, "杰克", "jack@qq.com", 1, 1, "杰克", "111", 2);

        memberMapper.insert(memberPO);

    }
    @Test
    void testConnection() throws SQLException {

        Connection connection = dataSource.getConnection();

        logger.info("connection="+connection.toString());
    }

}
