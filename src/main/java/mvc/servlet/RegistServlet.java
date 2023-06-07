package mvc.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.Dao.UserDao;
import mvc.JavaBean.User;

/**
 * @description 注册请求处理类
 */
@WebServlet(name = "RegistServlet",urlPatterns = "/RegistServlet")
public class RegistServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String rpsw = request.getParameter("rpsw");
		if(username==null||username.trim().isEmpty()){
			request.setAttribute("registError", "用户名不能为空");
			request.getRequestDispatcher("/front/regist.jsp").forward(request, response);
			return;
		}
		if(password==null||password.trim().isEmpty()){
			request.setAttribute("registError", "密码不能为空");
			request.getRequestDispatcher("/front/regist.jsp").forward(request, response);
			return;
		}
		if(!password.equals(rpsw)){
			request.setAttribute("registError", "密码不一致");
			request.getRequestDispatcher("/front/regist.jsp").forward(request, response);
			return;
		}
		UserDao dao = new UserDao();
		User user =new User();
		user.setUsername(username);
		user.setPassword(password);
		try {
			if(dao.selectByUsername(username)!=null){
				request.setAttribute("registError", "注册失败，该用户名已存在");
				request.getRequestDispatcher("/front/regist.jsp").forward(request, response);
			}else {
				if(dao.insert(user)){
					response.sendRedirect("/webProject3/index.jsp");
				}else {
					request.setAttribute("registError", "注册失败，发生未知错误");
					request.getRequestDispatcher("/front/regist.jsp").forward(request, response);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}
