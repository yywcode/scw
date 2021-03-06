package com.atguigu.crowd.mvc.handler;


import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class AdminHandler {

    @Autowired
    private AdminService adminService;

    @RequestMapping("/admin/update.html")
    public String update(Admin admin, @RequestParam("pageNum") Integer pageNum, @RequestParam("keyword") String keyword) {
        adminService.update(admin);

        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(
            @RequestParam("adminId") Integer adminId,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("keyword") String keyword,
            ModelMap modelMap
    ) {

        //1.根据adminId查询Admin对象，将Admin对象存入模型，
        Admin admin = adminService.getAdminById(adminId);

        modelMap.addAttribute("admin", admin);

        return "admin-edit";
    }


    @PreAuthorize("hasAnyAuthority('user:save')")
    @RequestMapping("/admin/save.html")
    public String save(Admin admin) {

        adminService.saveAdmin(admin);

        return "redirect:/admin/get/page.html?pageNum=" + Integer.MAX_VALUE;
    }


    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(
            @PathVariable("adminId") Integer adminId,
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("keyword") String keyword
    ) {

        //执行删除
        adminService.remove(adminId);
        //重定向到/admin/get/page.html
        //为了保持原本所在的页面和查询关键词再附加pageNum和keyword两个请求参数
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(

            //使用@RequestParam注解的defaultValue属性,指定默认值，在请求中没有携带对应参数时使用默认值
            //keyword默认值使用空字符串，和sql语句配合实现两种情况适配
            @RequestParam(value = "keyword", defaultValue = "") String keyword,

            //pageNum默认值使用1
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            //为了把PageInfo放到模型里
            ModelMap modelMap
    ) {

        //调用Service方法获取PageInfo对象
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);

        //将PageInfo对象存入模型
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);

        return "admin-page";
    }

    @RequestMapping("/admin/do/logout.html")
    public String doLogOut(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/to/login/page.html";
    }

    @RequestMapping("/admin/do/login.html")
    public String doLogin(
            @RequestParam("loginAcct") String loginAcct,
            @RequestParam("userPswd") String userPswd,
            HttpSession session
    ) {
        //调用service方法，执行登录检查
        //如果该方法可以返回admin对象说明登录成功，如果账号密码不正确异常抛出
        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);

        //将登录成功返回的admin对象存入Session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);

        return "redirect:/admin/to/main/page.html";
    }

    @PreFilter(value = "filterObject%2==0")
    @ResponseBody
    @RequestMapping("/admin/test/pre/filter")
    public ResultEntity<List<Integer>> saveList(@RequestBody List<Integer> valueList) {

        return ResultEntity.successWithData(valueList);
    }
}
