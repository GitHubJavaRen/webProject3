<%@ page import="mvc.JavaBean.Order" %>
<%@ page import="java.util.List" %>
<%@ page import="mvc.JavaBean.CartItem" %>
<%@ page import="mvc.JavaBean.User" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Iterator" %>

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
<%

    Map<String ,List<Order>> map = (Map<String, List<Order>>) request.getAttribute("map");
    User user = (User) request.getSession().getAttribute("user");
    if(map.isEmpty()){
%>
<h1 style="position: relative;top: 50px;width: 100%;
        color: #00a3ff;">您目前没有订单</h1>
<%
}else {
%>
<h1 class="callout">订单信息</h1>
<div class="" style="text-align:center;">
    <table class="wzc-table">
        <tr>
            <th>订单人</th>
            <th>订单编号</th>
            <th>订单详情</th>
            <th>订单总数</th>
            <th>订单总价</th>
            <th>订单时间</th>
            <th>订单状态</th>
        </tr>
            <%
            Set set = map.keySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()){
                String username = (String) iterator.next();
                List<Order> orderList = map.get(username);
                for(Order order:orderList){
                    List<CartItem> itemlist = order.getList();
            %>
            <tr>
                <th><%=order.getUsername()%></th>
                <th><%=order.getId()%></th>
                <th>
                <%
                    int num = 0;
                    for(CartItem item:itemlist){
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
                    if(order.getState().equals("已签收")){
                %>
                <th style="color: deepskyblue"><%=order.getState()%></th>
                <td>
                    <a href="/webProject3/OrderServlet?act=delete_ht&id=<%=order.getId()%>&username=<%=order.getUsername()%>">
                        <i class="fa fa-trash fa-fw"></i>删除</a>
                </td>
                <%
                    }else if(order.getState().equals("已发货")){
                %>
                <th style="color: red"><%=order.getState()%></th>
                <td>
                    <a href="/webProject3/OrderServlet?act=delete_ht&id=<%=order.getId()%>&username=<%=order.getUsername()%>">
                        <i class="fa fa-trash fa-fw"></i>删除</a>
                </td>
                <%
                }else {
                %>
                <th><%=order.getState()%></th>
                <td>
                    <a href="/webProject3/OrderServlet?act=setState&time=<%=order.getTime()%>&username=<%=order.getUsername()%>">
                        <i class="fa fa-trash fa-handshake-o"></i>发货</a>
                </td>
                <%
                    }
                %>
            </tr>
            <%
                }}}
%>


</body>
</html>
