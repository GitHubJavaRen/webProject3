<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" import="java.sql.*"%>
<%@ page import="mvc.JavaBean.Cake" %>
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
<h1 class="callout">蛋糕信息展示</h1>
<h3><a href="http://localhost:8080/webProject3/CakeServlet?act=insert_cake" class="wzc-btn">添加蛋糕</a></h3>
<div class="" style="text-align:center;">
  <table class="wzc-table">
    <tr>
      <th>蛋糕名</th>
      <th>蛋糕图片</th>
      <th>蛋糕类别</th>
      <th>蛋糕价格</th>
      <th>推出时间</th>
      <th>蛋糕关注度</th>
      <th>蛋糕介绍</th>
    </tr>
    <%
      List<Cake> cList = (List<Cake>)request.getAttribute("cakelist");
      if(cList==null){
        request.getRequestDispatcher(request.getContextPath()+"/CakeServlet?act=select").forward(request, response);
      }else{
        for(Cake cake:cList){
    %>
    <tr>
      <td><%=cake.getCake_name()==null?"":cake.getCake_name()%></td>
      <td><img src="<%=cake.getImg()%>" style="width: 200px;height: 200px;"/></td>
      <td><%=cake.getCake_class()==null?"":cake.getCake_class()%></td>
      <td><%=cake.getCake_price()%></td>
      <td><%=cake.getCake_time()%></td>
      <td><%=cake.getCake_num()%></td>
      <td><%=cake.getIntroduce()==null?"":cake.getIntroduce()%></td>
      <td><a href="http://localhost:8080/webProject3/CakeServlet?act=toUpdatePage&cake_name=<%=cake.getCake_name()%>" style="margin:0 10px;">
        <i class="fa fa-edit fa-fw"></i> 修改</a>
        <a href="http://localhost:8080/webProject3/CakeServlet?act=delete&cake_name=<%=cake.getCake_name()%>">
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

