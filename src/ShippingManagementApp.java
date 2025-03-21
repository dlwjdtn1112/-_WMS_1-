import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShippingManagementApp {
    public static void main(String[] args) {
        // 저장 프로시저는 미리 DB에 등록되었다고 가정합니다.
        IShippingController controller = new ShippingController();
        controller.start();
    }

    // ==================== DTO 클래스 ====================

    // WarehouseDto: 창고 정보를 담는 DTO
    public static class WarehouseDto {
        private String ware_id;
        private String client_id;
        private String prod_id;
        private int quantity;
        // last_inbound_day, last_outbount_day는 필요 시 사용
        private Date last_inbound_day;
        private Date last_outbount_day;

        // Lombok @Data 대체: getter / setter 직접 작성 (간단히 구현)
        public String getWare_id() { return ware_id; }
        public void setWare_id(String ware_id) { this.ware_id = ware_id; }

        public String getClient_id() { return client_id; }
        public void setClient_id(String client_id) { this.client_id = client_id; }

        public String getProd_id() { return prod_id; }
        public void setProd_id(String prod_id) { this.prod_id = prod_id; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public Date getLast_inbound_day() { return last_inbound_day; }
        public void setLast_inbound_day(Date last_inbound_day) { this.last_inbound_day = last_inbound_day; }

        public Date getLast_outbount_day() { return last_outbount_day; }
        public void setLast_outbount_day(Date last_outbount_day) { this.last_outbount_day = last_outbount_day; }
    }

    // ProductDto: 제품 정보를 담는 DTO (필요 시 확장)
    public static class ProductDto {
        private String prod_id;
        private String brand;
        private String prod_name;
        private int prod_price;
        private int prod_code;
        private String prod_category;
        private BigDecimal prod_size;

        public String getProd_id() { return prod_id; }
        public void setProd_id(String prod_id) { this.prod_id = prod_id; }

        public String getBrand() { return brand; }
        public void setBrand(String brand) { this.brand = brand; }

        public String getProd_name() { return prod_name; }
        public void setProd_name(String prod_name) { this.prod_name = prod_name; }

        public int getProd_price() { return prod_price; }
        public void setProd_price(int prod_price) { this.prod_price = prod_price; }

        public int getProd_code() { return prod_code; }
        public void setProd_code(int prod_code) { this.prod_code = prod_code; }

        public String getProd_category() { return prod_category; }
        public void setProd_category(String prod_category) { this.prod_category = prod_category; }

        public BigDecimal getProd_size() { return prod_size; }
        public void setProd_size(BigDecimal prod_size) { this.prod_size = prod_size; }
    }

    // ==================== Repository 계층 ====================

    // 인터페이스: IShippingRepository
    public static interface IShippingRepository {
        Connection getConnection() throws SQLException;
        List<WarehouseDto> getAvailableWarehouses(String prodId) throws SQLException;
        void createOutbound(String outboundNumber, String prodId, String clientId, int quantity, String wareId) throws SQLException;
        void approveOutbound(String outboundNumber, String prodId) throws SQLException;
        void rejectOutbound(String outboundNumber, String prodId) throws SQLException;
        ResultSet getOutboundList(String procedureCall, int queryType, Date startDate, Date endDate, String prodId) throws SQLException;
        ResultSet getUnapprovedOutboundByClient(String clientId) throws SQLException;
        // 회원(client_id) 존재 여부 확인
        boolean checkClientExists(String clientId) throws SQLException;
    }

    // 구현체: ShippingRepository
    public static class ShippingRepository implements IShippingRepository {
        // DB 연결 정보 (환경에 맞게 수정)
        private static final String DB_URL = "jdbc:mysql://localhost:3306/mypra5?serverTimezone=Asia/Seoul&allowMultiQueries=true";
        private static final String DB_USER = "mypra5";
        private static final String DB_PASSWORD = "mypra5";

        @Override
        public Connection getConnection() throws SQLException {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }

        // sp_getWarehouseListByProduct 호출 시 client_id를 반환하도록 수정
        @Override
        public List<WarehouseDto> getAvailableWarehouses(String prodId) throws SQLException {
            List<WarehouseDto> warehouses = new ArrayList<>();
            try (Connection conn = getConnection()) {
                CallableStatement cs = conn.prepareCall("{call sp_getWarehouseListByProduct(?)}");
                cs.setString(1, prodId);
                ResultSet rs = cs.executeQuery();
                while (rs.next()) {
                    WarehouseDto dto = new WarehouseDto();
                    dto.setWare_id(rs.getString("ware_id"));
                    // 수정: 저장 프로시저에서 반환된 client_id를 매핑
                    dto.setClient_id(rs.getString("client_id"));
                    dto.setProd_id(prodId);
                    dto.setQuantity(rs.getInt("quantity"));
                    warehouses.add(dto);
                }
                rs.close();
                cs.close();
            }
            return warehouses;
        }

        @Override
        public void createOutbound(String outboundNumber, String prodId, String clientId, int quantity, String wareId) throws SQLException {
            try (Connection conn = getConnection()) {
                CallableStatement cs = conn.prepareCall("{call sp_createOutbound(?, ?, ?, ?, ?)}");
                cs.setString(1, outboundNumber);
                cs.setString(2, prodId);
                cs.setString(3, clientId);
                cs.setInt(4, quantity);
                cs.setString(5, wareId);
                cs.execute();
                cs.close();
            }
        }

        @Override
        public void approveOutbound(String outboundNumber, String prodId) throws SQLException {
            try (Connection conn = getConnection()) {
                CallableStatement cs = conn.prepareCall("{call sp_approveOutbound(?, ?)}");
                cs.setString(1, outboundNumber);
                cs.setString(2, prodId);
                cs.execute();
                cs.close();
            }
        }

        @Override
        public void rejectOutbound(String outboundNumber, String prodId) throws SQLException {
            try (Connection conn = getConnection()) {
                CallableStatement cs = conn.prepareCall("{call sp_rejectOutbound(?, ?)}");
                cs.setString(1, outboundNumber);
                cs.setString(2, prodId);
                cs.execute();
                cs.close();
            }
        }

        @Override
        public ResultSet getOutboundList(String procedureCall, int queryType, Date startDate, Date endDate, String prodId) throws SQLException {
            Connection conn = getConnection();
            CallableStatement cs = conn.prepareCall(procedureCall);
            cs.setInt(1, queryType); // 1: 전체, 2: 기간, 3: 특정 상품
            cs.setDate(2, startDate);
            cs.setDate(3, endDate);
            cs.setString(4, prodId);
            return cs.executeQuery();
        }

        @Override
        public ResultSet getUnapprovedOutboundByClient(String clientId) throws SQLException {
            Connection conn = getConnection();
            String sql = "SELECT * FROM outbound WHERE client_id = ? AND status IN (0,1)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, clientId);
            return ps.executeQuery();
        }

        // 회원(client_id) 존재 여부 확인
        @Override
        public boolean checkClientExists(String clientId) throws SQLException {
            Connection conn = getConnection();
            String sql = "SELECT COUNT(*) FROM user WHERE client_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, clientId);
            ResultSet rs = ps.executeQuery();
            boolean exists = false;
            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
            rs.close();
            ps.close();
            conn.close();
            return exists;
        }
    }

    // ==================== Service 계층 ====================

    // 인터페이스: IShippingService
    public static interface IShippingService {
        List<WarehouseDto> getAvailableWarehouses(String prodId);
        boolean createOutboundRequest(String prodId, String clientId, int quantity, String wareId);
        ResultSet getOutboundList(boolean approved, int queryType, Date startDate, Date endDate, String prodId);
        boolean approveOutbound(String outboundNumber, String prodId);
        boolean rejectOutbound(String outboundNumber, String prodId);
        ResultSet getUnapprovedOutboundByClient(String clientId);
        // 회원 존재 여부 확인 메서드 추가
        boolean checkClientExists(String clientId);
    }

    // 구현체: ShippingService
    public static class ShippingService implements IShippingService {
        private IShippingRepository repository;

        public ShippingService() {
            repository = new ShippingRepository();
        }

        @Override
        public List<WarehouseDto> getAvailableWarehouses(String prodId) {
            try {
                return repository.getAvailableWarehouses(prodId);
            } catch (SQLException e) {
                System.out.println("창고 리스트 조회 실패: " + e.getMessage());
                return new ArrayList<>();
            }
        }

        @Override
        public boolean createOutboundRequest(String prodId, String clientId, int quantity, String wareId) {
            try {
                String outboundNumber = "OUT" + System.currentTimeMillis();
                repository.createOutbound(outboundNumber, prodId, clientId, quantity, wareId);
                return true;
            } catch (SQLException e) {
                System.out.println("출고 요청 실패: " + e.getMessage());
                return false;
            }
        }

        @Override
        public ResultSet getOutboundList(boolean approved, int queryType, Date startDate, Date endDate, String prodId) {
            String procedureCall = approved ? "{call sp_getApprovedOutbound(?, ?, ?, ?)}" : "{call sp_getUnapprovedOutbound(?, ?, ?, ?)}";
            try {
                return repository.getOutboundList(procedureCall, queryType, startDate, endDate, prodId);
            } catch (SQLException e) {
                System.out.println("출고 리스트 조회 실패: " + e.getMessage());
                return null;
            }
        }

        @Override
        public boolean approveOutbound(String outboundNumber, String prodId) {
            try {
                repository.approveOutbound(outboundNumber, prodId);
                return true;
            } catch (SQLException e) {
                System.out.println("승인 처리 실패: " + e.getMessage());
                return false;
            }
        }

        @Override
        public boolean rejectOutbound(String outboundNumber, String prodId) {
            try {
                repository.rejectOutbound(outboundNumber, prodId);
                return true;
            } catch (SQLException e) {
                System.out.println("거부 처리 실패: " + e.getMessage());
                return false;
            }
        }

        @Override
        public ResultSet getUnapprovedOutboundByClient(String clientId) {
            try {
                return repository.getUnapprovedOutboundByClient(clientId);
            } catch (SQLException e) {
                System.out.println("미승인 내역 조회 실패: " + e.getMessage());
                return null;
            }
        }

        @Override
        public boolean checkClientExists(String clientId) {
            try {
                return repository.checkClientExists(clientId);
            } catch (SQLException e) {
                System.out.println("회원 확인 실패: " + e.getMessage());
                return false;
            }
        }
    }

    // ==================== Controller 계층 ====================

    // 인터페이스: IShippingController
    public static interface IShippingController {
        void start();
    }

    // 구현체: ShippingController
    public static class ShippingController implements IShippingController {
        private IShippingService service;
        private Scanner scanner;

        // 로그인한 회원의 client_id 저장 (회원 모드 전용)
        private String memberClientId;

        public ShippingController() {
            service = new ShippingService();
            scanner = new Scanner(System.in);
        }

        @Override
        public void start() {
            while (true) {
                System.out.println("========== 출고관리 시스템 ==========");
                System.out.println("1. 총관리자");
                System.out.println("2. 회원");
                System.out.println("0. 종료");
                System.out.print("역할 선택: ");
                int role = scanner.nextInt();
                scanner.nextLine(); // 개행 처리

                if (role == 0) {
                    System.out.println("시스템을 종료합니다.");
                    break;
                }
                switch (role) {
                    case 1:
                        adminMenu();
                        break;
                    case 2:
                        memberLogin(); // 회원 로그인 수행: client_id 입력받음
                        memberMenu();
                        break;
                    default:
                        System.out.println("잘못된 선택입니다.");
                }
            }
            scanner.close();
        }

        // 회원 로그인: client_id를 입력받아 존재 여부 확인 (존재할 때까지 반복)
        private void memberLogin() {
            while (true) {
                System.out.print("회원 ID를 입력하세요: ");  // client_id 입력
                String inputId = scanner.nextLine();
                if (service.checkClientExists(inputId)) {
                    memberClientId = inputId;  // 로그인한 회원의 client_id 저장
                    System.out.println("로그인 성공!");
                    break;
                } else {
                    System.out.println("등록된 회원이 아닙니다. 다시 입력해주세요.");
                }
            }
        }

        // 회원 메뉴
        private void memberMenu() {
            while (true) {
                System.out.println("\n----- 회원 메뉴 -----");
                System.out.println("1. 출고 요청");
                System.out.println("2. 출고여부 조회");
                System.out.println("0. 상위 메뉴로");
                System.out.print("선택: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 0) break;

                switch (choice) {
                    case 1:
                        processShippingRequest();
                        break;
                    case 2:
                        processShippingStatusQuery();
                        break;
                    default:
                        System.out.println("잘못된 선택입니다.");
                }
            }
        }

        // 총관리자 메뉴
        private void adminMenu() {
            while (true) {
                System.out.println("\n----- 총관리자 메뉴 -----");
                System.out.println("1. 미승인 출고 요청 조회 및 처리");
                System.out.println("0. 상위 메뉴로");
                System.out.print("선택: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 0) break;

                if (choice == 1) {
                    processAdminApproval();
                } else {
                    System.out.println("잘못된 선택입니다.");
                }
            }
        }

        // 회원: 출고 요청 처리
        private void processShippingRequest() {
            System.out.print("출고할 상품의 ID를 입력하세요: ");
            String prodId = scanner.nextLine();

            // 모든 창고 목록 조회 후, 로그인한 회원(client_id) 필터링
            List<WarehouseDto> allWarehouses = service.getAvailableWarehouses(prodId);
            List<WarehouseDto> warehouses = new ArrayList<>();
            for (WarehouseDto dto : allWarehouses) {
                // 수정: 저장된 client_id와 로그인한 회원의 client_id(memberClientId)를 비교하여 필터링
                if (dto.getClient_id().equals(memberClientId)) {
                    warehouses.add(dto);
                }
            }

            if (warehouses.isEmpty()) {
                System.out.println("해당 상품을 보유한 창고가 없습니다. 출고 요청을 종료합니다.");
                return;
            }

            System.out.println("\n== 사용 가능한 창고 리스트 ==");
            for (WarehouseDto dto : warehouses) {
                System.out.println("창고ID: " + dto.getWare_id() + ", 재고: " + dto.getQuantity());
            }

            System.out.print("출고할 창고의 ID를 입력하세요: ");
            String wareId = scanner.nextLine();
            System.out.print("출고할 상품 수량을 입력하세요: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            System.out.print("출고 요청을 진행하시겠습니까? ('yes' 입력): ");
            String confirm = scanner.nextLine();
            if (!confirm.equalsIgnoreCase("yes")) {
                System.out.println("출고 요청이 취소되었습니다.");
                return;
            }

            // 출고 요청 시 회원 client_id는 로그인한 값(memberClientId)를 사용
            if (service.createOutboundRequest(prodId, memberClientId, quantity, wareId)) {
                System.out.println("출고 요청 성공!");
            } else {
                System.out.println("출고 요청 실패!");
            }
        }

        // 회원: 출고 상태 조회 처리
        private void processShippingStatusQuery() {
            System.out.println("\n== 출고 상태 조회 ==");
            System.out.println("1. 출고 승인 리스트 조회");
            System.out.println("2. 출고 미승인 리스트 조회");
            System.out.print("리스트 선택: ");
            int listChoice = scanner.nextInt();
            scanner.nextLine();

            System.out.println("\n조회 방법:");
            System.out.println("1. 전체 조회");
            System.out.println("2. 특정 기간 조회 (예: 2024-02-01 ~ 2024-03-02)");
            System.out.println("3. 특정 상품 조회");
            System.out.print("선택: ");
            int queryType = scanner.nextInt();
            scanner.nextLine();

            Date startDate = null, endDate = null;
            String prodId = null;
            if (queryType == 2) {
                System.out.print("시작 날짜 (yyyy-mm-dd): ");
                startDate = Date.valueOf(scanner.nextLine());
                System.out.print("종료 날짜 (yyyy-mm-dd): ");
                endDate = Date.valueOf(scanner.nextLine());
            } else if (queryType == 3) {
                System.out.print("조회할 상품 ID 입력: ");
                prodId = scanner.nextLine();
            }

            boolean approved = (listChoice == 1);
            ResultSet rs = service.getOutboundList(approved, queryType, startDate, endDate, prodId);

            try {
                System.out.println("\n== 조회 결과 ==");
                while (rs != null && rs.next()) {
                    System.out.println("OutboundNumber: " + rs.getString("outbound_number")
                            + ", ProdID: " + rs.getString("prod_id")
                            + ", ClientID: " + rs.getString("client_id")
                            + ", Quantity: " + rs.getInt("quantity")
                            + ", Status: " + rs.getInt("status")
                            + ", RequestDate: " + rs.getDate("req_outbound_day")
                            + ", WarehouseID: " + rs.getString("ware_id"));
                }
                if (rs != null) rs.getStatement().close();
            } catch (SQLException e) {
                System.out.println("조회 실패: " + e.getMessage());
            }
        }

        // 총관리자: 미승인 출고 요청 조회 및 처리
        private void processAdminApproval() {
            System.out.print("검색할 회사(Client ID)를 입력하세요: ");
            String clientId = scanner.nextLine();
            ResultSet rs = service.getUnapprovedOutboundByClient(clientId);

            try {
                int index = 1;
                List<String[]> outboundList = new ArrayList<>();
                System.out.println("\n== 미승인 출고 내역 ==");
                while (rs != null && rs.next()) {
                    String outboundNumber = rs.getString("outbound_number");
                    String prodId = rs.getString("prod_id");
                    int quantity = rs.getInt("quantity");
                    int status = rs.getInt("status");
                    Date reqDate = rs.getDate("req_outbound_day");
                    String wareId = rs.getString("ware_id");
                    System.out.println(index + ". OutboundNumber: " + outboundNumber
                            + ", ProdID: " + prodId
                            + ", Quantity: " + quantity
                            + ", Status: " + status
                            + ", RequestDate: " + reqDate
                            + ", WarehouseID: " + wareId);
                    outboundList.add(new String[]{outboundNumber, prodId});
                    index++;
                }
                if (rs != null) rs.getStatement().close();

                if (outboundList.isEmpty()) {
                    System.out.println("미승인 출고 내역이 없습니다.");
                    return;
                }

                System.out.print("처리할 항목 번호를 선택하세요 (0 입력 시 메뉴로): ");
                int selection = scanner.nextInt();
                scanner.nextLine();
                if (selection == 0 || selection > outboundList.size()) {
                    System.out.println("메뉴로 돌아갑니다.");
                    return;
                }

                String[] selected = outboundList.get(selection - 1);
                String outboundNumber = selected[0];
                String prodId = selected[1];

                System.out.println("\n선택한 요청 처리:");
                System.out.println("1. 승인 완료");
                System.out.println("2. 승인 거부");
                System.out.println("3. 메뉴로 돌아가기");
                System.out.print("선택: ");
                int action = scanner.nextInt();
                scanner.nextLine();

                if (action == 1) {
                    if (service.approveOutbound(outboundNumber, prodId)) {
                        System.out.println("출고 승인 완료되었습니다.");
                    }
                } else if (action == 2) {
                    if (service.rejectOutbound(outboundNumber, prodId)) {
                        System.out.println("출고 승인 거부되었습니다.");
                    }
                } else {
                    System.out.println("메뉴로 돌아갑니다.");
                }

            } catch (SQLException e) {
                System.out.println("관리자 승인 처리 실패: " + e.getMessage());
            }
        }
    }
}
