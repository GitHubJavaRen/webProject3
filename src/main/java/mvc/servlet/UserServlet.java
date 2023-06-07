package mvc.servlet;

import com.sun.org.apache.xpath.internal.operations.Or;
import mvc.Dao.CartDao;
import mvc.Dao.OrderDao;
import mvc.Dao.UserDao;
import mvc.JavaBean.Order;
import mvc.JavaBean.User;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet(name = "UserServlet",urlPatterns = "/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private UserDao dao = new UserDao();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		String act = request.getParameter("act");
		if(act!=null){
			//根据传输的act调用不同的方法进行处理
			try {
				switch (act) {
					case "toUpdatePage":
						toUpdatePage(request,response);
						break;
					case "select":
						selectAllUsers(request,response);
						break;
					case "insert":
						insertUser(request,response);
						break;
					case "update":
						updateUser(request, response);
						break;
					case "delete":
						deleteUser(request, response);
						break;
					default:selectAllUsers(request,response);
						break;
				}
			} catch (Exception e){
				e.printStackTrace();
			}

		}else {
			response.getWriter().print("act参数不能位空，请检查是否传输了该参数！");
		}

	}

	private void toUpdatePage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//用来回响，让修改界面能够获得user这个对象
		String username = request.getParameter("username");
		User user = dao.selectByUsername(username);
		request.setAttribute("updateUser", user);
		request.getRequestDispatcher("/back/update.jsp").forward(request, response);
	}

	private void selectAllUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<User> list = dao.selectByUser();
		request.setAttribute("userList", list);
		request.getRequestDispatcher("/back/query.jsp").forward(request, response);
	}

	private void insertUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = new User();
		if(username!=null){
			user.setUsername(username);
		}
		if(password!=null){
			user.setPassword(password);
		}
		if(dao.insert(user)){
			request.getSession().setAttribute("resultMSG", "新增成功");
			response.sendRedirect("/webProject3/UserServlet?act=select");
		}else {
			request.getSession().setAttribute("resultMSG", "新增失败");
			request.getRequestDispatcher("/back/insert.jsp").forward(request, response);
		}
	}

	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String name =request.getParameter("username");
		//删除名字的同时，还应该删除该用户的所有信息
		//包括购物车以及订单信息
		User user = dao.selectByUsername(name);//获得该用户
		new CartDao().delete_re_All(user);//删除用户购物车影响力
		new CartDao().deleteAll(user);//删除用户购物车
		OrderDao dao1 = new OrderDao();
		List<Order> list = dao1.getOrder(user);//获得用户所有订单
		for(Order order:list){
			dao1.deleteOrder(user,order);//删除用户所有订单，内部封装了删除订单影响力的方法
		}
		if(dao.delete(name)){
			request.getSession().setAttribute("resultMSG", "删除成功");
		}else {
			request.getSession().setAttribute("resultMSG", "删除失败");
		}
		response.sendRedirect("/webProject3/UserServlet?act=select");
	}

	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = new User();
		if(username!=null){
			user.setUsername(username);
		}
		if(password!=null){
			user.setPassword(password);
		}
		if(dao.update(user)){
			request.getSession().setAttribute("resultMSG", "修改成功");
			response.sendRedirect("/webProject3/UserServlet?act=select");
		}else {
			request.setAttribute("updateUser", user);
			request.getSession().setAttribute("resultMSG","修改失败");
			request.getRequestDispatcher("/back/update.jsp").forward(request, response);//转发
			//重定向是 基于8080往后走的url
			//转发是在 自身的webProject上面往后走
		}
	}
}
