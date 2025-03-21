package jdbc.callablestmt;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class CallablestmtEx2 {

    Connection conn = null;
    CallableStatement cs = null;

    public CallablestmtEx2() throws SQLException {
        // 1. connection 얻어오기
        conn = DBUtil.getConnection();
        cs   = conn.prepareCall("{call P_INSERTRESULTCODES(?,?,?)}"); // ? : cDATA ?:CTname ? resultMsg

        // 3. 바인드 변수에 값을 셋팅 inpararmenter (?)애 값 넣기

        cs.setString(1,"it 기획자");
        cs.setString(2,"CODE1");

        // 4. out 파라미터에 저장된 프로시저의 수향결과에 대한 외부 변수 등록
        cs.registerOutParameter(3,java.sql.Types.VARCHAR);



        // 5.쿼리 수행
        // flag의 값은 resultSet 객체의 경우는 true,갱신카운트 또는 결과가 없는 false리턴 됨
        boolean flag  = cs.execute();
        System.out.println(flag);
        String resultMsg= cs.getNString(3);
        System.out.println(resultMsg);

        if(cs != null){
            cs.close();
        }
        if(conn !=null){
            conn.close();
        }






    }

    public static void main(String[] args) throws SQLException {

        new CallablestmtEx2();

    }

}
