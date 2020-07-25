package com.yyw.crowd.service.api;

import com.yyw.crowd.entity.vo.DetailProjectVO;
import com.yyw.crowd.entity.vo.PortalTypeVO;
import com.yyw.crowd.entity.vo.ProjectVO;

import java.util.List;

public interface ProjectService {

    void saveProject(ProjectVO projectVO,Integer memberId);

    List<PortalTypeVO> getPortalType();

    DetailProjectVO getDetailProjectVO(Integer projectId);
}
