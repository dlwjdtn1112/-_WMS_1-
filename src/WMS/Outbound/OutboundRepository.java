package WMS.Outbound;

import WMS.DatabaseConnection;
import WMS.InboundDTO;
import WMS.OutboundDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OutboundRepository {

    public void OutboundRequestRepository(OutboundDTO outboundDTO){
        String sql = "{CALL pro_outbound_request(?,?,?,?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            //cstmt.setString(1, inboundDTO.getInbound_id());
            cstmt.setString(1, outboundDTO.getProduct_id());
            cstmt.setInt(2, outboundDTO.getOutbound_quantity());
            cstmt.setString(3, outboundDTO.getReq_outbound_day());
            cstmt.setString(4,outboundDTO.getWarehouse_id());

            // 실행
            cstmt.executeUpdate();
            System.out.println("출고 신청 완료");


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void OutboundDeleteRepository(){

    }
    public List<OutboundDTO> OutboundReadAllRepository(){
        List<OutboundDTO> outbounds = new ArrayList<>();

        String sql = "select * from outbound";
        try(Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                OutboundDTO outboundDTO  = new OutboundDTO(
                        rs.getString("outbound_id"),
                        rs.getString("product_id"),
                        rs.getInt("outbound_quantity"),
                        rs.getString("req_outbound_day"),
                        rs.getString("status"),
                        rs.getString("warehouse_id")

                );
                outbounds.add(outboundDTO);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }


        return outbounds;
    }

    public void OutboundApproveRepository(int id){
        String sql = "{CALL pro_outbound_approve(?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) { //  CallableStatement 사용

            cstmt.setInt(1, id);

            int rowsAffected = cstmt.executeUpdate(); //  실행

            if (rowsAffected > 0) {
                System.out.println("출고 승인 성공");
            } else {
                System.out.println("승인 실패 (해당 ID 없음)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
