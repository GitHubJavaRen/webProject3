<%@ page import="mvc.JavaBean.User" %>
<%@ page import="mvc.JavaBean.Cake" %>
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
    <link rel="stylesheet" href="http://localhost:8080/webProject3/css/table.css">
    <script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
    <script src="./js/index.js"></script>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>

<div id="box_autoplay" class="">
    <ul class="img">
        <li><img src="./img/002.jpg"/></li>
        <li><img src="./img/001.jpg"/></li>
        <li><img src="./img/003.jpg"/></li>
        <li><img src="./img/004.jpg"/></li>
        <li><img src="./img/005.jpg"/></li>
    </ul>
    <ul class="num"></ul>
    <!--左右点击按钮-->
    <div class="btn">
        <span class="prev"><</span>
        <span class="next">></span>
    </div>
</div>

<div id="divcontent">
    <div id="lt">
        <p>公告板</p>
        <h1 style="width: 100%">本店蛋糕巨好吃！名师何仁制作</h1>
    </div>
    <div id="rt">
        <p>本周热卖</p>
        <%
            Cake cake = (Cake) request.getAttribute("cake");//获取服务器传来的最热门的的蛋糕
            if(cake!=null){
        %>
        <img src="<%=cake.getImg()%>">
        <%
        }else{
        %>
        <img src="./img/003.jpg">
        <%
            }
        %>
    </div>
</div>

<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
