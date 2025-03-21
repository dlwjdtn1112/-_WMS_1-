package jdbc.callablestmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

// 싱글톤 패턴을 적용한 커넥션 제공
public class DBUtil {

    private static Connection conn = null;

    //외부에서 인스턴스 생성을 막기 위해서
    private DBUtil(){}

    public static Connection getConnection()  {
        if(conn != null){
            return conn;
        }
        // 1.MYSQL 드라이버 로드
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 2. MySQL 연결 URL작성
            //String url = "jdbc:mysql://127.0.0.1:3306/mypra5?serverTimezone=Asia/Seoul";
            String url = "jdbc:mysql://localhost:3306/mypra5?serverTimezone=Asia/Seoul";
            String user = "mypra5";
            String password = "mypra5";
            conn   = DriverManager.getConnection(url, user, password);
            System.out.println("연결 성공");
        } catch (ClassNotFoundException e) {
            System.out.println("드라이버를 찾을 수 없습니다." + e.getMessage());
        }catch (SQLException e1){
            e1.printStackTrace();
        }
        return conn;



    }




}
