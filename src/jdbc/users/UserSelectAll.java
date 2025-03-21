//package jdbc.users;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
///**
// * packageName   : jdbc.users
// * fileName      : UserInsert
// * author        : a
// * date          : 2025-02-27
// * description   :
// * =================================================
// * DATE             AUTHOR             NOTE
// * -------------------------------------------------
// * 2025-02-27        a              사용자 입력 자바클래스
// */
//public class UserSelectAll {
//    public static void main(String[] args) {
//        Connection connection = null;
//
//        try {
//            // 1. JDBC 드라이버 등록  : MYSQL DB 접근 하기 위한 드라이버 등록
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            System.out.println("Driver loaded ok!" + connection);
//
//            // 2. Mysql DB에 연결객체를 얻어와서 연결하기
//            //connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ssgdb?serverTimeZone=Asia/Seoul","ssg","ssg1234");
//            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mypra5?serverTimezone=Asia/Seoul", "mypra5", "mypra5");
//            System.out.println("Connection OK" + connection);
//
//            //3. 매개변수화된 SQL 문 작성
//
//
//            String query = new StringBuilder()
//                    .append(" SELECT * FROM users ")
//                    .toString();
//
//
//            PreparedStatement pstmt = connection.prepareStatement(query);
//            pstmt.setString(1, "12345678910");
//            pstmt.setString(2, "mycaptain622");
//
//            //4. SQL문 실행
//            int rows =  pstmt.executeUpdate();
//            System.out.println(rows + " rows update completed");
//            //5. PreparedStatement 객체 닫기
//            pstmt.close();
//
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (connection != null) {
//                try {
//                    connection.close();
//                    System.out.println("connection closed");
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
//    }
//}
