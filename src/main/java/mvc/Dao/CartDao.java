package mvc.Dao;

import mvc.JavaBean.Cake;
import mvc.JavaBean.Cart;
import mvc.JavaBean.CartItem;
import mvc.JavaBean.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDao {
    Connection conn = null;

    //判断是否存在该购物项
    public boolean isExistCake(User user,Cake cake) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select * from cart where cake_name = ? and cart_name = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,cake.getCake_name());
        pstmt.setString(2, user.getUsername());
        rs = pstmt.executeQuery();
        return rs.next();
    }

    //添加购物项
    public boolean addCake(User user,Cake cake) throws Exception{
        boolean flag = true;
        if(isExistCake(user,cake)){//存在该购物项
            flag=update(user,cake);
        }else{//不存在该购物项
            flag = add(user, cake);
        }
        add_cake_num(cake);
        return flag;
    }

    //若数据库存在该购物项,则数量加一
    private boolean update(User user,Cake cake) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt = null;
        String sql = "update cart set cake_num= cake_num+1,cart_price = cart_price+? where cart_name=? and cake_name=?";
        boolean res = false;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,cake.getCake_price());
            pstmt.setString(2,user.getUsername());
            pstmt.setString(3,cake.getCake_name());
            res = (pstmt.executeUpdate()==1);
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
        return res;
    }

    //若不存在该购物项，则添加该购物项，并把该Cake的数值赋予1
    private boolean add(User user,Cake cake) throws  Exception{
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt = null;
        String sql = "insert into cart(cart_name,cart_price,cake_name,cake_num) values (?,?,?,?)";
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setInt(2, cake.getCake_price());
            pstmt.setString(3,cake.getCake_name());
            pstmt.setInt(4,1);
            return pstmt.executeUpdate()>0;
        } finally {
            JDBCUtils.close(conn,pstmt);
        }
    }

    //无论是否存在购物项，都需要进行CAKE蛋糕内的热度增加
    public void add_cake_num(Cake cake) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt = null;
        String sql = "update cake set cake_num= cake_num+1 where cake_name=?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,cake.getCake_name());
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
    }


    //查询并返回该购物项
    public CartItem selectByCartName(User user,Cake cake) throws  Exception{
        conn = JDBCUtils.getInstance().getConnection();
        List<CartItem> list = new ArrayList<>();
        String sql = "select * from cart where cart_name = ? and cake_name = ?";
        PreparedStatement pstmt =null;
        ResultSet rs;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, cake.getCake_name());
            rs = pstmt.executeQuery();
            list=resultSetToBean(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
        return list.isEmpty()?null:list.get(0);
    }

    //查询返回该用户所有的购物项形成购物车
    public Cart getAllcart(User user) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        Cart cart = new Cart();
        List<CartItem> list = new ArrayList<>();
        String sql = "select * from cart where cart_name = ?";
        PreparedStatement pstmt =null;
        ResultSet rs;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            rs = pstmt.executeQuery();
            list=resultSetToBean(rs);
            cart.setList(list);
            cart.setPrice(cart.getPrice());
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
        return cart;
    }

    //将查询到的所有购物项添加到list列表中,并返回list
    private static List<CartItem> resultSetToBean(ResultSet rs) throws SQLException {
        List<CartItem> list = new ArrayList<>();
        CakeDao dao = new CakeDao();
        while (rs.next()){
            CartItem cartItem = new CartItem();
            String cake_name = rs.getString("cake_name");
            try {
                Cake cake =  dao.selectByCakename(cake_name);//获得蛋糕信息
                cartItem.setCake(cake);
                cartItem.setCakenum(Integer.parseInt(rs.getString("cake_num")));
                cartItem.setPrice(Integer.parseInt(rs.getString("cart_price")));
                list.add(cartItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    //蛋糕数量减1
    public boolean delete_cake(User user,Cake cake) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt = null;
        String sql = "update cart set cake_num= cake_num-1,cart_price = cart_price-? where cart_name=? and cake_name=?";
        boolean res = false;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,cake.getCake_price());
            pstmt.setString(2,user.getUsername());
            pstmt.setString(3,cake.getCake_name());
            res = (pstmt.executeUpdate()==1);
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
        return res;
    }

    //蛋糕数量减一，同时蛋糕影响力也需要减一
    public void delete_cake_num(Cake cake,int num) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt = null;
        String sql = "update cake set cake_num= cake_num-? where cake_name=?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,num);
            pstmt.setString(2,cake.getCake_name());
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
    }

    //当购物车内蛋糕数为1时，需要删除这个购物项
    public void delete_cartitem(User user,Cake cake) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt =null;
        String sql = "DELETE from cart where cake_name=? and cart_name = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,cake.getCake_name());
            pstmt.setString(2, user.getUsername());
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
    }

    //删除购物车内的所有蛋糕
    public void deleteAll(User user) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt =null;
        String sql = "DELETE from cart where cart_name = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
    }

    //删除购物车的同时，删除该用户购物车内所有蛋糕的影响力
    public void delete_re_All(User user) throws Exception{
        Cart cart = getAllcart(user);//先删除影响力再删除购物车信息
        List<CartItem> list = cart.getList();
        for(CartItem item:list){
            delete_cake_num(item.getCake(), item.getCakenum());
        }
    }
}
