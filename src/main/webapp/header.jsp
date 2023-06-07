<%@ page import="mvc.JavaBean.User" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    //   过滤器！！！
    Object obj = request.getSession().getAttribute("user");
    //如果没有获得USER的参数，则不进入该页面
    //从而跳转到登录界面
    if(obj==null){
        response.sendRedirect(request.getContextPath()+"/front/login1.jsp");
    }
%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>首页</title>
    <link rel="stylesheet" href="http://localhost:8080/webProject3/css/header.css">
    <script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
</head>
<style>
    #asc{
        cursor: pointer;
    }
</style>
<body>
<div class="header">
    <a href="index.html" class="logo">
        <h1>云蛋糕商城</h1>
    </a>
    <nav class="navbar">
        <ul>
            <li style="width: 300px;"><input name="check" type="text"
                                             style="background: white;
                    margin: 13px;
                    width: 200px;
                    font-size: 16px;
                    height: 20.8px;
                    padding: 0 0;
                    border-radius: 3px;
                    display: inline-flex;"><a style="font-size: 16px;
                                                width: 128px;
                                                line-height: 45px;
                                                " id="asc">搜索</a></li>
            <li><h1>用户：<%
                User user = (User) obj;
                if(user!=null){
            %>
                <%=user.getUsername()%>
                <%
                    }
                %></h1></li>
            <li><a href="http://localhost:8080/webProject3/CakeServlet?act=rexiao">首页</a></li>
            <li><a href="http://localhost:8080/webProject3/CakeServlet?act=show_cake">蛋糕信息</a></li>
            <li><a href="http://localhost:8080/webProject3/CakeServlet?act=sh">热门推荐</a></li>
            <li><a href="http://localhost:8080/webProject3/CakeServlet?act=th">新品推荐</a></li>
            <li><a href="http://localhost:8080/webProject3/CartServlet?act=select">购物车</a></li>
            <li><a href="http://localhost:8080/webProject3/OrderServlet?act=select">订单</a></li>
            <li><a href="http://localhost:8080/webProject3/LogoutServlet">退出登录</a></li>
        </ul>
    </nav>
</div>
</body>
<script>
    $("#asc").click(function(){
        var val = $(".navbar input").val().trim();
        if(val!=""){
            window.location.href="http://localhost:8080/webProject3/CakeServlet?act=souce&cakename="+val;
        }
    })
</script>
</html>
