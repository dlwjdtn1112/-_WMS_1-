//package Test_Project;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//public class ShippingManagementApp {
//
//    // 애플리케이션 진입점
//    public static void main(String[] args) {
//        // 저장 프로시저 초기화: 필요한 모든 프로시저 생성
//        ShippingRepository repo = new ShippingRepository();
//        repo.initializeProcedures();
//
//        // 컨트롤러 시작
//        IShippingController controller = new ShippingController();
//        controller.start();
//    }
//
//    // 1. POJO: Warehouse 클래스 (창고 정보)
//    static class Warehouse {
//        private String wareId;
//        private String wareName;
//        private String wareLocation;
//        private int quantity;
//
//        public String getWareId() {
//            return wareId;
//        }
//        public void setWareId(String wareId) {
//            this.wareId = wareId;
//        }
//        public String getWareName() {
//            return wareName;
//        }
//        public void setWareName(String wareName) {
//            this.wareName = wareName;
//        }
//        public String getWareLocation() {
//            return wareLocation;
//        }
//        public void setWareLocation(String wareLocation) {
//            this.wareLocation = wareLocation;
//        }
//        public int getQuantity() {
//            return quantity;
//        }
//        public void setQuantity(int quantity) {
//            this.quantity = quantity;
//        }
//    }
//
//    // 2. Repository 계층 인터페이스
//    public interface IShippingRepository {
//        Connection getConnection() throws SQLException;
//        List<Warehouse> getAvailableWarehouses(String prodId) throws SQLException;
//        void createOutbound(String outboundNumber, String prodId, String clientId, int quantity, String wareId) throws SQLException;
//        void approveOutbound(String outboundNumber, String prodId) throws SQLException;
//        void rejectOutbound(String outboundNumber, String prodId) throws SQLException;
//        ResultSet getOutboundList(String procedureCall, int queryType, Date startDate, Date endDate, String prodId) throws SQLException;
//        ResultSet getUnapprovedOutboundByClient(String clientId) throws SQLException;
//        void initializeProcedures();
//    }
//
//    // 2. Repository 계층 구현체
//    public static class ShippingRepository implements IShippingRepository {
//        // DB 연결 정보 (환경에 맞게 수정)
//        private static final String DB_URL = "jdbc:mysql://localhost:3306/mypra5?serverTimezone=Asia/Seoul&allowMultiQueries=true";
//        private static final String DB_USER = "mypra5";
//        private static final String DB_PASSWORD = "mypra5";
//
//        @Override
//        public Connection getConnection() throws SQLException {
//            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//        }
//
//        // sp_getWarehouseListByProduct 저장 프로시저 호출
//        @Override
//        public List<Warehouse> getAvailableWarehouses(String prodId) throws SQLException {
//            List<Warehouse> warehouses = new ArrayList<>();
//            try (Connection conn = getConnection()) {
//                CallableStatement cs = conn.prepareCall("{call sp_getWarehouseListByProduct(?)}");
//                cs.setString(1, prodId);
//                ResultSet rs = cs.executeQuery();
//                while (rs.next()) {
//                    Warehouse w = new Warehouse();
//                    w.setWareId(rs.getString("ware_id"));
//                    w.setWareName(rs.getString("ware_name"));
//                    w.setWareLocation(rs.getString("ware_location"));
//                    w.setQuantity(rs.getInt("quantity"));
//                    warehouses.add(w);
//                }
//                rs.close();
//                cs.close();
//            }
//            return warehouses;
//        }
//
//        @Override
//        public void createOutbound(String outboundNumber, String prodId, String clientId, int quantity, String wareId) throws SQLException {
//            try (Connection conn = getConnection()) {
//                CallableStatement cs = conn.prepareCall("{call sp_createOutbound(?, ?, ?, ?, ?)}");
//                cs.setString(1, outboundNumber);
//                cs.setString(2, prodId);
//                cs.setString(3, clientId);
//                cs.setInt(4, quantity);
//                cs.setString(5, wareId);
//                cs.execute();
//                cs.close();
//            }
//        }
//
//        @Override
//        public void approveOutbound(String outboundNumber, String prodId) throws SQLException {
//            try (Connection conn = getConnection()) {
//                CallableStatement cs = conn.prepareCall("{call sp_approveOutbound(?, ?)}");
//                cs.setString(1, outboundNumber);
//                cs.setString(2, prodId);
//                cs.execute();
//                cs.close();
//            }
//        }
//
//        @Override
//        public void rejectOutbound(String outboundNumber, String prodId) throws SQLException {
//            try (Connection conn = getConnection()) {
//                CallableStatement cs = conn.prepareCall("{call sp_rejectOutbound(?, ?)}");
//                cs.setString(1, outboundNumber);
//                cs.setString(2, prodId);
//                cs.execute();
//                cs.close();
//            }
//        }
//
//        @Override
//        public ResultSet getOutboundList(String procedureCall, int queryType, Date startDate, Date endDate, String prodId) throws SQLException {
//            Connection conn = getConnection();
//            CallableStatement cs = conn.prepareCall(procedureCall);
//            cs.setInt(1, queryType); // 1: 전체, 2: 기간, 3: 특정 상품
//            cs.setDate(2, startDate);
//            cs.setDate(3, endDate);
//            cs.setString(4, prodId);
//            return cs.executeQuery();
//            // 주의: 호출한 쪽에서 ResultSet, Statement, Connection close 필요.
//        }
//
//        @Override
//        public ResultSet getUnapprovedOutboundByClient(String clientId) throws SQLException {
//            Connection conn = getConnection();
//            String sql = "SELECT * FROM outbound WHERE client_id = ? AND status IN (0, 1)";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setString(1, clientId);
//            return ps.executeQuery();
//        }
//
//        // 모든 필요한 저장 프로시저 초기화
//        @Override
//        public void initializeProcedures() {
//            try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
//                // sp_getWarehouseListByProduct
//                stmt.execute("DROP PROCEDURE IF EXISTS sp_getWarehouseListByProduct");
//                String proc1 = "CREATE PROCEDURE sp_getWarehouseListByProduct(IN p_prod_id VARCHAR(10)) " +
//                        "BEGIN " +
//                        "SELECT w.ware_id, wa.ware_name, wa.ware_location, w.quantity " +
//                        "FROM warehouse w " +
//                        "JOIN warehouse_area wa ON w.ware_id = wa.ware_id " +
//                        "WHERE w.prod_id = p_prod_id AND w.quantity > 0; " +
//                        "END";
//                stmt.execute(proc1);
//
//                // sp_createOutbound
//                stmt.execute("DROP PROCEDURE IF EXISTS sp_createOutbound");
//                String proc2 = "CREATE PROCEDURE sp_createOutbound(" +
//                        "IN p_outbound_number VARCHAR(30), " +
//                        "IN p_prod_id VARCHAR(10), " +
//                        "IN p_client_id VARCHAR(10), " +
//                        "IN p_quantity INT, " +
//                        "IN p_ware_id VARCHAR(10)) " +
//                        "BEGIN " +
//                        "DECLARE available_quantity INT; " +
//                        "SELECT quantity INTO available_quantity FROM warehouse WHERE prod_id = p_prod_id AND ware_id = p_ware_id; " +
//                        "IF available_quantity IS NULL OR available_quantity < p_quantity THEN " +
//                        "SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '창고에 출고할 상품이 없거나 수량이 부족합니다.'; " +
//                        "ELSE " +
//                        "INSERT INTO outbound (outbound_number, prod_id, client_id, quantity, status, req_outbound_day, ware_id) " +
//                        "VALUES (p_outbound_number, p_prod_id, p_client_id, p_quantity, 0, CURDATE(), p_ware_id); " +
//                        "UPDATE warehouse SET quantity = quantity - p_quantity, last_outbount_day = CURDATE() " +
//                        "WHERE prod_id = p_prod_id AND ware_id = p_ware_id; " +
//                        "END IF; " +
//                        "END";
//                stmt.execute(proc2);
//
//                // sp_approveOutbound
//                stmt.execute("DROP PROCEDURE IF EXISTS sp_approveOutbound");
//                String proc3 = "CREATE PROCEDURE sp_approveOutbound(" +
//                        "IN p_outbound_number VARCHAR(30), " +
//                        "IN p_prod_id VARCHAR(10)) " +
//                        "BEGIN " +
//                        "UPDATE outbound SET status = 2 " +
//                        "WHERE outbound_number = p_outbound_number AND prod_id = p_prod_id; " +
//                        "END";
//                stmt.execute(proc3);
//
//                // sp_rejectOutbound
//                stmt.execute("DROP PROCEDURE IF EXISTS sp_rejectOutbound");
//                String proc4 = "CREATE PROCEDURE sp_rejectOutbound(" +
//                        "IN p_outbound_number VARCHAR(30), " +
//                        "IN p_prod_id VARCHAR(10)) " +
//                        "BEGIN " +
//                        "UPDATE outbound SET status = 1 " +
//                        "WHERE outbound_number = p_outbound_number AND prod_id = p_prod_id; " +
//                        "END";
//                stmt.execute(proc4);
//
//                // sp_getApprovedOutbound
//                stmt.execute("DROP PROCEDURE IF EXISTS sp_getApprovedOutbound");
//                String proc5 = "CREATE PROCEDURE sp_getApprovedOutbound(" +
//                        "IN p_type INT, " +
//                        "IN p_start_date DATE, " +
//                        "IN p_end_date DATE, " +
//                        "IN p_prod_id VARCHAR(10)) " +
//                        "BEGIN " +
//                        "IF p_type = 1 THEN " +
//                        "   SELECT * FROM outbound WHERE status = 2; " +
//                        "ELSEIF p_type = 2 THEN " +
//                        "   SELECT * FROM outbound WHERE status = 2 AND req_outbound_day BETWEEN p_start_date AND p_end_date; " +
//                        "ELSEIF p_type = 3 THEN " +
//                        "   SELECT * FROM outbound WHERE status = 2 AND prod_id = p_prod_id; " +
//                        "END IF; " +
//                        "END";
//                stmt.execute(proc5);
//
//                // sp_getUnapprovedOutbound
//                stmt.execute("DROP PROCEDURE IF EXISTS sp_getUnapprovedOutbound");
//                String proc6 = "CREATE PROCEDURE sp_getUnapprovedOutbound(" +
//                        "IN p_type INT, " +
//                        "IN p_start_date DATE, " +
//                        "IN p_end_date DATE, " +
//                        "IN p_prod_id VARCHAR(10)) " +
//                        "BEGIN " +
//                        "IF p_type = 1 THEN " +
//                        "   SELECT * FROM outbound WHERE status IN (0,1); " +
//                        "ELSEIF p_type = 2 THEN " +
//                        "   SELECT * FROM outbound WHERE status IN (0,1) AND req_outbound_day BETWEEN p_start_date AND p_end_date; " +
//                        "ELSEIF p_type = 3 THEN " +
//                        "   SELECT * FROM outbound WHERE status IN (0,1) AND prod_id = p_prod_id; " +
//                        "END IF; " +
//                        "END";
//                stmt.execute(proc6);
//
//                System.out.println("Stored procedures created successfully.");
//            } catch (SQLException e) {
//                System.out.println("Failed to initialize procedures: " + e.getMessage());
//            }
//        }
//    }
//
//    // 3. Service 계층 인터페이스
//    public interface IShippingService {
//        List<Warehouse> getAvailableWarehouses(String prodId);
//        boolean createOutboundRequest(String prodId, String clientId, int quantity, String wareId);
//        ResultSet getOutboundList(boolean approved, int queryType, Date startDate, Date endDate, String prodId);
//        boolean approveOutbound(String outboundNumber, String prodId);
//        boolean rejectOutbound(String outboundNumber, String prodId);
//        ResultSet getUnapprovedOutboundByClient(String clientId);
//    }
//
//    // 3. Service 계층 구현체
//    public static class ShippingService implements IShippingService {
//        private IShippingRepository repository;
//
//        public ShippingService() {
//            repository = new ShippingRepository();
//        }
//
//        @Override
//        public List<Warehouse> getAvailableWarehouses(String prodId) {
//            try {
//                return repository.getAvailableWarehouses(prodId);
//            } catch (SQLException e) {
//                System.out.println("창고 리스트 조회 실패: " + e.getMessage());
//                return new ArrayList<>();
//            }
//        }
//
//        @Override
//        public boolean createOutboundRequest(String prodId, String clientId, int quantity, String wareId) {
//            try {
//                String outboundNumber = "OUT" + System.currentTimeMillis();
//                repository.createOutbound(outboundNumber, prodId, clientId, quantity, wareId);
//                return true;
//            } catch (SQLException e) {
//                System.out.println("출고 요청 실패: " + e.getMessage());
//                return false;
//            }
//        }
//
//        @Override
//        public ResultSet getOutboundList(boolean approved, int queryType, Date startDate, Date endDate, String prodId) {
//            String procedureCall = approved ? "{call sp_getApprovedOutbound(?, ?, ?, ?)}" : "{call sp_getUnapprovedOutbound(?, ?, ?, ?)}";
//            try {
//                return repository.getOutboundList(procedureCall, queryType, startDate, endDate, prodId);
//            } catch (SQLException e) {
//                System.out.println("출고 리스트 조회 실패: " + e.getMessage());
//                return null;
//            }
//        }
//
//        @Override
//        public boolean approveOutbound(String outboundNumber, String prodId) {
//            try {
//                repository.approveOutbound(outboundNumber, prodId);
//                return true;
//            } catch (SQLException e) {
//                System.out.println("승인 처리 실패: " + e.getMessage());
//                return false;
//            }
//        }
//
//        @Override
//        public boolean rejectOutbound(String outboundNumber, String prodId) {
//            try {
//                repository.rejectOutbound(outboundNumber, prodId);
//                return true;
//            } catch (SQLException e) {
//                System.out.println("거부 처리 실패: " + e.getMessage());
//                return false;
//            }
//        }
//
//        @Override
//        public ResultSet getUnapprovedOutboundByClient(String clientId) {
//            try {
//                return repository.getUnapprovedOutboundByClient(clientId);
//            } catch (SQLException e) {
//                System.out.println("미승인 내역 조회 실패: " + e.getMessage());
//                return null;
//            }
//        }
//    }
//
//    // 4. Controller 계층 인터페이스
//    public interface IShippingController {
//        void start();
//    }
//
//    // 4. Controller 계층 구현체
//    public static class ShippingController implements IShippingController {
//        private IShippingService service;
//        private Scanner scanner;
//
//        public ShippingController() {
//            service = new ShippingService();
//            scanner = new Scanner(System.in);
//        }
//
//        @Override
//        public void start() {
//            while (true) {
//                System.out.println("========== 출고관리 시스템 ==========");
//                System.out.println("1. 총관리자");
//                System.out.println("2. 회원");
//                System.out.println("0. 종료");
//                System.out.print("역할 선택: ");
//                int role = scanner.nextInt();
//                scanner.nextLine(); // 개행 처리
//
//                if (role == 0) {
//                    System.out.println("시스템을 종료합니다.");
//                    break;
//                }
//                switch (role) {
//                    case 1:
//                        adminMenu();
//                        break;
//                    case 2:
//                        memberMenu();
//                        break;
//                    default:
//                        System.out.println("잘못된 선택입니다.");
//                }
//            }
//            scanner.close();
//        }
//
//        // 회원 메뉴
//        private void memberMenu() {
//            while (true) {
//                System.out.println("\n----- 회원 메뉴 -----");
//                System.out.println("1. 출고 요청");
//                System.out.println("2. 출고여부 조회");
//                System.out.println("0. 상위 메뉴로");
//                System.out.print("선택: ");
//                int choice = scanner.nextInt();
//                scanner.nextLine();
//
//                if (choice == 0) break;
//
//                switch (choice) {
//                    case 1:
//                        processShippingRequest();
//                        break;
//                    case 2:
//                        processShippingStatusQuery();
//                        break;
//                    default:
//                        System.out.println("잘못된 선택입니다.");
//                }
//            }
//        }
//
//        // 총관리자 메뉴
//        private void adminMenu() {
//            while (true) {
//                System.out.println("\n----- 총관리자 메뉴 -----");
//                System.out.println("1. 미승인 출고 요청 조회 및 처리");
//                System.out.println("0. 상위 메뉴로");
//                System.out.print("선택: ");
//                int choice = scanner.nextInt();
//                scanner.nextLine();
//
//                if (choice == 0) break;
//
//                if (choice == 1) {
//                    processAdminApproval();
//                } else {
//                    System.out.println("잘못된 선택입니다.");
//                }
//            }
//        }
//
//        // 회원: 출고 요청 처리
//        private void processShippingRequest() {
//            System.out.print("출고할 상품의 ID를 입력하세요: ");
//            String prodId = scanner.nextLine();
//
//            List<Warehouse> warehouses = service.getAvailableWarehouses(prodId);
//            if (warehouses.isEmpty()) {
//                System.out.println("해당 상품을 보유한 창고가 없습니다. 출고 요청을 종료합니다.");
//                return;
//            }
//
//            System.out.println("\n== 사용 가능한 창고 리스트 ==");
//            for (Warehouse w : warehouses) {
//                System.out.println("창고ID: " + w.getWareId()
//                        + ", 이름: " + w.getWareName()
//                        + ", 위치: " + w.getWareLocation()
//                        + ", 재고: " + w.getQuantity());
//            }
//
//            System.out.print("출고할 창고의 ID를 입력하세요: ");
//            String wareId = scanner.nextLine();
//            System.out.print("출고할 상품 수량을 입력하세요: ");
//            int quantity = scanner.nextInt();
//            scanner.nextLine();
//
//            System.out.print("출고 요청을 진행하시겠습니까? ('yes' 입력): ");
//            String confirm = scanner.nextLine();
//            if (!confirm.equalsIgnoreCase("yes")) {
//                System.out.println("출고 요청이 취소되었습니다.");
//                return;
//            }
//
//            System.out.print("회원 ID를 입력하세요: ");
//            String clientId = scanner.nextLine();
//
//            if (service.createOutboundRequest(prodId, clientId, quantity, wareId)) {
//                System.out.println("출고 요청 성공!");
//            } else {
//                System.out.println("출고 요청 실패!");
//            }
//        }
//
//        // 회원: 출고 상태 조회 처리
//        private void processShippingStatusQuery() {
//            System.out.println("\n== 출고 상태 조회 ==");
//            System.out.println("1. 출고 승인 리스트 조회");
//            System.out.println("2. 출고 미승인 리스트 조회");
//            System.out.print("리스트 선택: ");
//            int listChoice = scanner.nextInt();
//            scanner.nextLine();
//
//            System.out.println("\n조회 방법:");
//            System.out.println("1. 전체 조회");
//            System.out.println("2. 특정 기간 조회 (예: 2024-02-01 ~ 2024-03-02)");
//            System.out.println("3. 특정 상품 조회");
//            System.out.print("선택: ");
//            int queryType = scanner.nextInt();
//            scanner.nextLine();
//
//            Date startDate = null, endDate = null;
//            String prodId = null;
//            if (queryType == 2) {
//                System.out.print("시작 날짜 (yyyy-mm-dd): ");
//                startDate = Date.valueOf(scanner.nextLine());
//                System.out.print("종료 날짜 (yyyy-mm-dd): ");
//                endDate = Date.valueOf(scanner.nextLine());
//            } else if (queryType == 3) {
//                System.out.print("조회할 상품 ID 입력: ");
//                prodId = scanner.nextLine();
//            }
//
//            boolean approved = (listChoice == 1);
//            ResultSet rs = service.getOutboundList(approved, queryType, startDate, endDate, prodId);
//
//            try {
//                System.out.println("\n== 조회 결과 ==");
//                while (rs != null && rs.next()) {
//                    System.out.println("OutboundNumber: " + rs.getString("outbound_number")
//                            + ", ProdID: " + rs.getString("prod_id")
//                            + ", ClientID: " + rs.getString("client_id")
//                            + ", Quantity: " + rs.getInt("quantity")
//                            + ", Status: " + rs.getInt("status")
//                            + ", RequestDate: " + rs.getDate("req_outbound_day")
//                            + ", WarehouseID: " + rs.getString("ware_id"));
//                }
//                if (rs != null) rs.getStatement().close();
//            } catch (SQLException e) {
//                System.out.println("조회 실패: " + e.getMessage());
//            }
//        }
//
//        // 총관리자: 미승인 출고 요청 조회 및 처리
//        private void processAdminApproval() {
//            System.out.print("검색할 회사(Client ID)를 입력하세요: ");
//            String clientId = scanner.nextLine();
//            ResultSet rs = service.getUnapprovedOutboundByClient(clientId);
//
//            try {
//                int index = 1;
//                List<String[]> outboundList = new ArrayList<>();
//                System.out.println("\n== 미승인 출고 내역 ==");
//                while (rs != null && rs.next()) {
//                    String outboundNumber = rs.getString("outbound_number");
//                    String prodId = rs.getString("prod_id");
//                    int quantity = rs.getInt("quantity");
//                    int status = rs.getInt("status");
//                    Date reqDate = rs.getDate("req_outbound_day");
//                    String wareId = rs.getString("ware_id");
//                    System.out.println(index + ". OutboundNumber: " + outboundNumber
//                            + ", ProdID: " + prodId
//                            + ", Quantity: " + quantity
//                            + ", Status: " + status
//                            + ", RequestDate: " + reqDate
//                            + ", WarehouseID: " + wareId);
//                    outboundList.add(new String[]{outboundNumber, prodId});
//                    index++;
//                }
//                if (rs != null) rs.getStatement().close();
//
//                if (outboundList.isEmpty()) {
//                    System.out.println("미승인 출고 내역이 없습니다.");
//                    return;
//                }
//
//                System.out.print("처리할 항목 번호를 선택하세요 (0 입력 시 메뉴로): ");
//                int selection = scanner.nextInt();
//                scanner.nextLine();
//                if (selection == 0 || selection > outboundList.size()) {
//                    System.out.println("메뉴로 돌아갑니다.");
//                    return;
//                }
//
//                String[] selected = outboundList.get(selection - 1);
//                String outboundNumber = selected[0];
//                String prodId = selected[1];
//
//                System.out.println("\n선택한 요청 처리:");
//                System.out.println("1. 승인 완료");
//                System.out.println("2. 승인 거부");
//                System.out.println("3. 메뉴로 돌아가기");
//                System.out.print("선택: ");
//                int action = scanner.nextInt();
//                scanner.nextLine();
//
//                if (action == 1) {
//                    if (service.approveOutbound(outboundNumber, prodId)) {
//                        System.out.println("출고 승인 완료되었습니다.");
//                    }
//                } else if (action == 2) {
//                    if (service.rejectOutbound(outboundNumber, prodId)) {
//                        System.out.println("출고 승인 거부되었습니다.");
//                    }
//                } else {
//                    System.out.println("메뉴로 돌아갑니다.");
//                }
//
//            } catch (SQLException e) {
//                System.out.println("관리자 승인 처리 실패: " + e.getMessage());
//            }
//        }
//    }
//}
//
//
