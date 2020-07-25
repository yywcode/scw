package com.yyw.crowd.service.impl;


import com.yyw.crowd.entity.po.MemberPO;
import com.yyw.crowd.entity.po.MemberPOExample;
import com.yyw.crowd.mapper.MemberPOMapper;
import com.yyw.crowd.service.api.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;


@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {

    @Resource
    private MemberPOMapper memberPOMapper;

    @Override
    public MemberPO getMemberPOByLoginAcct(String loginacct) {

        //1.创建example对象
        MemberPOExample example = new MemberPOExample();

        //2.创建Criterial对象
        MemberPOExample.Criteria criteria = example.createCriteria();

        //3.封装查询条件
        criteria.andLoginacctEqualTo(loginacct);

        //4.执行查询
        List<MemberPO> list = memberPOMapper.selectByExample(example);

        //5.获取结果
        return list.size() == 0? null : list.get(0);
    }


    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = Exception.class,
            readOnly = false)
    @Override
    public void saveMember(MemberPO memberPO) {

        memberPOMapper.insertSelective(memberPO);
    }
}
