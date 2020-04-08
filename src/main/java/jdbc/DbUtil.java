package jdbc;

import java.sql.*;

public class DbUtil {
    public static final String URL = "jdbc:mysql://localhost:3306/ktv_sys?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Hongkong";
    public static final String USER = "root";
    public static final String PASSWORD = "zkjxx950626";


    static{
        try {
            //1.加载驱动程序
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2. 获得数据库连接
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeJDBC(ResultSet rs, PreparedStatement ps, Connection conn) {
        try{
            rs.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        try {
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

