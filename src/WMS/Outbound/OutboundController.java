package WMS.Outbound;

import WMS.InboundDTO;
import WMS.OutboundDTO;
import WMS.InventoryDTO;
import WMS.Inventory.InventoryController;


import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class OutboundController {

    private final OutboundService outboundService = new OutboundService();
     final InventoryController inventoryService = new InventoryController();
    Scanner scanner = new Scanner(System.in);

    public void outbound_member_start(){
        while(true){
            System.out.println("=======회원 출고 관리 시스템=========");
            System.out.println("1. 출고 등록");
            System.out.println("2. 출고 목록 조회(관리자 출고 승인 테스트)");
            System.out.println("3. 출고 삭제(예정)");
            System.out.println("4. 종료");
            int choice = scanner.nextInt();
            switch(choice){
                case 1:
                    OutboundRequsetController();
                    break;
                case 2:
                    OutboundAppriveController();
                    break;
                case 3:
                    OutboundDeleteController();
                    break;
                case 4:
                    System.out.println("프로그램 종료");
                    return;



            }

        }

    }

    public void outbound_admin_start(){
        while(true){
            System.out.println("=======총관리자 출고 관리 시스템=========");
            System.out.println("1 .출고 승인");
            System.out.println("2. 출고 목록 조회");
            System.out.println("3. 종료");
            int choice = scanner.nextInt();
            switch(choice){
                case 1:
                    OutboundAppriveController();
                    break;
                case 2:
                    OutboundReadAllController();
                    break;
                case 3:
                    System.out.println("프로그램 종료");
                    return;

            }

        }


    }

    public void OutboundRequsetController(){



//        System.out.println("출고할 상품 아이디를 입력하세요.");
//        String product_id = scanner.next();
//
//
//
//        System.out.println("출고할 수량을 입력하세요");
//        int outbound_quantity = scanner.nextInt();
//        scanner.nextLine();
//
//        String req_outbound_day = LocalDate.now().toString();
//        System.out.println("출고요청일: " + req_outbound_day);
//
//        System.out.println("출고할 창고 아이디를 입력하세요.");
//        String outbound_ware_id = scanner.nextLine();
//        OutboundDTO outboundDTO = new OutboundDTO(product_id,outbound_quantity,req_outbound_day,outbound_ware_id);
//        outboundService.OutboundRequsetService(outboundDTO);
//        System.out.println("--------------------------");
        List<InventoryDTO> inventorys = inventoryService.InventoryCheckController();

        String product_id;
        String outbound_ware_id;
        int outbound_quantity = 0;
        InventoryDTO matched = null;

        // 상품 ID 입력
        System.out.println("출고할 상품 아이디를 입력하세요:");
        product_id = scanner.next();

        scanner.nextLine(); // next() 뒤 개행 제거

        // 창고 ID 입력
        System.out.println("출고할 창고 아이디를 입력하세요:");
        outbound_ware_id = scanner.nextLine();

        // 해당 상품 + 창고의 재고 검색
        for (InventoryDTO dto : inventorys) {
            if (dto.getProduct_id().equals(product_id) &&
                    dto.getWarehouse_id().equals(outbound_ware_id)) {
                matched = dto;
                break;
            }
        }

        if (matched == null) {
            System.out.println("해당 상품이 해당 창고에 존재하지 않습니다. 출고 등록 실패.");
            return;
        }

        // 출고 수량 입력 및 검증
        while (true) {
            System.out.println("출고할 수량을 입력하세요 (현재 재고: " + matched.getQuantity() + "개)");
            try {
                outbound_quantity = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다. 숫자를 입력하세요.");
                continue;
            }

            if (outbound_quantity <= 0) {
                System.out.println("출고 수량은 1 이상이어야 합니다.");
            } else if (outbound_quantity > matched.getQuantity()) {
                System.out.println("출고 수량이 재고보다 많습니다. 다시 입력하세요.");
            } else {
                break;
            }
        }

        // 출고 요청일은 오늘 날짜
        String req_outbound_day = LocalDate.now().toString();
        System.out.println("출고요청일: " + req_outbound_day);

        // DTO 생성 및 서비스 호출
        OutboundDTO outboundDTO = new OutboundDTO(product_id, outbound_quantity, req_outbound_day, outbound_ware_id);
        outboundService.OutboundRequsetService(outboundDTO);

        System.out.println("출고 요청이 등록되었습니다.");





    }


    public void OutboundDeleteController(){
        outboundService.OutboundDeleteService();
    }
    public void OutboundAppriveController(){
//        OutboundReadAllController();
//        System.out.println("출고 승인할 아이디를 입력하세요");
//        int approve_id = scanner.nextInt();
//        System.out.println(approve_id);
//        outboundService.OutboundApproveService(approve_id);
        List<OutboundDTO> outbounds = outboundService.OutboundReadAllService();

        if (outbounds.isEmpty()) {
            System.out.println("출고 신청 내역이 없습니다.");
            return;
        }

        // 모든 출고 신청 내역 출력
        OutboundReadAllController();

        while (true) {
            int approve_id;

            // 예외 처리를 통해 잘못된 입력 방지
            while (true) {
                System.out.println("출고 승인할 아이디를 입력하세요:");
                try {
                    approve_id = Integer.parseInt(scanner.nextLine().trim()); // 문자열로 받고 숫자로 변환
                    break; // 정상 입력되면 반복문 탈출
                } catch (NumberFormatException e) {
                    System.out.println("잘못된 입력입니다. 숫자를 입력하세요.");
                }
            }

            boolean isValid = false;
            for (OutboundDTO outbound : outbounds) {
                if (Integer.parseInt(outbound.getOutbound_id()) == approve_id) {
                    if (outbound.getStatus().equals("미승인")) { // 승인되지 않은 건만 처리
                        isValid = true;
                        break;
                    } else {
                        System.out.println("이미 승인된 요청입니다. 다른 ID를 입력하세요.");
                        return;
                    }
                }
            }

            if (isValid) {
                outboundService.OutboundApproveService(approve_id);
                System.out.println("출고 신청이 승인되었습니다.");
                break; // 승인 후 종료
            } else {
                System.out.println("해당 ID가 존재하지 않습니다. 다시 입력하세요.");
            }
        }
    }
    public void OutboundReadAllController(){
        List<OutboundDTO> outbounds = outboundService.OutboundReadAllService();
        if (outbounds.isEmpty()) {
            System.out.println("정보가 없습니다.");
        }
        for (OutboundDTO outbound : outbounds) {
            System.out.println(outbound.toString());
        }

    }


}
