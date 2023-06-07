package mvc.servlet;

import mvc.Dao.CakeDao;
import mvc.Dao.CartDao;
import mvc.JavaBean.Cake;
import mvc.JavaBean.Cart;
import mvc.JavaBean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CartServlet",urlPatterns = "/CartServlet")
public class CartServlet extends HttpServlet {

    private CakeDao dao = new CakeDao();
    private CartDao dao1 = new CartDao();

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
                        delete(req,resp);
                        break;
                    case "select":
                        getCart(req,resp);
                        break;
                    case "insert":
                        addCart(req,resp);
                        break;
                    case "add_cake":
                        add_cake(req,resp);
                        break;
                    case "delete_cake":
                        delete_cake(req,resp);
                        break;
                    case "deleteAll":
                        deleteAll(req,resp);
                        break;
                    default:addCart(req,resp);
                        break;
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }else {
            resp.getWriter().print("act参数不能位空，请检查是否传输了该参数！");
        }
    }

    //添加蛋糕到购物车
    private void addCart(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String cakename = request.getParameter("cakename");
        Cake cake = dao.selectByCakename(cakename);
        User user = (User) request.getSession().getAttribute("user");
        if(dao1.addCake(user,cake)){
            request.getSession().setAttribute("resultMSG", "添加成功");
        }else{
            request.getSession().setAttribute("resultMSG", "添加失败");
        }
        response.sendRedirect("/webProject3/CakeServlet?act=show_cake");
    }

    //获得购物车内的所有蛋糕
    private void getCart(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User) request.getSession().getAttribute("user");
        Cart cart = dao1.getAllcart(user);
        request.setAttribute("cart",cart);
        request.getRequestDispatcher("/front/Cart_show.jsp").forward(request,response);
    }

    //购物车进行蛋糕加一
    private void add_cake(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User) request.getSession().getAttribute("user");
        String cake_name = request.getParameter("cakename");
        Cake cake = dao.selectByCakename(cake_name);
        dao1.addCake(user,cake);
        response.sendRedirect("/webProject3/CartServlet?act=select");
    }

    //购物车进行蛋糕减一
    private void delete_cake(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User) request.getSession().getAttribute("user");
        int cake_num = Integer.parseInt(request.getParameter("cakenum"));
        String cake_name = request.getParameter("cakename");
        Cake cake = dao.selectByCakename(cake_name);
        if(cake_num!=1){
            dao1.delete_cake(user,cake);//购物车蛋糕数量减一
        }else{
            dao1.delete_cartitem(user,cake);//购物车蛋糕数量为1时，删除该购物项
        }
        dao1.delete_cake_num(cake,1);//蛋糕总体影响力减一
        response.sendRedirect("/webProject3/CartServlet?act=select");
    }

    //购物车删除该蛋糕
    private void delete(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User) request.getSession().getAttribute("user");
        int cake_num = Integer.parseInt(request.getParameter("cakenum"));
        String cake_name = request.getParameter("cakename");
        Cake cake = dao.selectByCakename(cake_name);
        dao1.delete_cartitem(user,cake);//删除该购物项
        dao1.delete_cake_num(cake,cake_num);//蛋糕里面减少其影响力
        response.sendRedirect("/webProject3/CartServlet?act=select");
    }

    //删除该用户的所有购物车
    private void deleteAll(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User) request.getSession().getAttribute("user");
        dao1.delete_re_All(user);//删除该用户购物车对蛋糕信息的影响力
        dao1.deleteAll(user);//删除该用户购物车所有蛋糕
        response.sendRedirect("/webProject3/CartServlet?act=select");
    }


}
