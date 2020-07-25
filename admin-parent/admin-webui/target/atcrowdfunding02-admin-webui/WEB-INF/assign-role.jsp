<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/7/7
  Time: 15:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<%@include file="include/include-head.jsp" %>
<script type="text/javascript">
    $(function () {
        $("#toRightBtn").click(function () {
            //eq(0)表示第一个select，>寻找子元素,appendTo将选中元素放到指定对象
            $("select:eq(0)>option:selected").appendTo("select:eq(1)");
        })

        $("#toLeftBtn").click(function () {

            $("select:eq(1)>option:selected").appendTo("select:eq(0)");
        })

        $("#submitBtn").click(function () {
            $("select:eq(1)>option").prop("selected", "selected");
        })


        //清除下拉框的选择
        $("#showSelectAuth").click(function () {
           window.location.href="${pageContext.request.contextPath}/assign/to/assign/role/page.html?adminId=${param.adminId}"

        })
        
        
        

        $(".selectOption").change(function () {
            var roleArray = [];
            //遍历被选中的option
            $(".selectOption option:selected").each(function () {

                //使用this引用当前遍历到的多选框
                var roleId = $(this).val();
                roleArray.push(roleId);
            });
            var requestBody = JSON.stringify(roleArray);
            var ajaxResult = $.ajax({
                "url":"assign/get/auth/by/select/role.json",
                "type":"post",
                "data":requestBody,
                "contentType":"application/json;charset=UTF-8",
                "dataType":"json",
                "success":function (data) {
                    $("#authPageBody tbody").html("")
                    var authList = data.data;
                    console.log(authList)
                    fillAuthTableBody(authList);
                }
            });

        });

        function fillAuthTableBody(authList) {
            //判断authList是否有效
            if (authList == null || authList == undefined || authList.length==0){
                $("#authPageBody").append("<tr><td colspan='3'>所选角色未分配权限</td></tr>");
                return;
            }

            //使用authList填充tbody
            for (var i = 0; i < authList.length;i++) {

                var auth = authList[i];

                var auth_name = authList[i].name;

                var auth_title = authList[i].title;

                var numberTd = "<td>" + (i + 1) + "</td>";
                var nameTd = "<td>" + auth_name + "</td>";
                var titleTd = "<td>" + auth_title + "</td>";

                var tr = "<tr>" + numberTd + nameTd + titleTd + "</tr>";

                $("#authPageBody").append(tr);
            }
        }
    })
</script>
<body>
<%@ include file="include/include-nav.jsp" %>
<style>

    .btn-block {
        position: relative;
        top: 10px;
        left: 90px;
    }
    .form-inline{
        width: 450px;
    }
    
</style>
<div class="container-fluid">
    <div class="row">
        <%@include file="include/include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="#">首页</a></li>
                <li><a href="#">数据列表</a></li>
                <li class="active">分配角色</li>
            </ol>
            <button id="showSelectAuth" type="button" class="btn btn-primary" style="float:right;"><i
                    class="glyphicon glyphicon-plus"></i> 显示权限
            </button>
            <div class="panel panel-default">
                <div class="panel-body">
                    <form action="assign/do/role/assign.html" method="post" role="form" class="form-inline">
                        <input type="hidden" name="adminId" value="${param.adminId}">
                        <input type="hidden" name="pageNum" value="${param.pageNum}">
                        <input type="hidden" name="keyword" value="${param.keyword}">
                        <div class="form-group">
                            <label for="exampleInputPassword1">未分配角色列表</label><br>
                            <select
                                    class="form-control selectOption" multiple size="10" style="width:100px;overflow-y:auto;">
                                <c:forEach items="${requestScope.unAssignedRoleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <ul>
                                <li id="toRightBtn" class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                                <br>
                                <li id="toLeftBtn" class="btn btn-default glyphicon glyphicon-chevron-left"
                                    style="margin-top:20px;"></li>
                            </ul>
                        </div>
                        <div id="father" class="form-group selectOption" style="margin-left:40px;">
                            <label for="exampleInputPassword1">已分配角色列表</label><br>
                            <select name="roleIdList"
                                    class="form-control" multiple="multiple" size="10"
                                    style="width:100px;overflow-y:auto;">
                                <c:forEach items="${requestScope.assignedRoleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>

                        </div>
                        <button id="submitBtn" type="submit" style="width:150px;"
                                class="btn btn-lg btn-success btn-block">保存
                        </button>
                    </form>
                </div>


                <div class="table-responsive">
                    <table id="authPageBody" class="table  table-bordered">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th>权限代码</th>
                            <th>权限内容</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${empty requestScope.authList}">
                            <tr>
                                <td colspan="6" align="center">抱歉，没有查询到想要的数据</td>
                            </tr>
                        </c:if>
                        <c:if test="${!empty requestScope.authList}">
                            <c:forEach items="${requestScope.authList}" var="auth" varStatus="myStatus">
                                <tr>
                                    <td>${myStatus.count}</td>
                                    <td>${auth.name}</td>
                                    <td>${auth.title}</td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        </tbody>

                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>

