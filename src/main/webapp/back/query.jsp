<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" import="java.sql.*"%>
<%@ page import="mvc.JavaBean.User" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>查看</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <link rel="stylesheet" type="text/css" href="http://localhost:8080/webProject3/css/app.css">
    <link rel="stylesheet" type="text/css" href="http://localhost:8080/webProject3/font-awesome-4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="http://localhost:8080/webProject3/css/subpage.css">
</head>
<body>
<h1 class="callout">用户管理</h1>
<h3><a href="/webProject3/back/insert.jsp" class="wzc-btn">添加用户</a></h3>
<div class="" style="text-align:center;">
    <table class="wzc-table">
        <tr>
            <th>用户名</th>
            <th>密码</th>
        </tr>
        <%
            List<User> uList = (List<User>)request.getAttribute("userList");
            if(uList==null){
                request.getRequestDispatcher(request.getContextPath()+"/UserServlet?act=select").forward(request, response);
            }else{
                for(User user:uList){
        %>
        <tr>
            <td><%=user.getUsername()==null?"":user.getUsername()%></td>
            <td><%=user.getPassword()==null?"":user.getPassword()%></td>
            <td><a href="/webProject3/UserServlet?act=toUpdatePage&username=<%=user.getUsername()%>" style="margin:0 10px;">
                <i class="fa fa-edit fa-fw"></i> 修改</a>
                <a href="/webProject3/UserServlet?act=delete&username=<%=user.getUsername()%>">
                    <i class="fa fa-trash fa-fw"></i> 删除</a>
            </td>
        </tr>
        <%}} %>
    </table>
    <%
        Object resultMSG = request.getSession().getAttribute("resultMSG");
        if(resultMSG!=null){
    %><script type="text/javascript">alert("<%=resultMSG.toString() %>")</script><%
        request.getSession().removeAttribute("resultMSG");
    }
%>
</div>
</body>
</html>

