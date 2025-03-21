package Outbound;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL  = "jdbc:mysql://localhost:3306/mypra5?serverTimezone=Asia/Seoul";
    private static final String USER  = "mypra5";
    private static final String PASS = "mypra5";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL,USER,PASS);
    }

}
