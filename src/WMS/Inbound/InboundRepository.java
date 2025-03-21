package WMS.Inbound;

import WMS.DatabaseConnection;
import WMS.InboundDTO;
import WMS.InventoryDTO;
import WMS.WarehouseDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InboundRepository {

    public void InboundRequestRepository(InboundDTO inboundDTO){

        //System.out.println("입고 요청 ID: " + inboundDTO.getInbound_id() + " (길이: " + inboundDTO.getInbound_id().length() + ")");
        String sql = "{CALL pro_inbound_request(?,?,?,?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            //cstmt.setString(1, inboundDTO.getInbound_id());
            cstmt.setString(1, inboundDTO.getProduct_id());
            cstmt.setInt(2, inboundDTO.getInbound_quantity());
            cstmt.setString(3, inboundDTO.getReq_inbound_day());
            cstmt.setString(4,inboundDTO.getWarehouse_id());

            // 실행
            cstmt.executeUpdate();
            System.out.println("입고 신청 완료");


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void InboundDeleteRepository(){

    }
//    public void InboundApproveRepository(int id){
//        String sql = "{CALL pro_inbound_approve(?)}";
//        try (Connection conn = DatabaseConnection.getConnection();
//             //CallableStatement cstmt = conn.prepareCall(sql))
//             PreparedStatement pstmt = conn.prepareStatement(sql){
//
//            pstmt.setInt(1, id);
//            System.out.println("관리자 승인 완료");
//            int rowsAffected = pstmt.executeUpdate();
//            if(rowsAffected > 0){
//                System.out.println("승인 성공");
//            }
//            else{
//                System.out.println("a");
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//
//    }


public void InboundApproveRepository(int id) {
    String sql = "{CALL pro_inbound_approve1(?)}";

    try (Connection conn = DatabaseConnection.getConnection();
         CallableStatement cstmt = conn.prepareCall(sql)) { //  CallableStatement 사용

        cstmt.setInt(1, id);

        int rowsAffected = cstmt.executeUpdate(); //  실행

        if (rowsAffected > 0) {
            System.out.println("승인 성공");
        } else {
            System.out.println("승인 실패 (해당 ID 없음)");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

}


    public void InboundRejectRepository(int id){
        String sql = "{CALL pro_inbound_reject(?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) { //  CallableStatement 사용

            cstmt.setInt(1, id);

            int rowsAffected = cstmt.executeUpdate(); //  실행

            if (rowsAffected > 0) {
                System.out.println("승인거절 성공");
            } else {
                System.out.println("승인 실패 (해당 ID 없음)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    public List<InboundDTO> InboundReadAllRepository(){
        List<InboundDTO> inbounds = new ArrayList<>();

        String sql = "select * from inbound";
        try(Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                InboundDTO inboundDTO  = new InboundDTO(
                        rs.getString("inbound_id"),
                        rs.getString("product_id"),
                        rs.getInt("inbound_quantity"),
                        rs.getString("req_inbound_day"),
                        rs.getString("status"),
                        rs.getString("warehouse_id")

                );
                inbounds.add(inboundDTO);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }


        return inbounds;

    }

    public List<WarehouseDTO> ReadAllWarehouseRepository(){
        List<WarehouseDTO> warehouses = new ArrayList<>();

        String sql = "select * from warehouse";
        try(Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                WarehouseDTO warehouseDTO  = new WarehouseDTO(
                        rs.getString("warehouse_id"),
                        rs.getString("warehouse_location"),
                        rs.getString("warehouse_manager")


                );
                warehouses.add(warehouseDTO);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }


        return warehouses;


    }


}

