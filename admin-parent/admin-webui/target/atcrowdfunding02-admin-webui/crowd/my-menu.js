function generateTree() {
    //1.准备生成树形结构的json数据,数据的来源是发送ajax请求得到
    $.ajax({
        "url": "/menu/get/whole/tree.json",
        "type": "post",
        "dataType": "json",
        "success": function (response) {
            var result = response.result;
            if (result == "SUCCESS") {

                //2.创建json对象用于存储对zTree所作的设置
                var setting = {
                    "view": {
                        "addDiyDom": myAddDiyDom,
                        "addHoverDom": myAddHoverDom,
                        "removeHoverDom": myRemoveHoverDom
                    },
                    "data": {
                        "key": {
                            "url": "maomi"
                        }
                    }
                };

                //3.从响应体中获取生成树形结构的JSON数据
                var zNodes = response.data;

                //3.初始化树形结构
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            }

            if (result == "FAILED") {
                layer.msg(response.message);
            }
        }
    })
}


//在鼠标离开范围时删除按钮组
function myRemoveHoverDom(treeId, treeNode) {
    //找到按钮组的id
    var btnGroupId = treeNode.tId + "_btnGrp";

    //移除对应的元素
    $("#" + btnGroupId).remove();
}

//在鼠标移入节点范围时添加按钮组
function myAddHoverDom(treeId, treeNode) {

    //按钮组的标签结构:<span><a><i></i></a></span>
    //按钮组出现的位置：节点中treeDemo_n_a

    //为了在移除按钮组的时候能够精确定位到按钮组所在的span，需要给按钮组设置有规律的id
    var btnGroupId = treeNode.tId + "_btnGrp";

    //判断一下以前是否已经添加了按钮组
    if ($("#" + btnGroupId).length > 0) {
        return 0;
    }
    //准备各个按钮的html标签
    var addBtn = "<a id='" + treeNode.id + "' class='addBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' title='添加子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
    var removeBtn = "<a id='" + treeNode.id + "' class='removeBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' title=' 删 除 节 点 '>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";
    var editBtn = "<a id='" + treeNode.id + "' class='editBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' title=' 修 改 节 点 '>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";

    //获取当前节点的级别数据
    var level = treeNode.level;

    //声明变量存储拼装好的按钮代码
    var btnHtml = "";

    //判断当前节点的级别
    if (level == 0) {
        //级别为0根节点，只能添加子节点
        btnHtml = btnHtml + addBtn;
    }
    if (level == 1) {
        //分支节点，可以修改
        btnHtml = addBtn + " " + editBtn;

        //获取当前节点的子节点
        var length = treeNode.children.length;
        if (length == 0) {
            //分支节点没有子节点，可以删除
            btnHtml = btnHtml + " " + removeBtn;
        }
    }
    if (level == 2) {
        //叶子节点可以修改删除
        btnHtml = editBtn + " " + removeBtn;
    }

    //找到附着按钮组的超链接
    var anchorId = treeNode.tId + "_a";

    //执行超链接后面附加span元素的操作
    $("#" + anchorId).after("<span id='" + btnGroupId + "'>" + btnHtml + "</span>");


}


function myAddDiyDom(treeId, treeNode) {

    //treeId是整个树形结构附着的树形结构的ul的id
    console.log("treeId=" + treeId);

    //当前树形节点的全部数据，包括从后端查询得到的menu对象的全部属性
    console.log("treeNode=" + treeNode);


    //ztree生成id的规则
    //例子：treeDemo_7_ico
    //解析：ul标签的id_当前节点的序号_功能
    //提示：'ul标签的id_当前节点的序号'可以通过访问treeNode的tId属性获得
    //根据id的生成规则去拼接出来span标签的id
    var spanId = treeNode.tId + "_ico";

    //根据控制图标的span的id找到这个span标签
    //删除旧的class
    //添加新的class
    $("#" + spanId)
        .removeClass()
        .addClass(treeNode.icon)


}