-- 1. 기존 테이블 삭제 (외래키 제약 조건 순서 주의)
use mypra5;
DROP TABLE IF EXISTS outbound;
DROP TABLE IF EXISTS inventory;
DROP TABLE IF EXISTS inbound;
DROP TABLE IF EXISTS warehouse;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS warehouse_area;
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS `user`;

-- 2. 테이블 생성

CREATE TABLE `user` (
                        `client_id`	VARCHAR(255)	NOT NULL,
                        `user_name`	VARCHAR(20)	NOT NULL,
                        `user_phone`	VARCHAR(15)	NOT NULL	COMMENT '하이픈 없는형태 + 국제번호',
                        `user_email`	VARCHAR(30)	NOT NULL,
                        `user_adress`	VARCHAR(30)	NOT NULL,
                        `business_number`	INT	NOT NULL,
                        `user_enterday`	DATE	NOT NULL	COMMENT '회원가입 승인이 된 날짜 기준',
                        `user_id`	VARCHAR(15)	NOT NULL,
                        `user_pw`	VARCHAR(15)	NOT NULL,
                        `user_status`	TINYINT(1)	NOT NULL	COMMENT '미승인/승인으로 나뉜다',
                        `user_ware_size`	DECIMAL(10,2)	NULL,
                        `user_ware_use`	DECIMAL(10,2)	NULL
);

CREATE TABLE `admin` (
                         `admin_number`	VARCHAR(255)	NOT NULL,
                         `admin_role`	VARCHAR(10)	NOT NULL	COMMENT '총관리자,창고관리자',
                         `admin_name`	VARCHAR(20)	NOT NULL,
                         `admin_email`	VARCHAR(30)	NOT NULL,
                         `admin_enterday`	VARCHAR(30)	NOT NULL	COMMENT '관리자 입사일',
                         `admin_adress`	VARCHAR(30)	NULL,
                         `admin_phone`	VARCHAR(15)	NOT NULL,
                         `admin_id`	VARCHAR(15)	NOT NULL	COMMENT '관리자 로그인 ID',
                         `admin_pw`	VARCHAR(15)	NOT NULL	COMMENT '관리자 로그인 PW'
);

CREATE TABLE `outbound` (
                            `outbound_number`	VARCHAR(30)	NOT NULL,
                            `prod_id`	VARCHAR(10)	NOT NULL,
                            `client_id`	VARCHAR(10)	NOT NULL,
                            `quantity`	INT	NOT NULL,
                            `status`	INT	NOT NULL	DEFAULT 0 COMMENT '0은 사용자가 출고 요청을 하기전, 1은 총관리자가 승인 거부, 2는 출고 완료',
                            `req_outbound_day`	DATE	NULL,
                            `ware_id`	VARCHAR(10)	NOT NULL
);

CREATE TABLE `inventory` (
                             `prod_id`	VARCHAR(10)	NOT NULL,
                             `client_id`	VARCHAR(10)	NOT NULL,
                             `quantity`	INT	NOT NULL	DEFAULT 0,
                             `ware_id`	VARCHAR(10)	NOT NULL,
                             `prod_id2`	VARCHAR(10)	NOT NULL	COMMENT '카테고리 + 생산년도 + 4자리 일련번호'
);

CREATE TABLE `product` (
                           `prod_id`	VARCHAR(10)	NOT NULL	COMMENT '카테고리 + 생산년도 + 4자리 일련번호',
                           `brand`	VARCHAR(20)	NOT NULL,
                           `prod_name`	VARCHAR(30)	NOT NULL,
                           `prod_price`	INTEGER	NULL,
                           `prod_code`	INTEGER	NULL,
                           `prod_category`	VARCHAR(20)	NOT NULL	COMMENT 'ex) CPU,VGA,RAM,MainBoard',
                           `prod_size`	DECIMAL(10,2)	NOT NULL	COMMENT 'cm^3 단위'
);

CREATE TABLE `warehouse_area` (
                                  `ware_id`	VARCHAR(10)	NOT NULL,
                                  `ware_name`	VARCHAR(20)	NOT NULL,
                                  `ware_location`	VARCHAR(10)	NOT NULL,
                                  `ware_size`	INT	NOT NULL	DEFAULT 0
);

CREATE TABLE `inbound` (
                           `inbound_number`	VARCHAR(30)	NOT NULL,
                           `prod_id`	VARCHAR(10)	NOT NULL,
                           `client_id`	VARCHAR(10)	NOT NULL,
                           `quantity`	INT	NOT NULL,
                           `Inboud_status`	INT	NOT NULL	DEFAULT 0,
                           `req_inbound_day`	DATE	NULL,
                           `ware_id`	VARCHAR(10)	NOT NULL
);

CREATE TABLE `warehouse` (
                             `ware_id`	VARCHAR(10)	NOT NULL,
                             `client_id`	VARCHAR(10)	NOT NULL,
                             `prod_id`	VARCHAR(10)	NOT NULL,
                             `quantity`	INT	NOT NULL	DEFAULT 0,
                             `last_inbound_day`	DATE	NOT NULL,
                             `last_outbount_day`	DATE	NULL
);

