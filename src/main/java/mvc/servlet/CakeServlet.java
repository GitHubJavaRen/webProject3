package mvc.servlet;

import mvc.Dao.CakeDao;
import mvc.Dao.ClassDao;
import mvc.JavaBean.Cake;
import mvc.JavaBean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(name = "CakeServlet",urlPatterns = "/CakeServlet")
public class CakeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CakeDao dao = new CakeDao();
    private ClassDao dao1 = new ClassDao();

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
                    case "toUpdatePage":
                        toUpdatePage(req,resp);
                        break;
                    case "select":
                        getAllCake(req,resp);
                        break;
                    case "insert":
                        insertCake(req,resp);
                        break;
                    case "update":
                        updateCake(req,resp);
                        break;
                    case "delete":
                        deleteCake(req,resp);
                        break;
                    case "pop":
                        popCake(req,resp);
                        break;
                    case "insert_cake":
                        cake_insert(req,resp);
                        break;
                    case "show_cake":
                        show_cake(req,resp);
                        break;
                    case "sh"://展示限定热门信息
                        show_re_cake(req,resp);
                        break;
                    case "souce":
                        souce(req,resp);//模糊查询
                        break;
                    case "th":
                        th(req,resp);
                        break;//最新产品信息
                    case "rexiao":
                        rexiao(req,resp);
                        break;//获取最热产品，并显示到首页
                    default:getAllCake(req,resp);
                        break;
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }else {
            resp.getWriter().print("act参数不能位空，请检查是否传输了该参数！");
        }
    }

    //蛋糕数据的显示
    private void getAllCake(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Cake> list = dao.getAllCake();
        request.setAttribute("cakelist",list);
        request.getRequestDispatcher("/back/CakeDisplay.jsp").forward(request,response);
    }

    private void rexiao(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Cake cake = dao.get_rd_dy();//获得最热门的的蛋糕
        request.setAttribute("cake",cake);//把最热门的蛋糕存入到request
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }

    //蛋糕数据的删除
    private void deleteCake(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String name = request.getParameter("cake_name");
        if(dao.delete(name)){
            request.getSession().setAttribute("resultMSG", "删除成功");
        }else {
            request.getSession().setAttribute("resultMSG", "删除失败");
        }
        response.sendRedirect("/webProject3/CakeServlet?act=select");
    }

    private void insertCake(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String cake_name = request.getParameter("cake_name");
        String cake_class = request.getParameter("select");
        int cake_price = Integer.parseInt(request.getParameter("cake_price"));
        int cake_num = Integer.parseInt(request.getParameter("cake_num"));//管理员可设置热度，也可初始化热度为0
        String cake_introduce = request.getParameter("cake_introduce");
        String cake_img = request.getParameter("cake_img");
        Cake cake = new Cake();
        if(cake_name!=null){cake.setCake_name(cake_name);}
        if(cake_class!=null){cake.setCake_class(cake_class);}
        if(cake_introduce!=null){cake.setIntroduce(cake_introduce);}
        cake.setCake_price(cake_price);
        cake.setCake_num(cake_num);
        cake.setImg(cake_img);//设置蛋糕图片链接
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        cake.setCake_time(date);
        if(dao.insert(cake)){
            request.getSession().setAttribute("resultMSG", "新增成功");
            response.sendRedirect("/webProject3/CakeServlet?act=select");
        }else{
            request.getSession().setAttribute("resultMSG","新增失败");
            request.getRequestDispatcher("/back/insert_cake.jsp").forward(request, response);//转发
        }
    }

    //用来回响蛋糕信息的数据给修改界面
    private void toUpdatePage(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //用来回响，让修改界面能够获得cake这个对象
        String cakename = request.getParameter("cake_name");
        Cake cake = dao.selectByCakename(cakename);
        request.setAttribute("updateCake", cake);
        request.getRequestDispatcher("/back/update_cake.jsp").forward(request, response);
    }

    //回响蛋糕数据给前台界面
    private void show_cake(HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<Cake> list = dao.getAllCake();
        List<String> list1 = dao1.getAllCakeclass();
        request.setAttribute("cakelist",list);
        request.setAttribute("classlist",list1);
        request.getRequestDispatcher("/front/Show_cake.jsp").forward(request,response);
    }

    //回响热门蛋糕数据给前台界面
    private void show_re_cake(HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<Cake> list = dao.getRecake();
        List<String> list1 = dao1.getAllCakeclass();
        request.setAttribute("cakelist",list);
        request.setAttribute("classlist",list1);
        request.getRequestDispatcher("/front/Show_cake.jsp").forward(request,response);
    }

    //蛋糕界面数据的修改  提交到数据库
    private void updateCake(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String cake_name = request.getParameter("cake_name");
        String cake_class = request.getParameter("select");
        int cake_price = Integer.parseInt(request.getParameter("cake_price"));
        int cake_num = Integer.parseInt(request.getParameter("cake_num"));//可以让管理员来修改最热门菜单
        String cake_introduce = request.getParameter("cake_introduce");
        String cake_img = request.getParameter("cake_img");
        Cake cake = new Cake();
        if(cake_name!=null){cake.setCake_name(cake_name);}
        if(cake_class!=null){cake.setCake_class(cake_class);}
        if(cake_introduce!=null){cake.setIntroduce(cake_introduce);}
        cake.setCake_price(cake_price);
        cake.setCake_num(cake_num);
        cake.setImg(cake_img);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        cake.setCake_time(date);
        if(dao.update(cake)){
            request.getSession().setAttribute("resultMSG", "修改成功");
            response.sendRedirect("/webProject3/CakeServlet?act=select");
        }else {
            request.setAttribute("updateCake", cake);
            request.getSession().setAttribute("resultMSG","修改失败");
            request.getRequestDispatcher("/back/update_cake.jsp").forward(request, response);//转发
            //重定向是 基于8080往后走的url
            //转发是在 自身的webProject上面往后走
        }
    }

    //进入蛋糕分类目展示页面
    private void popCake(HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<Cake> list = dao.getAllCake();
        List<String> list1 = dao1.getAllCakeclass();
        request.setAttribute("cakelist",list);
        request.setAttribute("classlist",list1);
        request.getRequestDispatcher("/back/CakeDisplay_2.jsp").forward(request,response);
    }

    //进入蛋糕增加页面，传入蛋糕类目列表的参数
    private void cake_insert(HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<String> list1 = dao1.getAllCakeclass();
        request.setAttribute("classlist",list1);
        request.getRequestDispatcher("/back/insert_cake.jsp").forward(request,response);
    }

    //模糊查询
    private void souce(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String cakename = request.getParameter("cakename");
        List<Cake> list = dao.souce(cakename);
        List<String> list1 = dao1.getAllCakeclass();
        request.setAttribute("cakelist",list);
        request.setAttribute("classlist",list1);
        request.getRequestDispatcher("/front/Show_cake.jsp").forward(request,response);
    }

    //最新产品信息
    private void th(HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<Cake> list = dao.th();
        List<String> list1 = dao1.getAllCakeclass();
        request.setAttribute("cakelist",list);
        request.setAttribute("classlist",list1);
        request.getRequestDispatcher("/front/Show_cake.jsp").forward(request,response);
    }
}
