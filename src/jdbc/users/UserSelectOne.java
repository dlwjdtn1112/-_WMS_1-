package jdbc.users;

import java.sql.*;

/**
 * packageName   : jdbc.users
 * fileName      : UserInsert
 * author        : a
 * date          : 2025-02-27
 * description   :
 * =================================================
 * DATE             AUTHOR             NOTE
 * -------------------------------------------------
 * 2025-02-27        a              사용자 입력 자바클래스
 */
public class UserSelectOne {
    public static void main(String[] args) {
        Connection connection = null;
        ResultSet rs  = null;
        try {
            // 1. JDBC 드라이버 등록  : MYSQL DB 접근 하기 위한 드라이버 등록
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded ok!" + connection);

            // 2. Mysql DB에 연결객체를 얻어와서 연결하기
            //connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ssgdb?serverTimeZone=Asia/Seoul","ssg","ssg1234");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mypra5?serverTimezone=Asia/Seoul", "mypra5", "mypra5");
            System.out.println("Connection OK" + connection);

            //3. 매개변수화된 SQL 문 작성


            String query = new StringBuilder()
                    .append(" SELECT * FROM users ")
                    //.append(" WHERE userid = ? ")
                    .toString();


            PreparedStatement pstmt = connection.prepareStatement(query);
            //pstmt.setString(1, "ssgcom1");


            //4. SQL문 실행
             rs =  pstmt.executeQuery();

//             if(rs.next()) {
//                 User user = new User();
//                 user.setUserid(rs.getString("userid"));
//                 user.setUsername(rs.getString("username"));
//                 user.setUserpassword(rs.getString("userpassword"));
//                 user.setAge(rs.getInt("userage"));
//                 user.setEmail(rs.getString("useremail"));
//                 System.out.println(user);
//
//             } else {
//                 System.out.println("가입된 회원이 아닙니다.");
//             }

            while (rs.next()) {
                User user = new User();
                 user.setUserid(rs.getString("userid"));
                 user.setUsername(rs.getString("username"));
                 user.setUserpassword(rs.getString("userpassword"));
                 user.setAge(rs.getInt("userage"));
                 user.setEmail(rs.getString("useremail"));
                 System.out.println(user);
                }


            //5. PreparedStatement 객체 닫기
            pstmt.close();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("connection closed");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
