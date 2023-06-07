package mvc.Dao;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库连接工具类
 */
public class JDBCUtils {

    public static JDBCUtils instance;
    DataSource ds;
    /**
     * 获取当前类的实例对象
     * @return
     */
    public static JDBCUtils getInstance() throws Exception {
        if (null == instance) {
            instance = new JDBCUtils();
        }
        return instance;
    }

    private JDBCUtils() throws Exception {
        //1、导入jar包
        //2、定义配置文件
        //3、加载配置文件
        Properties pro = new Properties();
        InputStream is = JDBCUtils.class.getResourceAsStream("/druid.properties");
        pro.load(is);
        //4、获取连接池对象
        ds = DruidDataSourceFactory.createDataSource(pro);
    }

    public Connection getConnection(){
        Connection conn = null;
        try {
            conn = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭JDBC连接
     * @param con 连接实例
     * @param pstmt PreparedStatement实例
     */
    public static void close(Connection con, PreparedStatement pstmt){
        try {
            if(pstmt!=null) {
                pstmt.close();
            }
            if(con!=null) {
                con.close();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
