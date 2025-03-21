package jdbc.advanced_users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil{
// DB 연결 정보
private static final String URL = "jdbc:mysql://localhost:3306/mypra5?serverTimezone=Asia/Seoul";
private static final String USER = "mypra5";
private static final String PASSWORD = "mypra5";

static {
    try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Found MySQL driver");
    } catch (ClassNotFoundException e){e.printStackTrace();}
}

public static Connection getConnection() throws SQLException{
    return DriverManager.getConnection(URL, USER, PASSWORD);
}

}
