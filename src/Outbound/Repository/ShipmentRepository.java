package Outbound.Repository;

import Outbound.DatabaseConnection;
import Outbound.Shipment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShipmentRepository {

    public void outboundRequestRepository(Shipment shipment){
        String sql = "insert into shipments (product_name,quantity,destination) values (?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, shipment.getProductName());
            pstmt.setInt(2,shipment.getQuantity());
            pstmt.setString(3,shipment.getDestination());
            pstmt.executeUpdate();
            System.out.println("출고 완료");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<Shipment> outboundTotalReadRepository() {
        List<Shipment> shipments = new ArrayList<>();
        String sql = "select * from shipments";

        try(Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                Shipment shipment = new Shipment(
                        rs.getInt("id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getString("destination")
                );
                shipments.add(shipment);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return shipments;
    }


    public void outboundDeleteRepository(int id){
        String sql ="delete from shipments where id= ?";
        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,id);
            int rowsAffected = pstmt.executeUpdate();

            if(rowsAffected > 0){
                System.out.println("출고 데이터 삭제 완료");
            }
            else{
                System.out.println("해당 ID의 출고 데이터");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
