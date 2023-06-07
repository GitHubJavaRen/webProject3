package mvc.Dao;

import mvc.JavaBean.Cake;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClassDao {
    Connection conn = null;

    //新增类别
    public boolean insert(String class_name) throws Exception{
        if(!class_name.trim().equals("")){
            conn = JDBCUtils.getInstance().getConnection();
            PreparedStatement pstmt = null;
            String sql = "insert into c_class values(?)";
            try {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1,class_name);
                return pstmt.executeUpdate()>0;
            }catch (SQLException e) {
                e.printStackTrace();
            }finally {
                JDBCUtils.close(conn,pstmt);
            }
        }else {
            System.err.println("该类别已存在");
        }
        return false;
    }

    //删除类别
    public boolean delete(String name) throws Exception{
        deleteCake(name);//删除对应的类别中的蛋糕
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt = null;
        String sql = "delete from c_class where c_name=?";
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

    //删除类别，同时也应该删除对应类别内的所有蛋糕
    public boolean deleteCake(String name) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt = null;
        String sql = "delete from cake where cake_class=?";
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

    //修改类别名字
    public boolean update(String name,String name1) throws Exception{
        updateCake(name,name1);//修改蛋糕的类别名字
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt = null;
        String sql = "update c_class set c_name = ? where c_name = ?";
        boolean res = false;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(2,name);//旧的名字
            pstmt.setString(1,name1);//新的名字
            res = (pstmt.executeUpdate()==1);
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
        return res;
    }

    //修改类别名字的同时，蛋糕类别的名字也需要修改
    public boolean updateCake(String name,String name1) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt = null;
        String sql = "update cake set cake_class = ? where cake_class = ?";
        boolean res = false;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(2,name);//旧的名字
            pstmt.setString(1,name1);//新的名字
            res = (pstmt.executeUpdate()==1);
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
        return res;
    }

    public List<Cake> selectByCakeclass(String name) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        List<Cake> list = new ArrayList<>();
        String sql = "select * from cake where cake_class = ? order by cake_time desc";
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

    //获取所有蛋糕的列表
    public List<Cake> getAllCake() throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        List<Cake> list = new ArrayList<>();
        String sql = "select * from cake order by cake_time desc";
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

    //获取部分蛋糕的列表
    public List<Cake> getClassCake(String name) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        List<Cake> list = new ArrayList<>();
        String sql = "select * from cake where cake_class = ? order by cake_time desc";
        PreparedStatement pstmt =null;
        ResultSet rs;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,name);
            rs = pstmt.executeQuery();
            list=resultSetToBean(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
        return list;
    }

    public List<String> getAllCakeclass() throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        List<String> list = new ArrayList<>();
        String sql = "select * from c_class";
        PreparedStatement pstmt =null;
        ResultSet rs;
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                String name = rs.getString("c_name");
                list.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
        return list;
    }

    private static List<Cake> resultSetToBean(ResultSet rs) throws SQLException {
        List<Cake> list = new ArrayList<>();
        while (rs.next()){
            Cake cake = new Cake();
            cake.setCake_name(rs.getString("cake_name"));
            cake.setCake_class(rs.getString("cake_class"));
            cake.setCake_price(rs.getInt("cake_price"));
            cake.setCake_num(rs.getInt("cake_num"));
            cake.setCake_time(rs.getString("cake_time"));
            cake.setImg(rs.getString("cake_img"));
            cake.setIntroduce(rs.getString("cake_introduce"));
            list.add(cake);
        }
        return list;
    }
}
