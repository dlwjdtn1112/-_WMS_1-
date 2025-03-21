package WMS.Inventory;

import Outbound.DatabaseConnection;
import Outbound.Shipment;
import WMS.InventoryDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryRepository {

    public List<InventoryDTO> inventoryCheckRepository(){
        List<InventoryDTO> inventorys = new ArrayList<>();

        String sql = "select * from inventory";
        try(Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                InventoryDTO inventoryDTO  = new InventoryDTO(
                        rs.getString("product_id"),
                        rs.getInt("quantity"),
                        rs.getString("warehouse_id")
                );
                inventorys.add(inventoryDTO);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return inventorys;

    }


}
