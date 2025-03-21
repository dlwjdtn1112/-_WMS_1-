package jdbc1;

import jdbc.advanced_users.DBUtil;

import java.sql.*;
import java.util.Scanner;

public class User_Management_Service {
    private Scanner scan = new Scanner(System.in);


    public static void main(String[] args) {

        new User_Management_Service().memberStart();
    }



    public void memberStart(){
        while(true){
            int choice = displayMenu();
            switch(choice){
                case 1 :	// 추가
                    insertMember(); break;
                case 2 : 	// 삭제
                    deleteMember(); break;
                case 3 : 	// 수정  ==> 전체 항목 수정
                    updateMember(); break;
                case 4 : 	// 전체 자료 출력
                    displayMember(); break;
                case 5 : 	// 수정	==> 원하는 항목만 수정
                    updateMember2(); break;
                case 0 : 	// 종료
                    System.out.println("작업을 마칩니다.");
                    return;
                default :
                    System.out.println("번호를 잘못 입력했습니다. 다시입력하세요.");
            }
        }
    }

    // 회원 정보를 추가하는 메서드
    private void insertMember(){
        Connection conn = null;
        PreparedStatement pstmt = null;
        int cnt = 0;

        System.out.println();
        System.out.println("추가할 회원 정보를 입력하세요.");

        int count = 0;
        String memId = null;  // 회원ID가 저장될 변수
        do{
            System.out.print("회원ID >> ");
            memId = scan.next();
            count = getMemberCount(memId);
            if(count > 0){
                System.out.println(memId + "은(는) 이미 등록된 회원ID입니다.");
                System.out.println("다른 회원ID를 입력하세요.");
            }

        }while(count > 0);

        System.out.print("회원이름 >> ");
        String memName = scan.next();

        System.out.print("비밀번호 >> ");
        String memPass = scan.next();

        System.out.print("전화번호 >> ");
        String memTel = scan.next();

        scan.nextLine();  // 입력 버퍼 비우기
        System.out.print("회원주소 >> ");
        String memAddr = scan.nextLine();

        try {
            //DB 연결  후 쿼리 작성
            conn = DBUtil.getConnection();

            String sql = "INSERT INTO member1 (mem_id, mem_name, mem_pass, mem_tel, mem_addr) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,memId);
            pstmt.setString(2,memName);
            pstmt.setString(3, memPass);
            pstmt.setString(4, memTel);
            pstmt.setString(5, memAddr);
            cnt = pstmt.executeUpdate();

            if( cnt > 0 ){
                System.out.println("회원 정보 추가 성공!!!");
            }else{
                System.out.println("회원 정보 추가 실패~~~");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(pstmt!=null) try{ pstmt.close();   }catch(SQLException e){}
            if(conn!=null) try{ conn.close();   }catch(SQLException e){}
        }

    }

    // 회원 정보를 삭제하는 메서드
    private void deleteMember(){
        Connection conn = null;
        PreparedStatement pstmt = null;
        int cnt = 0;

        System.out.println();
        System.out.println("삭제할 회원 정보를 입력하세요.");
        System.out.print("삭제할 회원ID >> ");
        String memId = scan.next();

        try {
            conn = DBUtil.getConnection();

            String sql ="DELETE FROM member1 WHERE mem_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,memId);
            cnt = pstmt.executeUpdate(); //행의 갯수를 반환한다.



            if(cnt > 0){
                System.out.println("회원ID가 " + memId + "인 회원 삭제 성공!!");
            }else{
                System.out.println(memId + "은(는) 없는 회원ID이거나 "
                        + "삭제에 실패했습니다.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(pstmt!=null) try{ pstmt.close();   }catch(SQLException e){}
            if(conn!=null) try{ conn.close();   }catch(SQLException e){}
        }
    }

    // 회원 정보를 수정하는 메서드  ==> 전체 항목 수정
    private void updateMember(){
        Connection conn = null;
        PreparedStatement pstmt = null;
        int cnt = 0;

        System.out.println();
        System.out.println("수정할 회원 정보를 입력하세요.");
        System.out.print("수정할 회원ID >> ");
        String memId = scan.next();

        int count = getMemberCount(memId);
        if(count==0){ // 없는 회원이면...
            System.out.println(memId + "은(는) 없는 회원ID입니다.");
            System.out.println("수정 작업을 중단합니다.");
            return;
        }

        System.out.println();
        System.out.println("수정할 내용을 입력하세요.");
        System.out.print("새로운 회원이름 >> ");
        String newMemName = scan.next();

        System.out.print("새로운 비밀번호 >> ");
        String newMemPass = scan.next();

        System.out.print("새로운 전화번호 >> ");
        String newMemTel = scan.next();

        scan.nextLine();
        System.out.print("새로운 회원주소 >> ");
        String newMemAddr = scan.nextLine();

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE member1 SET mem_name = ?, mem_pass = ?, mem_tel = ?, mem_addr = ? WHERE mem_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newMemName);
            pstmt.setString(2, newMemPass);
            pstmt.setString(3, newMemTel);
            pstmt.setString(4, newMemAddr);
            pstmt.setString(5, memId);
            cnt = pstmt.executeUpdate();


            if(cnt > 0){
                System.out.println(memId + "회원 정보 수정 완료!!!");
            }else{
                System.out.println(memId + "회원 정보 수정 실패~~~");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            if(pstmt!=null) try{ pstmt.close();   }catch(SQLException e){}
            if(conn!=null) try{ conn.close();  }catch(SQLException e){}
        }

    }

    // 회원 정보를 수정하는 메서드  ==> 원하는 항목 수정
    private void updateMember2(){
        Connection conn = null;
        PreparedStatement pstmt = null;
        int cnt = 0;

        System.out.println();
        System.out.println("수정할 회원 정보를 입력하세요.");
        System.out.print("수정할 회원ID >> ");
        String memId = scan.next();

        int count = getMemberCount(memId);
        if(count==0){ // 없는 회원이면...
            System.out.println(memId + "은(는) 없는 회원ID입니다.");
            System.out.println("수정 작업을 중단합니다.");
            return;
        }

        int num; // 수정할 컬럼에 대한 선택 값이 저장될 변수
        String updateField = null;
        String updateTitle = null;
        do{
            System.out.println();
            System.out.println("수정할 항목을 선택하세요.");
            System.out.println(" 1.회원이름  2.비밀번호  3.전화번호  4.회원주소");
            System.out.println("----------------------------------------------");
            System.out.print("수정할 항목 선택 >> ");
            num = scan.nextInt();

            switch(num){
                case 1 : updateField = "mem_name";
                    updateTitle = "회원이름"; break;
                case 2 : updateField = "mem_pass";
                    updateTitle = "비밀번호"; break;
                case 3 : updateField = "mem_tel";
                    updateTitle = "전화번호"; break;
                case 4 : updateField = "mem_addr";
                    updateTitle = "회원주소"; break;
                default :
                    System.out.println("수정할 항목을 잘못 선택했습니다.");
                    System.out.println("다시 선택하세요.");
            }
        }while(num<1 || num>4);

        scan.nextLine();  // 입력 버퍼 비우기
        System.out.println();
        System.out.print("새로운 " + updateTitle + " >> ");
        String updateData = scan.nextLine();

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE member1 SET " + updateField + " = ? WHERE mem_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, updateData);
            pstmt.setString(2, memId);
            cnt = pstmt.executeUpdate();

            if(cnt>0){
                System.out.println(memId + " 회원 정보 수정 완료!!!");
            }else{
                System.out.println(memId + " 회원 정보 수정 실패~~~");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(pstmt!=null) try{ pstmt.close();   }catch(SQLException e){}
            if(conn!=null) try{ conn.close();   }catch(SQLException e){}
        }

    }


    // 전체 회원 정보를 출력하는 메서드
    private void displayMember(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        System.out.println();
        System.out.println("===============================================");
        System.out.println(" 회원ID   회원이름  비밀번호   전화번호    주 소");
        System.out.println("===============================================");

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM member1";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                String memId = rs.getString("mem_id");
                String memName = rs.getString("mem_name");
                String memPass = rs.getString("mem_pass");
                String memTel = rs.getString("mem_tel");
                String memAddr = rs.getString("mem_addr");
                System.out.printf("%-8s %-10s %-10s %-12s %s\n", memId, memName, memPass, memTel, memAddr);
            }


            System.out.println("===============================================");
            System.out.println("출력 작업 끝...");


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(rs!=null) try{ rs.close();  }catch(SQLException e){}
            if(stmt!=null) try{ stmt.close();  }catch(SQLException e){}
            if(conn!=null) try{ conn.close();  }catch(SQLException e){}
        }

    }

    // 회원ID를 인수값으로 받아서 해당 회원ID의 개수를 반환하는 메서드
    private int getMemberCount(String memId){ Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) AS cnt FROM member1 WHERE mem_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                count = rs.getInt("cnt");
            }
        } catch (SQLException e) {
            count = 0;
            e.printStackTrace();
        } finally {
            if(rs != null) try { rs.close(); } catch(SQLException e) { }
            if(pstmt != null) try { pstmt.close(); } catch(SQLException e) { }
            if(conn != null) try { conn.close(); } catch(SQLException e) { }
        }
        return count;

    }

    // 메뉴를 출력하고 선택한 작업 번호를 반환하는 메서드
    private int displayMenu(){
        System.out.println();
        System.out.println("== 작업 선택 ==");
        System.out.println("1. 자료 추가 ");
        System.out.println("2. 자료 삭제");
        System.out.println("3. 자료 수정");
        System.out.println("4. 전체 자료 출력");
        System.out.println("5. 자료 수정2");
        System.out.println("0. 작업 끝.");
        System.out.println("==================");
        System.out.print("원하는 작업 선택 >> ");
        int num = scan.nextInt();
        return num;
    }

}