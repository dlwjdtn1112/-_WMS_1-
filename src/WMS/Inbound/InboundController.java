package WMS.Inbound;

import WMS.InboundDTO;
import WMS.Inventory.InventroyService;
import WMS.InventoryDTO;
import WMS.OutboundDTO;
import WMS.WarehouseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;



public class InboundController {
    InventroyService inventroyService = new InventroyService();
    //int cnt = 1;
    //String inbound_name = "In";
    private final InboundService inboundService = new InboundService();
    Scanner scanner = new Scanner(System.in);

    public void inbound_member_start(){
        while(true){
            System.out.println("=======회원 입고 관리 시스템=========");
            System.out.println("1. 입고 등록");
            System.out.println("2. 입고 목록 조회");
            System.out.println("3. 입고 삭제(예정");
            System.out.println("4. 종료");
            //System.out.println("5. 상품 등록");
            int choice = scanner.nextInt();
            switch(choice){
                case 1:
                    InboundRequsetController();
                    break;
                case 2:
                    // InboundApproveController();
                    InboundReadAllController();
                    break;
                case 3:
                    System.out.println("예정");
                    break;
                case 4:
                    System.out.println("프로그램 종료");
                    return;



            }

        }

    }

    public void inbound_admin_start(){
        while(true){
            System.out.println("=======총관리자 입고 관리 시스템=========");
            System.out.println("1 .입고 승인");
            System.out.println("2. 입고 목록 조회");
            System.out.println("3. 종료");
            int choice = scanner.nextInt();
            switch(choice){
                case 1:
                    InboundApproveController();
                    break;
                case 2:
                    InboundReadAllController();
                    break;
                case 3:
                    System.out.println("프로그램 종료");
                    return;



            }

        }


    }


    public void InboundRequsetController(){

        //scanner.nextLine();
        //String inbound_id = inbound_name + cnt;
        //System.out.println("입고 요청 아이디: "+ inbound_id);

        //cnt += 1;

        System.out.println("입고할 상품 아이디를 입력하세요.");
        String product_id = scanner.next();



        System.out.println("입고할 수량을 입력하세요");
        int inbound_quantity = scanner.nextInt();

        scanner.nextLine();

        String req_inbound_day = LocalDate.now().toString();
        System.out.println("입고요청일: " + req_inbound_day);

        System.out.println("입고할 창고 아이디를 입력하세요.");
        String inbound_ware_id = scanner.nextLine();

        InboundDTO inboundDTO = new InboundDTO(product_id,inbound_quantity,req_inbound_day,inbound_ware_id);
        inboundService.InboundRequsetService(inboundDTO);

    }
    public void InboundReadAllController() {
        List<InboundDTO> inbounds = inboundService.InboundReadAllService();
        if (inbounds.isEmpty()) {
            System.out.println("정보가 없습니다.");
        }
        for (InboundDTO inbound : inbounds) {
            System.out.println(inbound.toString());
        }



    }

    public void InboundApproveController(){
//        InboundReadAllController();
//        System.out.println("입고 승인할 아이디를 입력하세요");
//        int approve_id = scanner.nextInt();
//        System.out.println(approve_id);
//        inboundService.InboundApproveService(approve_id);

        List<InboundDTO> inbounds = inboundService.InboundReadAllService();

        if (inbounds.isEmpty()) {
            System.out.println("입고 신청 내역이 없습니다.");
            return;
        }

        // 모든 입고 신청 내역 출력
        //InboundReadAllController();

        while (true) {
            int approve_id;

            // 예외 처리를 통해 잘못된 입력 방지
            while (true) {
                InboundReadAllController();
                System.out.println("입고 승인할 아이디를 입력하세요:");
                try {
                    approve_id = Integer.parseInt(scanner.nextLine().trim()); // 문자열로 받고 숫자로 변환
                    break; // 정상 입력되면 반복문 탈출
                } catch (NumberFormatException e) {
                    System.out.println("잘못된 입력입니다. 숫자를 입력하세요.");
                }
            }

            boolean isValid = false;
            for (InboundDTO inbound : inbounds) {
                if (Integer.parseInt(inbound.getInbound_id()) == approve_id) {
                    if (inbound.getStatus().equals("미승인")) { // 승인되지 않은 건만 처리
                        isValid = true;
                        break;
                    } else {
                        System.out.println("이미 승인된 요청입니다. 다른 ID를 입력하세요.");
                        return;
                    }
                }
            }

            if (isValid) {
                inboundService.InboundApproveService(approve_id);
                System.out.println("입고 신청이 승인되었습니다.");
                break; // 승인 후 종료
            } else {
                System.out.println("해당 ID가 존재하지 않습니다. 다시 입력하세요.");
            }
        }


    }
    public void InboundRejectController(){
        System.out.println("입고 승인 거부할 아이디를 입력하세요");
        int reject_id = scanner.nextInt();
        System.out.println(reject_id);
        inboundService.InboundRejectService(reject_id);
    }

    public void adminMenu(){
        //아직 못함
    }
    public void userMenu(){
        System.out.println("=======회원 창고 관리 시스템=========");
        System.out.println("1.입고요청 하기");
        System.out.println("2.재고 현황");
        System.out.println("3.출고요청 하기");
        int choice = scanner.nextInt();
        switch(choice){
            case 1:
                inbound_member_start();
                break;
            case 2:
                //제고 현황 보기(사용자)
            case 3:
                //출고 요청하기(사용자)
        }


        //inbound_member_start();

    }

    public void ReadAllWarehouseController(){
        inboundService.ReadAllWarehouseService();

    }



}
