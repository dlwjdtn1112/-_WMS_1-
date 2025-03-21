package WMS;

import WMS.Inbound.InboundController;
import WMS.Inventory.InventoryController;
import WMS.Outbound.OutboundController;

import java.util.Scanner;

public class main {
    static InboundController inboundController = new InboundController();
    static OutboundController outboundController = new OutboundController();
    static InventoryController inventoryController = new InventoryController();
    //void start();
    //InboundController inboundController = new InboundController();
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
//        InboundController inboundController = new InboundController();
//        OutboundController outboundController = new OutboundController();
//        InventoryController inventoryController = new InventoryController();
        // 입고 관련 메뉴
        //inboundController.inbound_member_start();
        // 출고 관련 메뉴
        //outboundController.outbound_member_start();
        //재고 관리 메뉴
        //inventoryController.inventory_member_start();
        while(true){
            System.out.println("======창고 관리 시스템=======");
            System.out.println("1. 관리자");
            System.out.println("2. 회원");
            System.out.println("3. 창고 관리 시스템 종료");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    admin();
                case 2:
                    user();
                case 3:
                    System.out.println("프로그램 종료");
                    return;

            }
        }




    }

    static public void user(){
        while(true){
            System.out.println("======= 회원 시스템=========");
            System.out.println("1. 입고");
            System.out.println("2. 재고");
            System.out.println("3. 출고");
            System.out.println("4. 종료");
            int choice = scanner.nextInt();
            switch(choice){
                case 1:
                    inboundController.inbound_member_start();
                    break;
                case 2:
                    inventoryController.inventory_member_start();
                    break;
                case 3:
                    outboundController.outbound_member_start();
                case 4:
                    System.out.println("프로그램 종료");
                    return;



            }

        }

    }

    static public void admin(){
        while(true){
            System.out.println("======= 관리자 시스템=========");
            System.out.println("1. 입고");
            System.out.println("2. 재고");
            System.out.println("3. 출고");
            System.out.println("4. 종료");
            int choice = scanner.nextInt();
            switch(choice){
                case 1:
                    inboundController.inbound_admin_start();
                    break;
                case 2:
                    inventoryController.inventory_admin_start();
                    break;
                case 3:
                    outboundController.outbound_admin_start();
                    break;
                case 4:
                    System.out.println("종료");
                    return;





            }

        }

    }
}
