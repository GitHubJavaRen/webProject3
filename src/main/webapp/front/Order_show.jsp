<%@ page import="mvc.JavaBean.Order" %>
<%@ page import="java.util.List" %>
<%@ page import="mvc.JavaBean.CartItem" %>
<%@ page import="mvc.JavaBean.User" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <link rel="stylesheet" type="text/css" href="http://localhost:8080/webProject3/css/app.css">
    <link rel="stylesheet" type="text/css" href="http://localhost:8080/webProject3/font-awesome-4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="http://localhost:8080/webProject3/css/subpage.css">
</head>
<body>
<jsp:include page="../header.jsp"></jsp:include>
<%
    List<Order> list = (List<Order>) request.getAttribute("orderlist");
    User user = (User) request.getSession().getAttribute("user");
    if(list==null || list.size()==0){
%>
<h1 style="position: relative;top: 50px;width: 100%;
        color: #00a3ff;">您目前没有订单</h1>
<%
    }else {
%>
<div style="position: relative;top: 46px">
    <h1 class="callout" style="color: #3F3D56; border-left: 5px solid #3F3D56;">订单信息</h1>
    <div class="" style="text-align:center;">
        <table class="wzc-table">
            <tr>
                <th>订单编号</th>
                <th>订单详情</th>
                <th>订单总数</th>
                <th>订单总价</th>
                <th>订单时间</th>
                <th>订单状态</th>
            </tr>
                <%
            for(Order order:list){
        %>
            <tr>
                <th><%=order.getId()%></th>
                <th>
                    <%
                        int num = 0;
                        List<CartItem> cartItems = order.getList();
                        for(CartItem item:cartItems){
                            num += item.getCakenum();
                    %>
                    <p><%=item.getCake().getCake_name()%>(<%=item.getCake().getCake_price()%>)*<%=item.getCakenum()%></p>
                    <%
                        }
                    %>
                </th>
                <th><%=num%></th>
                <th><%=order.getPrice()%></th>
                <th><%=order.getTime()%></th>
                <%
                    if(order.getState().equals("已发货")){
                %>
                <th style="color: red"><%=order.getState()%></th>
                <td>
                    <a href="/webProject3/OrderServlet?act=getGoods&id=<%=order.getId()%>">
                        <i class="fa fa-check-circle-o fa-fw"></i>签收</a>
                </td>
                <%
                }else if (order.getState().equals("已付款")){
                %>
                <th><%=order.getState()%></th>
                <td>
                    <a href="/webProject3/OrderServlet?act=delete&id=<%=order.getId()%>">
                        <i class="fa fa-trash fa-fw"></i>取消订单</a>
                </td>
                <%
                    }else{
                %>
                <th style="color: deepskyblue"><%=order.getState()%></th>
                <td>
                    <a href="/webProject3/OrderServlet?act=delete&id=<%=order.getId()%>">
                        <i class="fa fa-trash fa-fw"></i>取消订单</a>
                </td>
                <%
                    }
                %>
            </tr>
                <%
 }}
%>
</div>
</div>

</body>
</html>
