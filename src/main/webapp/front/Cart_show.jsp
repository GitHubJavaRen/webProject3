<%@ page import="mvc.JavaBean.Cart" %>
<%@ page import="mvc.JavaBean.CartItem" %>
<%@ page import="java.util.List" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<style>
    body{
        overflow: hidden;
        text-align: center;

    }
    .car{
        width: 80%;
        border: 5px solid #3F3D56;
        border-radius: 18px;
        margin-left: 10%;
        position: relative;
        top: 60px;
    }
    .car .good{
        background-color:  #3F3D56;
        height:55px;
        font-size: 22px;
        color:white;
        line-height: 55px;
        font-weight: 200;
        border-radius: 18px 18px 0 0;
        margin-bottom: 20px;

    }
    .car .good table{
        width:100%;
    }
    .car .goods{
        height:45px;
        line-height: 45px;
        font-size: 20px;
        font-weight: 200;
    }
    .car .goods table{
        width:100%;
    }
    .car .goods table .btn1{
        width: 60px;
        height:28px;
        border: 2px solid #46B3E6;
        background-color: white;
        color: #46B3E6;
        border-radius: 4px;
        font-weight: 600;
    }
    .car .goods table button:hover{
        background-color: #46B3E6;
        color: white;
    }
    .car .goods1{
        margin-top: 10px;
        height:50px;
        font-size: 19px;
        color:white;
        line-height: 50px;
        font-weight: 200;
        border-radius:0 0 10px 10px ;
    }
    .car .goods2{
        border-top: 1px solid  #3F3D56;
        margin-top: 10px;
        height:50px;
        font-size: 19px;
        line-height: 50px;
        font-weight: 200;
        border-radius:0 0 10px 10px ;
    }
    .car .goods1 table{

        width:100%;
    }
    .car .goods1 .btn2{
        width: 70px;
        height:28px;
        border: 2px solid #21BF73;
        background-color: white;
        color: #21BF73;
        border-radius: 4px;
        font-weight: 600;
    }
    .car .goods1 .btn2:hover{
        color:white;
        background-color: #21BF73;
    }
    .car .goods1 .btn3{
        width: 80px;
        height:30px;
        border: 2px solid #FF0000;
        background-color: white;
        color: #FF0000;
        border-radius: 4px;
        font-weight: 600;
    }
    .car .goods1 .btn3:hover{
        color:white;
        background-color:#FF0000 ;
    }

</style>
<body>
<jsp:include page="../header.jsp"></jsp:include>
<%
    Cart cart = (Cart) request.getAttribute("cart");
    if(!cart.isList()){
%>
<h1 style="position: relative;top: 50px;width: 100%;
    color: #00a3ff;
">购物车内无东西，请前往商城首页进行购买</h1>
<%
    }else{
        int num = 0;
        List<CartItem> list = cart.getList();
%>
<div class="car">
    <div class="good">
        <table><tr style="color: white;"><td width=20% style="padding: 15px">商品名称</td><td width=20%>单价</td><td width=20%>数量</td><td width=20%>总价</td><td width=30%>操作</td></tr></table>
    </div>
    <%
        for(CartItem item:list){
            num += item.getCakenum();
        %>
    <div class="goods">
        <table><tr style="padding: 15px"><td width=20%><%=item.getCake().getCake_name()%></td><td width=20%><%=item.getCake().getCake_price()%></td>
            <td width=20%><%=item.getCakenum()%></td><td width=20%><%=item.getPrice()%></td><td width=30%>
                <a><button class="btn1"
  onclick="window.location.href='http://localhost:8080/webProject3/CartServlet?act=delete_cake&cakename=<%=item.getCake().getCake_name()%>&cakenum=<%=item.getCakenum()%>'">-1</button><button class="btn1"
  onclick="window.location.href='http://localhost:8080/webProject3/CartServlet?act=delete&cakename=<%=item.getCake().getCake_name()%>&cakenum=<%=item.getCakenum()%>'">删除</button><button class="btn1"
  onclick="window.location.href='http://localhost:8080/webProject3/CartServlet?act=add_cake&cakename=<%=item.getCake().getCake_name()%>'">+1</button>
            </a></td></tr></table>
    </div>
    <%
        }
        %>
    <div class="goods2">
        <table><tr style="padding: 15px"><td width=560></td><td width="20%">小计：<%=num%></td><td width=20%>总计金额：<%=cart.getPrice()%></td></tr></table>
    </div>
    <div class="goods1">
        <table><tr style="padding: 15px"><td width=50%><button class="btn2"
     onclick="window.location.href='http://localhost:8080/webProject3/OrderServlet?act=insert'">确认购买</button></td><td><button class="btn3"
     onclick="window.location.href='http://localhost:8080/webProject3/CartServlet?act=deleteAll'">全部清空</button></td></tr></table>
    </div>
</div>
<%
    }
%>
</body>
</html>
