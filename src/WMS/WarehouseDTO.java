package WMS;

import lombok.Data;

@Data
public class WarehouseDTO {
    String warehouse_id;
    String warehouse_location;
    String warehouse_manager;

    public WarehouseDTO(String warehouse_id, String warehouse_location, String warehouse_manager) {
        this.warehouse_id = warehouse_id;
        this.warehouse_location = warehouse_location;
        this.warehouse_manager = warehouse_manager;
    }


}
