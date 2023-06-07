package mvc.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.Dao.CakeDao;
import mvc.Dao.UserDao;
import mvc.JavaBean.Cake;
import mvc.JavaBean.User;

/**
 * 登录请求处理类
 */
@WebServlet(name = "LoginServlet",urlPatterns = "/LoginServlet")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		//接收表单信息
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		//设置回显
		request.setAttribute("username", username);
		request.setAttribute("password", password);
		//根据用户名查询用户
		User user = null;
		try {
			user = new UserDao().selectByUsername(username);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if(user!=null){
			if(user.getPassword().equals(password)){
				if(user.getPossion()==1){//判断为超级管理员  且管理员优先进入后台系统
					request.getSession().setAttribute("user", user);//将USER存入session中
					response.sendRedirect("/webProject3/back/index.jsp");//重定向
				}else if(user.getPossion()==0){//判断为普通用户
					request.getSession().setAttribute("user",user);
					try {
						//调用cakeDao获得热度第一的蛋糕
						Cake cake = new CakeDao().get_rd_dy();
						//将蛋糕存入request中
						request.setAttribute("cake",cake);
					} catch (Exception e) {
						e.printStackTrace();
					}
					//转发
					request.getRequestDispatcher("/index.jsp").forward(request,response);
				}
			}else {
				request.setAttribute("loginError", "* 密码错误");
				request.getRequestDispatcher("/front/login1.jsp").forward(request, response);
			}
		}else {
			request.setAttribute("loginError", "* 用户不存在");
			request.getRequestDispatcher("/front/login1.jsp").forward(request, response);
		}

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
