package mvc.Dao;

import mvc.JavaBean.Cake;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CakeDao {
    Connection conn = null;

    //新增蛋糕
    public boolean insert(Cake cake) throws Exception{
        if(!isExist(cake.getCake_name())){
            conn = JDBCUtils.getInstance().getConnection();
            PreparedStatement pstmt = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
            String sql = "INSERT INTO cake(cake_name,cake_class,cake_price,cake_num,cake_time,cake_introduce,cake_img) VALUES(?,?,?,?,?,?,?)";
            try{
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, cake.getCake_name());
                pstmt.setString(2, cake.getCake_class());
                pstmt.setInt(3,cake.getCake_price());
                pstmt.setInt(4,cake.getCake_num());
                pstmt.setString(5,date);
                pstmt.setString(6,cake.getIntroduce());
                pstmt.setString(7,cake.getImg());
                return pstmt.executeUpdate()>0;
            } finally {
                JDBCUtils.close(conn,pstmt);
            }
        }else {
            System.err.println("该蛋糕已存在");
        }
        return false;
    }

    //判断是否有重复蛋糕名
    public boolean isExist(String name) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select * from cake where cake_name = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,name);
        rs = pstmt.executeQuery();
        return rs.next();
    }

    //	查询是否存在该蛋糕并返回
    public Cake selectByCakename(String cakename) throws Exception {
        conn = JDBCUtils.getInstance().getConnection();
        List<Cake> list = new ArrayList<>();
        String sql = "select * from cake where cake_name=?";
        PreparedStatement pstmt =null;
        ResultSet rs;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cakename);
            rs = pstmt.executeQuery();
            list=resultSetToBean(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
        return list.isEmpty()?null:list.get(0);
    }

    //删除蛋糕
    public boolean delete(String name) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt =null;
        String sql = "DELETE from cake where cake_name=?";
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

    //修改蛋糕
    public boolean update(Cake cake) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        String sql = "update cake set cake_class = ? , cake_price = ? , cake_time = ? , cake_introduce = ? , cake_num = ? ,cake_img = ? where cake_name = ?";
        boolean res = false;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,cake.getCake_class());
            pstmt.setString(3,date);
            pstmt.setInt(2,cake.getCake_price());
            pstmt.setString(4,cake.getIntroduce());
            pstmt.setInt(5,cake.getCake_num());
            pstmt.setString(6,cake.getImg());
            pstmt.setString(7,cake.getCake_name());
            res = (pstmt.executeUpdate()==1);
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
        return res;
    }

    //获得所有的蛋糕
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

    //获得3个热门蛋糕
    public List<Cake> getRecake() throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        List<Cake> list = new ArrayList<>();
        String sql = "select * from cake ORDER BY cake_num desc limit 3;";
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

    //获得三个最新产品蛋糕
    public List<Cake> th() throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        List<Cake> list = new ArrayList<>();
        String sql = "select * from cake ORDER BY cake_time desc limit 3;";
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

    //模糊查询
    public List<Cake> souce(String name) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        List<Cake> list = new ArrayList<>();
        String sql = "select * from cake where cake_name like ?;";
        PreparedStatement pstmt =null;
        ResultSet rs;
        String str = "%"+name+"%";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,str);
            rs = pstmt.executeQuery();
            list=resultSetToBean(rs);//内置封装函数，获取List《Cake》列表
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
        return list;
    }


    //将查询到的所有蛋糕添加到list列表中,并返回list
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


    //获取第一热度的蛋糕，显示在公告栏中
    public Cake get_rd_dy() throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        List<Cake> list = new ArrayList<>();
        String sql = "select * from cake order by cake_num desc limit 1";
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
        return list.isEmpty()?null:list.get(0);
    }
}
