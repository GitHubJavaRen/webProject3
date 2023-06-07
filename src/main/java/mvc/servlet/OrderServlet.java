package mvc.servlet;

import mvc.Dao.OrderDao;
import mvc.Dao.UserDao;
import mvc.JavaBean.Order;
import mvc.JavaBean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "OrderServlet",urlPatterns = "/OrderServlet")
public class OrderServlet extends HttpServlet {

    private OrderDao dao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        String act = req.getParameter("act");
        if(act!=null){
            //根据传输的act调用不同的方法进行处理
            try {
                switch (act) {
                    case "delete":
                        deleteAllOrder(req,resp);
                        break;
                    case "select":
                        getAllOrder(req,resp);//去前台订单界面
                        break;
                    case "insert":
                        setOrder(req,resp);
                        break;
                    case "getAll":
                         getAll(req,resp);//去后台订单界面
                         break;
                    case "setState":
                        setState(req,resp);//设置已发货状态
                        break;
                    case "getGoods":
                        getGoods(req,resp);//设置为签收状态
                        break;
                    case "delete_ht":
                        delete_ht(req,resp);
                        break;
                    default:getAllOrder(req,resp);
                        break;
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }else {
            resp.getWriter().print("act参数不能位空，请检查是否传输了该参数！");
        }
    }


    //获取该用户所有的订单
    private void getAllOrder(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User) request.getSession().getAttribute("user");
        List<Order> list = dao.getOrder(user);
        request.setAttribute("orderlist",list);
        request.getRequestDispatcher("/front/Order_show.jsp").forward(request,response);
    }

    //获取所有订单
    private void getAll(HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<User> userlist = new UserDao().selectByUser();//获得所有普通用户
        Map<String ,List<Order>> ordermap = new LinkedHashMap<>();//人名，人名的订单
        for(User user:userlist){
            List<Order> list1 = new OrderDao().getOrder(user);
            if(list1!=null && list1.size()!=0){
                ordermap.put(user.getUsername(), list1);
            }
        }
        request.setAttribute("map",ordermap);
        request.getRequestDispatcher("/back/Show_order.jsp").forward(request,response);
    }

    //删除指定的订单
    private void deleteAllOrder(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User) request.getSession().getAttribute("user");
        String id = request.getParameter("id");
        Order order = dao.getOrder_re(user,id);//获取该订单信息，里面包括了详细订单信息List<CartItem>
        dao.deleteOrder(user,order);//删除该订单
        response.sendRedirect("/webProject3/OrderServlet?act=select");
    }

    //删除指定的订单（后台）
    private void delete_ht(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String name = request.getParameter("username");
        String id = request.getParameter("id");
        //获取用户信息
        User user = new UserDao().selectByUsername(name);
        Order order = dao.getOrder_re(user,id);//获取该订单信息，里面包括了详细订单信息List<CartItem>
        dao.deleteOrder(user,order);//删除该订单
        response.sendRedirect("/webProject3/OrderServlet?act=getAll");
    }

    //添加指定的订单并清除购物车
    private void setOrder(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User) request.getSession().getAttribute("user");
        dao.add_order(user);
        response.sendRedirect("/webProject3/CartServlet?act=select");
    }

    private void setState(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String name = request.getParameter("username");
        String time = request.getParameter("time");
        dao.setState(name,time);//修改状态
        response.sendRedirect("/webProject3/OrderServlet?act=getAll");
    }

    private void getGoods(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = request.getParameter("id");
        dao.setState(id);
        response.sendRedirect("/webProject3/OrderServlet?act=select");
    }
}
