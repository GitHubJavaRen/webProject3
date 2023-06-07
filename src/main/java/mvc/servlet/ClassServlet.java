package mvc.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import mvc.Dao.CakeDao;
import mvc.Dao.ClassDao;
import mvc.JavaBean.Cake;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ClassServlet",urlPatterns = "/ClassServlet")
public class ClassServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    ClassDao dao = new ClassDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        doGet(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        String act = req.getParameter("act");
        if(act!=null){
            //根据传输的act调用不同的方法进行处理
            try {
                switch (act) {
                    case "select":
                        getCake(req,resp);
                        break;
                    case "insert":
                        insertCakeclass(req,resp);
                        break;
                    case "update":
                        updateCakeclass(req,resp);
                        break;
                    case "delete":
                        deleteCakeclass(req,resp);
                        break;
                    case "update_para":
                        update_para(req,resp);
                        break;
                    case "delete_para":
                        delete_para(req,resp);
                        break;
                    default:getCake(req,resp);
                        break;
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }else {
            resp.getWriter().print("act参数不能位空，请检查是否传输了该参数！");
        }
    }

    private void getCake(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String name = request.getParameter("select");
        List<Cake> list = null;
        if(name.equals("qwe")){
           list  = dao.getAllCake();//获取所有蛋糕组
        }else{
            list = dao.getClassCake(name);//获取部分蛋糕组
        }
        //转为json数组
        JSONArray json = (JSONArray) JSONObject.toJSON(list);
        //响应给浏览器
        PrintWriter pw =response.getWriter();
        pw.print(json);
        System.out.println(json);
    }

    private void deleteCakeclass(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String name = request.getParameter("select");
        if(dao.delete(name)){
            request.getSession().setAttribute("resultMSG", "删除成功");
        }else {
            request.getSession().setAttribute("resultMSG", "删除失败");
        }
        response.sendRedirect("/webProject3/CakeServlet?act=pop");
    }

    private void updateCakeclass(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String name = request.getParameter("select");//旧名字
        String name1 = request.getParameter("classname");//新名字
        if(dao.update(name,name1)){
            request.getSession().setAttribute("resultMSG", "修改成功");
            response.sendRedirect("/webProject3/CakeServlet?act=pop");
        }else {
            request.getSession().setAttribute("resultMSG","修改失败");
            request.getRequestDispatcher("/back/update_cakeclass.jsp").forward(request, response);//转发
        }
    }

    private void insertCakeclass(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String classname = request.getParameter("classname");
        if(dao.insert(classname)){
            request.getSession().setAttribute("resultMSG", "新增成功");
            response.sendRedirect("/webProject3/CakeServlet?act=pop");//获取蛋糕列表参数
        }else{
            request.getSession().setAttribute("resultMSG", "新增失败");
            request.getRequestDispatcher("/back/insert_cakeClass.jsp").forward(request, response);//回响
        }
    }

    private void update_para(HttpServletRequest request,HttpServletResponse response) throws Exception{
        List<String> list = dao.getAllCakeclass();
        request.setAttribute("classlist",list);
        request.getRequestDispatcher("/back/update_cakeclass.jsp").forward(request,response);
    }

    private void delete_para(HttpServletRequest request,HttpServletResponse response) throws Exception{
        List<String> list = dao.getAllCakeclass();
        request.setAttribute("classlist",list);
        request.getRequestDispatcher("/back/delete_cakeclass.jsp").forward(request,response);
    }
}
