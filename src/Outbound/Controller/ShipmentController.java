package Outbound.Controller;

import Outbound.Service.ShipmentService;
import Outbound.Shipment;

import java.util.List;
import java.util.Scanner;

public class ShipmentController {
    private final ShipmentService shipmentService = new ShipmentService();
    private final Scanner scanner = new Scanner(System.in);

    public void start(){
        while(true){
            System.out.println("=======출고 관리 시스템=========");
            System.out.println("1. 출고 등록");
            System.out.println("2. 출고 목록 조회");
            System.out.println("3. 출고 삭제");
            System.out.println("4. 종료");

            int choice = scanner.nextInt();
            switch(choice){
                case 1:
                    outboundRequestController();
                    break;
                case 2:
                    outboundTotalReadController();
                    break;

                case 3:
                    outboundDeleteController();
                    break;
                case 4:
                    System.out.println("프로그램 종료");
                    return;



            }

    }



    }

    public void outboundRequestController(){
        System.out.println("상품명을 입력하세요");
        String name = scanner.next();


        System.out.println("수량을 입력하세요");
        //int quantity = scanner.nextInt();
        while (!scanner.hasNextInt()) { // 숫자가 아닌 입력 방지
            System.out.println("잘못된 입력입니다. 숫자를 입력하세요.");
            scanner.next(); // 잘못된 입력을 버퍼에서 제거
        }
        int quantity = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기

        System.out.println("목적지를 입력하세요");
        String address = scanner.next();
        Shipment shipments = new Shipment(name,quantity,address);
        shipmentService.outboundRequestService(shipments);
    }
    public void outboundDeleteController(){
        System.out.println("삭제할 출고id를 입력하세요");
        int id = scanner.nextInt();
        shipmentService.outboundDeleteService(id);
    }

    public void outboundTotalReadController(){
        List<Shipment> s1 = shipmentService.outboundTotalReadService();
        if(s1.isEmpty()){
            System.out.println("정보가 없습니다.");
        }
        for(Shipment shipment : s1){
            System.out.println(shipment);
        }



    }




}
