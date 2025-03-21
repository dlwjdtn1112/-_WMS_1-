package WMS.Inventory;

import WMS.InventoryDTO;

import java.util.List;
import java.util.Scanner;

public class InventoryController {

    private final InventroyService inventroyService = new InventroyService();
    Scanner scanner = new Scanner(System.in);

    public void inventory_member_start(){
        while(true){
            System.out.println("=======재고 관리 시스템=========");
            System.out.println("1. 재고 현황");
            System.out.println("2. 종료");
            int choice = scanner.nextInt();
            switch(choice){
                case 1:
                    InventoryCheckController();
                    break;
                case 2:
                    System.out.println("종료");
                    return;


            }

        }

    }
    public void inventory_admin_start(){
        while(true){
            System.out.println("=======재고 관리 시스템=========");
            System.out.println("1. 재고 현황");
            System.out.println("2. 종료");
            int choice = scanner.nextInt();
            switch(choice){
                case 1:
                    InventoryCheckController();
                    break;
                case 2:
                    System.out.println("종료");
                    return;


            }

        }

    }

    public List<InventoryDTO> InventoryCheckController(){
        List<InventoryDTO> inventoryDTO = inventroyService.InventoryCheckService();
        if(inventoryDTO.isEmpty()){
            System.out.println("정보가 없습니다.");
        }
        for(InventoryDTO inventory : inventoryDTO){
            System.out.println(inventory.toString());
        }


        return inventoryDTO;
    }





}
