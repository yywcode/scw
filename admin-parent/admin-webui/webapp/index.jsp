<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/7/4
  Time: 20:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>
    <%--http://localhost:8082/test/ssm.html--%>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="layer/layer.js"></script>
    <script type="text/javascript">


        $(function () {
            $("#btn1").click(function () {

                $.ajax({
                    "url": "send/array/one.html",          //请求目标资源的地址
                    "type": "post",                   //请求方式
                    "data": {
                        "array": [5, 8, 12]                //要发送的请求参数
                    },
                    "dataType": "text",               //如何对待服务器端返回的数据
                    "success": function (response) {  //服务器端成功处理请求后调用的回调函数
                        alert(response);
                    },
                    "error": function (response) {     //服务器端处理请求失败后调用的回调函数
                        alert(response);
                    }
                });
            });
            $("#btn2").click(function () {

                $.ajax({
                    "url": "send/array/two.html",          //请求目标资源的地址
                    "type": "post",                   //请求方式
                    "data": {
                        "array[0]": 1,                  //要发送的请求参数
                        "array[1]": 2,                  //要发送的请求参数
                        "array[2]": 3                  //要发送的请求参数
                    },
                    "dataType": "text",                //如何对待服务器端返回的数据
                    "success": function (response) {   //服务器端成功处理请求后调用的回调函数
                        alert(response);
                    },
                    "error": function (response) {      //服务器端处理请求失败后调用的回调函数
                        alert(response);
                    }
                });
            });

            $("#btn3").click(function () {
                //准备好要发送到服务器端的数组
                var array = [4, 5, 6];
                console.log(array.length)
                //将json数组转换成json字符串
                var requestBody = JSON.stringify(array);
                console.log(requestBody.length);
                $.ajax({
                    "url": "send/array/three.html",          //请求目标资源的地址
                    "type": "post",                   //请求方式
                    "data": requestBody,
                    "contentType": "application/json;charset=UTF-8",//设置请求体内容类型，告诉服务器本次请求的请求体是json数据
                    "dataType": "text",               //如何对待服务器端返回的数据
                    "success": function (response) {  //服务器端成功处理请求后调用的回调函数
                        alert(response);
                    },
                    "error": function (response) {     //服务器端处理请求失败后调用的回调函数
                        alert(response);
                    }
                });
            });

            $("#btn4").click(function () {
                //准备好要发送到服务器端的数组
                var student = {
                    "stuId": 5,
                    "stuName": "Tom",
                    "address": {
                        "province": "广东",
                        "city": "深圳",
                        "street": "后端"
                    },
                    "subjectList": [
                        {
                            "subjectName": "JavaSE",
                            "subjectScore": 100
                        }, {
                            "subjectName": "SSM",
                            "subjectScore": 99
                        }
                    ],
                    "map": {
                        "k1": "v1",
                        "k2": "v2"
                    }
                };
                //将json对象转化为json字符串
                var requestBody = JSON.stringify(student);
                //发送ajax请求
                $.ajax({
                    "url": "send/compose/object.json",
                    "type": "post",
                    "data": requestBody,
                    "contentType": "application/json;charset=UTF-8",
                    "dataType": "json",
                    "success": function (response) {
                        console.log(response);
                    },
                    "error": function (response) {
                        console.log(response);
                    }
                });
            });

            $("#btn5").click(function () {
                layer.msg("Layer弹框");
            })
        });
    </script>
</head>
<body>
<a href="test/ssm.html">测试ssm整合环境</a>
<br>


<button id="btn1">Send [5,8,12]</button>
<br>
<button id="btn2">Send [1,2,3]</button>
<br>
<button id="btn3">Send [4,5,6]</button>
<br>
<button id="btn4">Send Student</button>
<br>
<button id="btn5">弹框</button>
</body>
</html>
