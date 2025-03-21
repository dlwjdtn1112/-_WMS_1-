package WMS;

import lombok.Data;

@Data
public class InventoryDTO {
    String product_id;
    int quantity;
    String warehouse_id;

    public InventoryDTO(String productId, int InventoryQuantity,String inventoryWareId){
        this.product_id = productId;
        this.quantity = InventoryQuantity;
        this.warehouse_id = inventoryWareId;

    }

    @Override
    public String toString() {
        return "InventoryDTO{" +
                "product_id='" + product_id + '\'' +
                ", quantity=" + quantity +
                ", warehouse_id='" + warehouse_id + '\'' +
                '}';
    }

}
