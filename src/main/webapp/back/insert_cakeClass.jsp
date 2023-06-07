<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>新增</title>
    <link rel="stylesheet" type="text/css" href="http://localhost:8080/webProject3/css/app.css">
    <link rel="stylesheet" type="text/css" href="http://localhost:8080/webProject3/css/subpage.css">
</head>
<body>
<h1 class="callout">新增类目信息</h1>
<div>
    <form action="/webProject3/ClassServlet?act=insert" method="post" style="width:100%;text-align:center;">
        <table class="wzc-table">
            <tr>
                <th>类目名</th>
                <td><input name="classname" type="text" ></td>
            </tr>
            <tr>
                <td colspan='2'><input name="sub" type="submit" value="添加" class="wzc-btn" style="width: 200px;"></td>
                <td colspan='2'><a href="/webProject3/CakeServlet?act=pop" class="wzc-btn" style="width: 200px;">返回</a></td>
            </tr>
        </table>
    </form>
</div>

<%
    Object resultMSG = request.getSession().getAttribute("resultMSG");
    if(resultMSG!=null){
%><script type="text/javascript">alert("<%=resultMSG.toString() %>")</script><%
        request.getSession().removeAttribute("resultMSG");
    }
%>
</body>
</html>