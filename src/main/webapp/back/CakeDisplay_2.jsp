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
    <script src="http://localhost:8080/webProject3/js/jquery.min.js"></script>
</head>
<body>
<h1 class="callout">蛋糕分类展示</h1>
<div class="qe">
    <%
        List<String> classlist = (List<String>) request.getAttribute("classlist");
    %>
    <h3>
        <a href="http://localhost:8080/webProject3/back/insert_cakeClass.jsp" class="wzc-btn">新增类目</a>
        <a href="http://localhost:8080/webProject3/ClassServlet?act=update_para" class="wzc-btn" style="background-color: green">修改类目</a>
        <a href="http://localhost:8080/webProject3/ClassServlet?act=delete_para" class="wzc-btn" style="background-color: red">删除类目</a>
    </h3>
    <div class="select_style_2" >
        <select name="select" style="float: right;margin: 0" id="class_name">
            <option value="qwe" selected>请选择类目</option>
            <%
                if(classlist==null){
                    request.getRequestDispatcher(request.getContextPath()+"/CakeServlet?act=pop").forward(request, response);
                }else{
                    for(String name:classlist){
                    %>
            <option value="<%=name%>"><%=name%></option>
            <%} }%>
        </select>
    </div>
</div>
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
                request.getRequestDispatcher(request.getContextPath()+"/CakeServlet?act=pop").forward(request, response);
            }else{
                for(Cake cake:cList){
        %>
        <div id="asd">
        <tr>
            <td><%=cake.getCake_name()==null?"":cake.getCake_name()%></td>
            <td><img src="<%=cake.getImg()%>" style="width: 200px;height: 200px;"/></td>
            <td><%=cake.getCake_class()==null?"":cake.getCake_class()%></td>
            <td><%=cake.getCake_price()%></td>
            <td><%=cake.getCake_time()%></td>
            <td><%=cake.getCake_num()%></td>
            <td><%=cake.getIntroduce()==null?"":cake.getIntroduce()%></td>
        </tr>
        </div>
        <%}} %>
    </table>
    <script type="text/javascript">
    <%
        Object resultMSG = request.getSession().getAttribute("resultMSG");
        if(resultMSG!=null){
    %>
    alert("<%=resultMSG.toString() %>")
<%
        request.getSession().removeAttribute("resultMSG");
    }
%>
    $('#class_name').change(function(){
    //获取option元素上的value值
        var val = $(this).find('option:selected').val();
        $.ajax({
            url:"http://localhost:8080/webProject3/ClassServlet?act=select",
            type:"get",
            dataType:"json",
            data:{"select":val},
            success:function (msg){
                var str = "";
                var str1 = "<tr>"+
                    "<th>蛋糕名</th>"+
                    "<th>蛋糕类别</th>"+
                    "<th>蛋糕价格</th>"+
                    "<th>推出时间</th>"+
                    "<th>蛋糕关注度</th>"+
                    "<th>蛋糕介绍</th>"+
                    "</tr>";
                for(i in msg){
                    str += "<tr>" +
                        "<td>" + msg[i].cake_name + "</td>" +
                        "<td><img src='" + msg[i].img + "' style='width: 200px;height: 200px;'/></td>" +
                        "<td>" + msg[i].cake_class + "</td>" +
                        "<td>" + msg[i].cake_price+ "</td>" +
                        "<td>" + msg[i].cake_time + "</td>" +
                        "<td>" + msg[i].cake_num + "</td>" +
                        "<td>" + msg[i].introduce + "</td>" +
                        "</tr>";
                    // console.log(msg[i].cake_name);
                }
                $(".wzc-table tbody").html(str1+str);
            }
         })
    })
    </script>
</div>
</body>
</html>

