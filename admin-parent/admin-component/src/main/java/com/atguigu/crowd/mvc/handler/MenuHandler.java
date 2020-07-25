package com.atguigu.crowd.mvc.handler;


import com.atguigu.crowd.entity.Menu;
import com.atguigu.crowd.service.api.MenuService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class MenuHandler {

    @Autowired
    private MenuService menuService;


    @RequestMapping("/menu/remove.json")
    public ResultEntity<String> removeMenu(@RequestParam("id") Integer id) {

        menuService.removeMenu(id);
        return ResultEntity.successWithoutData();
    }


    @RequestMapping("/menu/update.json")
    public ResultEntity<String> updateMenu(Menu menu) {

        menuService.updateMenu(menu);

        return ResultEntity.successWithoutData();
    }


    @RequestMapping("/menu/save.json")
    public ResultEntity<String> saveMenu(Menu menu) {

        menuService.saveMenu(menu);
        return ResultEntity.successWithoutData();
    }


    @RequestMapping("/menu/get/whole/tree.json")
    public ResultEntity<Menu> getWholeTreeNew() {

        //1.查询全部的Menu对象
        List<Menu> menuList = menuService.getAll();

        //2.声明一个变量用来存储找到的根节点
        Menu root = null;

        //3.创建Map对象用来存储id和Menu对象的对应关系便于查找父节点
        Map<Integer, Menu> menuMap = new HashMap<>();

        //4.遍历menuList填充menuMap
        for (Menu menu : menuList) {

            Integer id = menu.getId();

            menuMap.put(id, menu);
        }

        //5.再次便利menuList查找根节点，组装父子节点
        for (Menu menu : menuList) {

            //6.获取当前menu对象的pid属性值
            Integer pid = menu.getPid();

            //7.如果pid为null的话，判定为根节点
            if (pid == null) {
                root = menu;

                //8.如果当前节点是根据节点，那么肯定没有父节点，不必继续执行
                continue;
            }
            //9.如果pid不为null，说明当前节点有父节点，那么可以根据pid到menuMap中查找对应的Menu对象
            Menu father = menuMap.get(pid);

            //10.将当前节点存入父节点的children集合
            father.getChildren().add(menu);
        }
        //11.经过上面的运算，根节点包含了整个树形结构，返回根节点就是返回整个树
        return ResultEntity.successWithData(root);
    }


    public ResultEntity<Menu> getWholeTreeOld() {
        // 1.查询全部的Menu对象
        List<Menu> menus = menuService.getAll();
        Menu root = null;
        for (Menu menu : menus) {
            // 2.获取当前menu对象的pid属性值
            Integer pid = menu.getPid();
            if (pid == null) {
                // 3.这个pid等于 null 的这个menu就是root节点
                root = menu;
                // 4.停止本次循环，继续执行下一次循环
                continue;
            }
            // 5.开始建立关系, 如果pid不为null，说明当前节点有父节点，找到父节点就可以进行组装，建立父子关系
            for (Menu maybeFather : menus) {
                // 6.获取maybeFather的id属性
                Integer id = maybeFather.getId();
                // 7.将子节点的pid和疑似父节点的id进行比较
                if (Objects.equals(pid, id)) {
                    // 8.将子节点存入父节点的children集合
                    maybeFather.getChildren().add(menu);
                    // 9.找到即可停止运行循环
                    break;
                }
            }
        }
        // 10.将组装的树形结构（也就是根节点对象）返回给浏览器
        return ResultEntity.successWithData(root);
    }
}
