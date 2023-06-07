package mvc.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mvc.JavaBean.User;

public class UserDao {

	Connection conn = null;


//	新增用户
	public boolean insert(User user) throws Exception {
		if(!isExist(user.getUsername())){
			conn = JDBCUtils.getInstance().getConnection();
			PreparedStatement pstmt =null;
			String sql = "INSERT INTO p_user(p_name,p_pwd,p_admin) VALUES(?,?,?)";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,user.getUsername());
				pstmt.setString(2,user.getPassword());
				pstmt.setInt(3,0);
				return pstmt.executeUpdate()>0;
			}catch (SQLException e) {
				e.printStackTrace();
			}finally {
				JDBCUtils.close(conn,pstmt);
			}
		}else {
			System.err.println("该用户已存在");
		}
		return false;
	}

//	判断是否有重复用户名
	public boolean isExist(String userName) throws Exception {
		conn = JDBCUtils.getInstance().getConnection();
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		String sql = "select * from p_user where p_name like ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1,userName);
		rs = pstmt.executeQuery();
		return rs.next();
	}

	//删除用户
	public boolean delete(String name) throws Exception {
		conn = JDBCUtils.getInstance().getConnection();
		PreparedStatement pstmt =null;
		String sql = "DELETE from p_user where p_name=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,name);
			return pstmt.executeUpdate()>0;
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(conn,pstmt);
		}
		return false;
	}


//  修改该用户名的密码
	public boolean update(User user) throws Exception {
		conn = JDBCUtils.getInstance().getConnection();
		PreparedStatement pstmt =null;
		String sql = "update p_user set p_pwd=? where p_name=?";
		boolean res = false;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(2,user.getUsername());
			pstmt.setString(1,user.getPassword());
			res = (pstmt.executeUpdate()==1);
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			JDBCUtils.close(conn,pstmt);
		}
		return res;
	}


//	查询是否存在该用户并返回
	public User selectByUsername(String username) throws Exception {
		conn = JDBCUtils.getInstance().getConnection();
		List<User> list = new ArrayList<>();
		String sql = "select * from p_user where p_name=?";
		PreparedStatement pstmt =null;
		ResultSet rs;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			list=resultSetToBean(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
            JDBCUtils.close(conn,pstmt);
        }
		return list.isEmpty()?null:list.get(0);
	}

	//获得所有的普通用户  超级管理员两者之间不能互知
	public List<User> selectByUser() throws Exception {
		conn = JDBCUtils.getInstance().getConnection();
		List<User> list = new ArrayList<>();
		String sql = "select * from p_user where p_admin='0'";
		PreparedStatement pstmt =null;
		ResultSet rs;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			list=resultSetToBean(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(conn,pstmt);
		}
		return list;
	}

    //将查询到的用户名添加到list列表中,并返回list
	private static List<User> resultSetToBean(ResultSet rs) throws SQLException {
		List<User> list = new ArrayList<>();
		while (rs.next()){
			User user = new User();
			user.setUsername(rs.getString("p_name"));
			user.setPassword(rs.getString("p_pwd"));
			user.setPossion(rs.getInt("p_admin"));
			list.add(user);
		}
		return list;
	}

}
