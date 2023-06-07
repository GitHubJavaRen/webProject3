<%@ page import="mvc.JavaBean.Cake" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>修改</title>
  <link rel="stylesheet" type="text/css" href="http://localhost:8080/webProject3/css/app.css">
  <link rel="stylesheet" type="text/css" href="http://localhost:8080/webProject3/css/subpage.css">
</head>
<body>
<%
  Cake cake = (Cake) request.getAttribute("updateCake");
%>
<h1 class="callout">用户信息修改</h1>
<div>
  <form action="/webProject3/CakeServlet?act=update" method="post" style="width:100%;text-align:center;">
    <table class="wzc-table">
      <tr>
        <th>蛋糕名</th>
        <td><input name="cake_name" type="text" value='<%=cake.getCake_name()==null?"":cake.getCake_name()%>'></td>
      </tr>
      <tr>
        <th>蛋糕类别</th>
        <td class="select_style">
        <select name="select">
          <option value="水果蛋糕">水果蛋糕</option>
          <option value="恶搞蛋糕">恶搞蛋糕</option>
          <option value="生日蛋糕">生日蛋糕</option>
        </select></td>
      </tr>
      <tr>
        <th>蛋糕图片链接</th>
        <th><input name="cake_img" type="text" value="<%=cake.getImg()%>"></th>
      </tr>
      <tr>
        <th>蛋糕价格</th>
        <td><input name="cake_price" type="text" value='<%=cake.getCake_price()%>'></td>
      </tr>
      <tr>
        <th>蛋糕关注度</th>
        <td><input name="cake_num" type="text" value='<%=cake.getCake_num()%>'></td>
      </tr>
      <tr>
        <th>蛋糕介绍</th>
        <td><input name="cake_introduce" type="text" value='<%=cake.getIntroduce()%>'></td>
      </tr>
      <tr>
        <td colspan='2'><input name="sub" type="submit" value="修改" class="wzc-btn" style="width: 200px;"></td>
        <td colspan='2'><a href="/webProject3/CakeServlet?act=select" class="wzc-btn" style="width: 200px;">返回</a></td>
      </tr>
    </table>
  </form>
</div>
<%
  //弹出警告框
  Object resultMSG = request.getSession().getAttribute("resultMSG");
  if(resultMSG!=null){
%>
<script type="text/javascript">
  alert("<%=resultMSG.toString() %>")
</script>
<%
    request.getSession().removeAttribute("resultMSG");
  }
%>
</body>
</html>