package mvc.Dao;

import mvc.JavaBean.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderDao {
    Connection conn = null;

    //获取User订单的标识符（id）
    private List<String> getTime(User user) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        List<String> list = new ArrayList<>();
        String sql = "select * from orders where o_name=? order by o_time desc";
        PreparedStatement pstmt =null;
        ResultSet rs;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            rs = pstmt.executeQuery();
            while (rs.next()){
                String id = rs.getString("o_id");
                list.add(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
        return list;
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
                cartItem.setPrice(Integer.parseInt(rs.getString("o_price")));
                list.add(cartItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    //根据确切的时间返回订单信息
    public Order getOrder_re(User user,String id) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        List<CartItem> list = new ArrayList<>();
        Order order = new Order();
        String sql = "select * from orders,orders_xq where orders.o_id=orders_xq.o_id and orders.o_name = ? and orders.o_id = ?";
        PreparedStatement pstmt =null;
        ResultSet rs;
        CakeDao dao = new CakeDao();
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, id);
            rs = pstmt.executeQuery();
            List<String> str = new ArrayList<>();
            String state = null;
            String time = null;
            while (rs.next()){
                CartItem cartItem = new CartItem();
                String cake_name = rs.getString("o_cakename");//循环获取订单详情里面的订单项的名字
                try {
                    Cake cake =  dao.selectByCakename(cake_name);//获得该购物项的蛋糕信息
                    cartItem.setCake(cake);
                    state = rs.getString("o_state");//获取订单的状态
                    time = rs.getString("o_time");
                    cartItem.setCakenum(Integer.parseInt(rs.getString("o_cakenum")));//获得购买该蛋糕数量
                    cartItem.setPrice(Integer.parseInt(rs.getString("o_cakeprice")));//获得该蛋糕项的总价
                    list.add(cartItem);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            order.setList(list);
            order.setTime(time);
            order.setId(id);
            order.setPrice(order.getPrice());
            order.setState(state);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
        return order;
    }

    //根据时间列表获取该用户所有的订单
    public List<Order> getOrder(User user) throws Exception{
        List<String> id_list = getTime(user);//获取该用户的所有ID列表
        List<Order> order_list = new ArrayList<>();
        for(String id:id_list){
            Order order = getOrder_re(user,id);//获取所有订单
            order.setUsername(user.getUsername());//设置order名字
            order_list.add(order);//将订单放入专属于某个用户的订单列表
        }
        return order_list;
    }

    //取消该用户的订单
    public void deleteOrder(User user,Order order) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt =null;
        String sql = "DELETE from orders where o_name=? and o_time = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, order.getTime());
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //记得删除订单详情
            deleteOrder_xq(order);
            JDBCUtils.close(conn,pstmt);
        }
        //取消订单时，需要取消该订单所造成的所有蛋糕信息影响度
        List<CartItem> list = order.getList();
        for(CartItem item:list){
            //取消订单的每个蛋糕影响度
            new CartDao().delete_cake_num(item.getCake(), item.getCakenum());
        }
    }

    //取消订单，同时需要取消订单详情
    private void deleteOrder_xq(Order order) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt =null;
        String sql = "DELETE from orders_xq where o_id=?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, order.getId());
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
    }

    //添加订单
    public void add_order(User user) throws Exception{
        //先通过cart数据库获取用户的购物车订单
        CartDao dao = new CartDao();
        Cart cart = dao.getAllcart(user);
        List<CartItem> item_list = cart.getList();
        int price = 0;
        //生成唯一ID
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
        String id = sdf.format(System.currentTimeMillis());//系统唯一id
        for(CartItem item:item_list){
            add_cartitem(user,item,id);//对所有购物项进行添加到订单
            price+= item.getPrice();
        }
        add_order_one(user,id,price);//添加到Order
        //删除购物车内的所有购物项
        dao.deleteAll(user);
        //不能删除购物项内对蛋糕的影响力，所以不执行deleteAll_re方法
    }

    //只添加一次Order
    private void add_order_one(User user,String id, int price) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        //date为当前订单创建的时间
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        String name = user.getUsername();//用户名
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO orders(o_name,o_state,o_price,o_id,o_time) VALUES(?,?,?,?,?)";
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, "已付款");
            pstmt.setInt(3,price);
            pstmt.setString(4,id);
            pstmt.setString(5,date);
            pstmt.executeUpdate();
        } finally {
            JDBCUtils.close(conn,pstmt);
        }
    }

    //按照cartitem进行order的添加
    private void add_cartitem(User user,CartItem item,String id) throws Exception{
        addOrders_xq(id,item);
    }

    //订单详情的增加
    private void addOrders_xq(String id,CartItem item) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt = null;
        String sql = "insert into orders_xq(o_id,o_cakename,o_cakeprice,o_cakenum) values (?,?,?,?)";
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, item.getCake().getCake_name());
            pstmt.setInt(3, item.getPrice());
            pstmt.setInt(4, item.getCakenum());
            pstmt.executeUpdate();
        } finally {
            JDBCUtils.close(conn,pstmt);
        }
    }

    //管理员修改订单的状态(确认订单发货)
    public void setState(String username,String time) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt = null;
        String sql = "update orders set o_state='已发货' where o_name=? and o_time=?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username);
            pstmt.setString(2,time);
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
    }

    //用户签收并修改订单的状态(确认订单到达)
    public void setState(String id) throws Exception{
        conn = JDBCUtils.getInstance().getConnection();
        PreparedStatement pstmt = null;
        String sql = "update orders set o_state='已签收' where o_id = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,id);
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt);
        }
    }
}
