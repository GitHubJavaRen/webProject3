<%@ page import="mvc.JavaBean.Cake" %>
<%@ page import="java.util.List" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="http://localhost:8080/webProject3/js/jquery.min.js"></script>
</head>

<style>
    * {
        margin: 0;
        padding: 0;
    }

    a {
        text-decoration: none;
        color: black;
    }

    body {
        background-color: #f5f5f5;
    }

    .box {
        width: 24.73%;
        height: 240px;
        background-color: #fff;
        margin: 20px 2px;
        border-radius: 14px;
        float: left;
        border: #2C2C2C solid;
        top: 40px;
        position: relative;
    }

    .box img {
        width: 100%;
        border-radius: 14px 14px 0 0;
    }

    .box p{
        font-size: 14px;
        padding: 0 16px;
        margin-top: 20px;
    }

    .text {
        font-size: 14px;
        color: #cdcdcd;
        padding: 0 16px;
        margin-top: 10px;
    }

    .box_a {
        padding: 0 16px;
        margin-top: 20px;
    }

    .box_a h2 {
        display: inline-block;
        font-weight: 400;
        font-size: 20px;
        text-align: center;
    }

    .box_a em {
        font-style: normal;
        margin: 0 16px 0 20px;
    }

    .box_a span {
        color: orange;
        font-size: 18px;
    }

    input{
        background-color: #4CAF50; /* 绿色 */
        border: none;
        color: white;
        padding: 15px 3px;
        text-align: center;
        text-decoration: none;
        display: inline-block;
        font-size: 16px;
        margin: 4px 2px;
        cursor: pointer;
    }
    .select_style select{
        padding:5px;
        background:transparent;
        width:268px;
        font-size:16px;
        border:none;
        height:30px;
        -webkit-appearance:none; /*for Webkit browsers*/
    }
    .select_style_2 select{
        width: 220px;
        padding: 5px;
        font-size: 16px;
        border-radius: 5px;
        border:1px solid #ccc;
    }
    .qe{
        display: flex;
        flex-direction: row;
        justify-content: space-between;
    }
</style>
<body>
<jsp:include page="../header.jsp"></jsp:include>
<div class="qe" style="top: 50px;position: relative;">
    <%
        List<String> classlist = (List<String>) request.getAttribute("classlist");
    %>
    <div class="select_style_2" >
        <select name="select" style="float: right;margin: 0" id="class_name">
            <option value="qwe" selected>请选择类目</option>
            <%
                if(classlist==null){
                    request.getRequestDispatcher(request.getContextPath()+"?act=show_cake").forward(request, response);
                }else{
                    for(String name:classlist){
            %>
            <option value="<%=name%>"><%=name%></option>
            <%} }%>
        </select>
    </div>
</div>
<div class="box2">
<%
    List<Cake> list = (List<Cake>) request.getAttribute("cakelist");
    if(list == null){
        request.getRequestDispatcher(request.getContextPath()+"?act=show_cake").forward(request, response);
    }else{
        for(Cake cake:list){
%>
    <div class="box wzc-table" style="width: 299px;height: 333px;">
        <form action="/webProject3/CartServlet?act=insert&cakename=<%=cake.getCake_name()%>" method="post" style="width:100%;text-align:center;">
            <img src='<%=cake.getImg()%>'
            style='width: 293px;height: 219px;border-radius: 14px 14px;'/>
<%--            <p><%=cake.getIntroduce()%></p>--%>
<%--            <div class="text"><%=cake.getCake_class()%></div>--%>
            <div class="box_a"><h2><%=cake.getCake_name()%></h2><em>|</em><span><%=cake.getCake_price()%></span></div>
            <input name="sub" type="submit" value="加入购物车" class="wzc-btn" style="width: 153px;border-radius: 9px;font-size: 20px;">
        </form>
    </div>
<%
    }}
%>
</div>
<script type="text/javascript">
<%
    Object resultMSG = request.getSession().getAttribute("resultMSG");
    if(resultMSG!=null){
%>alert("<%=resultMSG.toString() %>")<%
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
            var str1 = "<div class='box wzc-table'  style='width: 299px;height: 333px;'>";
            var str2 = "<input name='sub' type='submit' value='加入购物车' class='wzc-btn' style='width: 153px;border-radius: 9px;font-size: 20px;'></form></div>";
            for(i in msg){
                str += str1+" <form action='/webProject3/CartServlet?act=insert&cakename="+msg[i].cake_name+"'"
                    +"method='post' style='width:100%;text-align:center;'>"+
                    "<img src='"+msg[i].img+"'style='width: 293px;height: 219px;border-radius: 14px 14px;'/>"+
                    "<div class='box_a'><h2>" + msg[i].cake_name+ "</h2><em>|</em><span>" +msg[i].cake_price+
                    "</span></div>"+str2;
                // console.log(msg[i]);
                // console.log(msg[i].img)
            }
            $(".box2").html(str);
        }
    })
})
</script>
</body>
</html>
