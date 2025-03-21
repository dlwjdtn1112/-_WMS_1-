package jdbc.Board;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;

public class boardInsert {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Connection connection = null;

        try {
            // 1. JDBC 드라이버 등록  : MYSQL DB 접근 하기 위한 드라이버 등록
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded ok!" + connection);

            // 2. Mysql DB에 연결객체를 얻어와서 연결하기
            //connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ssgdb?serverTimeZone=Asia/Seoul","ssg","ssg1234");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mypra5?serverTimezone=Asia/Seoul", "mypra5", "mypra5");
            System.out.println("Connection OK" + connection);

            //3. 매개변수화된 SQL 문 작성

            String query = "" +
                    "INSERT INTO boards (btitle, bcontent, bwriter,bdate,bfilename,bfiledata) " +
                    "VALUES (?,?,?,now(),?,?); ";

            //매개변수회원 SQL문 작성

            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1,"바");
            pstmt.setString(2,"dd");
            pstmt.setString(3,"ljs");
            pstmt.setString(4,"spring (1).jpg");
            pstmt.setBlob(5,new FileInputStream("src/jdbc/images/spring (1).jpg"));

            // pstmt.setString(5,new FileInputStream("src/images/spring (1).jpg"));


            //4. SQL문 실행
            int rows =  pstmt.executeUpdate();
            System.out.println(rows + " rows inserted");

            if(rows == 1){
                ResultSet re = pstmt.getGeneratedKeys();
                if(re.next()){
                    int bno = re.getInt(1);
                    System.out.println(bno);

                }
                re.close();
            }
            //5. PreparedStatement 객체 닫기
            pstmt.close();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
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
