package WMS.Inventory;

import WMS.InventoryDTO;

import java.util.List;

public class InventroyService {
    private final InventoryRepository inventoryRepository = new InventoryRepository();

    public List<InventoryDTO> InventoryCheckService(){

        return inventoryRepository.inventoryCheckRepository();


    }





}
