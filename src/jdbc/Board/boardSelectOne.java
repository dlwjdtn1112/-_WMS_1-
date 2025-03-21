package jdbc.Board;

import jdbc.users.User;

import java.sql.*;

public class boardSelectOne {
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
                    .append(" SELECT * FROM boards ")
                    .append(" WHERE btitle = ? ")
                    .toString();


            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1,15);
            //pstmt.setString(2,"자바");



            //4. SQL문 실행
            rs =  pstmt.executeQuery();

             if(rs.next()) {
                 Board boards  = new Board();
                 boards.setBno(rs.getInt("bno"));
                 boards.setBititle(rs.getString("btitle"));
                 boards.setBcontent(rs.getString("bcontent"));
                 boards.setBwriter(rs.getString("bwriter"));
                 boards.setBdate(rs.getString("bdate"));
                 boards.setBfilename(rs.getString("bfilename"));
                 boards.setBfiledata(rs.getBlob("bfiledata"));
                 System.out.println(boards);

             } else {
                 System.out.println("X");
             }

            while (rs.next()) {
                Board board = new Board();
                board.setBno(rs.getInt(1));
                board.setBititle(rs.getString("boardname"));
                board.setBcontent(rs.getString("boardcotents"));
                board.setBwriter(rs.getString("boardwriter"));
                board.setBfilename(rs.getString("boardfilename"));
                board.setBdate(rs.getString("boarddate"));
                System.out.println(board);
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
