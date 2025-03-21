package jdbc.Board;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;

public class boardSelectAll {
    public static void main(String[] args) {
        Connection connection = null;

        try {
            // 1. JDBC 드라이버 등록
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded OK!");

            // 2. MySQL DB에 연결
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mypra5?serverTimezone=Asia/Seoul",
                    "mypra5", "mypra5"
            );
            System.out.println("Connection OK: " + connection);

            // 3. SQL 문 작성
            String query = "SELECT * FROM boards";
            PreparedStatement pstmt = connection.prepareStatement(query);

            // 4. SELECT 쿼리 실행 및 결과 처리
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                Board board = new Board();
                board.setBno(rs.getInt("bno"));
                board.setBtitle(rs.getString("btitle"));
                board.setBcontent(rs.getString("bcontent"));
                board.setBwriter(rs.getString("bwriter"));
                board.setBdate(String.valueOf(rs.getDate("bdate")));
                board.setBfilename(rs.getString("bfilename"));
                board.setBfiledata(rs.getBlob("bfiledata"));
                System.out.println(board);
            }

            // ResultSet과 PreparedStatement 닫기
            rs.close();
            pstmt.close();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBC 드라이버를 찾을 수 없습니다.", e);
        } catch (SQLException e) {
            throw new RuntimeException("SQL 실행 중 오류 발생", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Connection closed");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }

}
