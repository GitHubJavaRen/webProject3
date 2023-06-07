<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    //   过滤器！！！
	Object obj = request.getSession().getAttribute("user");
    //如果没有获得USER的参数，则不进入该页面
    //从而跳转到登录界面
	if(obj==null){
		response.sendRedirect(request.getContextPath()+"/front/login1.jsp");
	}
%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>后台管理系统</title>
    <link rel="stylesheet" type="text/css" href="http://localhost:8080/webProject3/css/app.css">
	<link rel="stylesheet" type="text/css" href="http://localhost:8080/webProject3/css/index.css">
	<link rel="stylesheet" type="text/css" href="http://localhost:8080/webProject3/font-awesome-4.7.0/css/font-awesome.min.css">
	<script src="http://localhost:8080/webProject3/js/jquery.min.js"></script>
</head>
<body>
<div id="app">
    <div class="slidebar"><%--左侧区域--%>
        <div class="slidebar-logo" >
            <div class="logo-icon"><i class="fa fa-cog fa-spin"></i></div>
            <span class="title">后台管理系统</span>
        </div>
        <ul class="slidebar-warpper">
            <li class="nav-item">
                <a href="/webProject3/back/workbench.jsp" target="mainframe">
                    <i class="fa fa-home fa-fw fa-lg"></i>
                    <span>欢迎界面</span></a>
            </li>
            <li class="nav-item">
                <a href="/webProject3/UserServlet?act=select" target="mainframe">
                    <i class="fa fa-child fa-fw fa-lg"></i>
                    <span>用户管理</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="javascript:;">
                    <i class="fa fa-birthday-cake fa-fw fa-lg"></i>
                    <span>蛋糕管理</span>
                    <i class="nav-more fa fa-chevron-right"></i>
                </a>
                <ul>
                    <li><a href="/webProject3/CakeServlet?act=select" target="mainframe"><span>蛋糕信息</span></a></li>
                    <li><a href="/webProject3/CakeServlet?act=pop" target="mainframe"><span>蛋糕类目</span></a></li>
                </ul>
            </li>
            <li class="nav-item">
                <a href="javascript:;">
                    <i class="fa fa-shopping-cart fa-fw fa-lg"></i>
                    <span>订单管理</span>
                    <i class="nav-more fa fa-chevron-right"></i>
                </a>
                <ul>
                    <li><a href="/webProject3/OrderServlet?act=getAll" target="mainframe"><span>订单查看</span></a></li>
                </ul>
            </li>
            <li class="nav-item"><a href="/webProject3/LogoutServlet" ><i class="fa fa-sign-out fa-fw fa-lg"></i> <span>退出登录</span></a></li>
        </ul>
    </div>
    <div class="container"><%--右侧区域--%>
        <div class="navbar">
            <div class="fold-btn" ><i class="fa fa-bars"></i></div>
            <a href="/webProject3/LogoutServlet"><i class="fa fa-sign-out fa-fw"></i> 退出登录</a>
            <span>当前用户：${sessionScope.user.username }</span>
        </div>
        <div class="container-main">
            <iframe name="mainframe" src="/webProject3/back/workbench.jsp" frameborder="0" class="iframe-full"></iframe>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function(){
        // nav收缩展开
        $('.nav-item>a').on('click',function(){
            if (!$('.nav').hasClass('nav-mini')) {
                if ($(this).next().css('display') == "none") {
                    //展开未展开
                    $('.nav-item').children('ul').slideUp(300);
                    $(this).next('ul').slideDown(300);
                    $(this).parent('li').addClass('nav-show').siblings('li').removeClass('nav-show');
                }else{
                    //收缩已展开
                    $(this).next('ul').slideUp(300);
                    $('.nav-item.nav-show').removeClass('nav-show');
                }
            }
        });
        //nav-mini切换
        $('.fold-btn').on('click',function(){
            $('.nav-item.nav-show').toggleClass('nav-show');
            // $('.nav-item').children('ul').removeAttr('style');
            $('.slidebar').toggleClass("fold");
            $('.fold-btn>.fa').toggleClass("fa-rotate-90");
        });
    });
</script>
</body>
</html>