-- 3. 제약 조건 추가

ALTER TABLE `user` ADD CONSTRAINT `PK_USER` PRIMARY KEY (`client_id`);
ALTER TABLE `admin` ADD CONSTRAINT `PK_ADMIN` PRIMARY KEY (`admin_number`);
ALTER TABLE `outbound` ADD CONSTRAINT `PK_OUTBOUND` PRIMARY KEY (`outbound_number`, `prod_id`);
ALTER TABLE `inventory` ADD CONSTRAINT `PK_INVENTORY` PRIMARY KEY (`prod_id`);
ALTER TABLE `product` ADD CONSTRAINT `PK_PRODUCT` PRIMARY KEY (`prod_id`);
ALTER TABLE `warehouse_area` ADD CONSTRAINT `PK_WAREHOUSE_AREA` PRIMARY KEY (`ware_id`);
ALTER TABLE `inbound` ADD CONSTRAINT `PK_INBOUND` PRIMARY KEY (`inbound_number`, `prod_id`);
ALTER TABLE `warehouse` ADD CONSTRAINT `PK_WAREHOUSE` PRIMARY KEY (`ware_id`);

ALTER TABLE `outbound` ADD CONSTRAINT `FK_product_TO_outbound_1` FOREIGN KEY (`prod_id`) REFERENCES `product` (`prod_id`);
ALTER TABLE `inbound` ADD CONSTRAINT `FK_product_TO_inbound_1` FOREIGN KEY (`prod_id`) REFERENCES `product` (`prod_id`);
ALTER TABLE `warehouse` ADD CONSTRAINT `FK_warehouse_area_TO_warehouse_1` FOREIGN KEY (`ware_id`) REFERENCES `warehouse_area` (`ware_id`);

-- 4. 각 테이블에 샘플 데이터 삽입

-- [user 테이블]
INSERT INTO `user`
(client_id, user_name, user_phone, user_email, user_adress, business_number, user_enterday, user_id, user_pw, user_status, user_ware_size, user_ware_use)
VALUES
    ('C001', '홍길동', '01012345678', 'hong@example.com', '서울', 123456, '2024-01-01', 'hong01', 'pass123', 1, 100.00, 50.00),
    ('C002', '김철수', '01087654321', 'kim@example.com', '부산', 654321, '2024-01-05', 'kim02', 'pass456', 0, 200.00, 80.00);

-- [admin 테이블]
INSERT INTO `admin`
(admin_number, admin_role, admin_name, admin_email, admin_enterday, admin_adress, admin_phone, admin_id, admin_pw)
VALUES
    ('A001', '총관리자', '이순신', 'lee@example.com', '2023-10-01', '서울', '01098765432', 'admin1', 'adminpass'),
    ('A002', '창고관리자', '강감찬', 'kang@example.com', '2023-11-01', '대구', '01012349876', 'admin2', 'adminpass2');

-- [product 테이블]
INSERT INTO `product`
(prod_id, brand, prod_name, prod_price, prod_code, prod_category, prod_size)
VALUES
    ('P001', 'Intel', 'Core i7', 300, 101, 'CPU', 50.00),
    ('P002', 'NVIDIA', 'RTX 3080', 700, 202, 'VGA', 150.00),
    ('P003', 'Corsair', 'Vengeance 16GB', 80, 303, 'RAM', 20.00);

-- [warehouse_area 테이블]
INSERT INTO `warehouse_area`
(ware_id, ware_name, ware_location, ware_size)
VALUES
    ('W001', '창고1', '서울', 1000),
    ('W002', '창고2', '부산', 800);

-- [inbound 테이블]
INSERT INTO `inbound`
(inbound_number, prod_id, client_id, quantity, Inboud_status, req_inbound_day, ware_id)
VALUES
    ('INB001', 'P001', 'C001', 50, 0, '2024-01-10', 'W001'),
    ('INB002', 'P002', 'C002', 30, 0, '2024-01-12', 'W002');

-- [warehouse 테이블]
INSERT INTO `warehouse`
(ware_id, client_id, prod_id, quantity, last_inbound_day, last_outbount_day)
VALUES
    ('W001', 'C001', 'P001', 50, '2024-01-10', NULL),
    ('W002', 'C002', 'P002', 30, '2024-01-12', NULL);

-- [inventory 테이블]
INSERT INTO `inventory`
(prod_id, client_id, quantity, ware_id, prod_id2)
VALUES
    ('P001', 'C001', 50, 'W001', 'CPU2023001'),
    ('P002', 'C002', 30, 'W002', 'VGA2023002');

-- [outbound 테이블]
-- 일반적으로 출고 요청 전에는 데이터가 없으나, 예제로 하나의 출고 기록(승인 완료 상태)을 삽입합니다.
INSERT INTO `outbound`
(outbound_number, prod_id, client_id, quantity, status, req_outbound_day, ware_id)
VALUES
    ('OUT0001', 'P001', 'C001', 10, 2, '2024-03-15', 'W001');

