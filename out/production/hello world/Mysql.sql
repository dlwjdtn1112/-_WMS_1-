use mypra5;


drop table inbound;
create table inbound(-- 기존 입고 테이블
                        inbound_id int primary key  not null comment 'in+압고요청날짜(timestamp)+상품명',
                        product_id varchar(20) not null,
                        inbound_quantity int not null,
                        req_inbound_day varchar(20) not null,
                        warehouse_id varchar(20) not null,
                        status enum('미승인','승인 거부','승인 수락') not null ,
                        foreign key (warehouse_id) references warehouse(warehouse_id)

);
ALTER TABLE inbound
    MODIFY COLUMN inbound_id INT NOT NULL AUTO_INCREMENT;

desc inbound;
select * from inbound;
-- 새로운 입고 테이블
drop procedure pro_inbound_request;
DELIMITER $$

create procedure pro_inbound_request(in u_product_id varchar(20), -- 회원이 관리자에게 승인요청을 한다.
                                     in u_inbound_quantity int,in u_req_inbound_day varchar(20),in u_warehouse_id varchar(20)
)
BEGIN
    insert into inbound(product_id, inbound_quantity, req_inbound_day, warehouse_id)
    values (u_product_id,u_inbound_quantity,
            u_req_inbound_day,u_warehouse_id);

end ;
DELIMITER ;










desc outbound;
drop table outbound;
create table outbound(
                         outbound_id int primary key auto_increment comment 'Out+숫자',
                         product_id varchar(20),
                         outbound_quantity int,
                         req_outbound_day varchar(20),
                         status enum('미승인','승인 거부','승인 수락')  not null,
                         warehouse_id varchar(20),
                         foreign key (warehouse_id) references warehouse(warehouse_id)

);





create table product(
                        product_id varchar(20) primary key not null,
                        product_name varchar(20) not null,
                        company_id varchar(20) not null,
                        price int not null

);

create table warehouse(
                          warehouse_id varchar(20) primary key not null,
                          warehouse_location varchar(3),
                          warehouse_manager varchar(4)

);


create table inventory(
                          product_id varchar(20) not null,
                          product_name varchar(20) not null,
                          quantity int,
                          warehouse_id varchar(20) not null ,
                          warehouse_name varchar(10) not null,
                          primary key (warehouse_id, product_id),
                          foreign key (warehouse_id) references warehouse(warehouse_id),
                          foreign key (product_id) references product(product_id)

);



drop procedure pro_inbound_request;

desc inbound;
select * from inbound;




desc inbound;
desc inventory;
select * from inventory;
select * from inbound;


drop procedure pro_inbound_approve;
DELIMITER $$
create procedure pro_inbound_approve(in u_inbound_id int)

    -- 총관리자가 승인을 해주는 기능
BEGIN
    DECLARE a int;
    update inbound set status = '승인 수락' where inbound_id = u_inbound_id;


    -- INSERT INTO inventory (product_id, warehouse_id)
    -- SELECT product_id, warehouse_id
    -- FROM inbound
    -- WHERE inbound_id = u_inbound_id;
    SELECT inbound_quantity INTO a FROM inbound WHERE inbound_id = u_inbound_id;
    update inventory set quantity = quantity + a where product_id = (SELECT product_id FROM inbound WHERE inbound_id = u_inbound_id);





end ;
DELIMITER ;

DELIMITER $$
create procedure pro_inbound_reject(in u_inbound_id int)
BEGIN
    update inbound set status = '승인 거부' where inbound_id = u_inbound_id;

end ;
DELIMITER ;




insert into warehouse values ('Gonjiam1112','광주시','발베르데');
insert into warehouse values ('Daegu1112','대구시','호드리구');
ALTER TABLE inbound MODIFY inbound_id INT AUTO_INCREMENT;





-- 출고관련 프로시저
drop procedure pro_outbound_request;
DELIMITER $$
create procedure pro_outbound_request(in u_product_id varchar(20), -- 회원이 관리자에게 출고승인요청을 한다.
                                     in u_outbound_quantity int,in u_req_outbound_day varchar(20),in u_warehouse_id varchar(20)
)
BEGIN
    insert into outbound(outbound_id,product_id, outbound_quantity, req_outbound_day, warehouse_id)
    values (outbound_id,u_product_id,u_outbound_quantity,
            u_req_outbound_day,u_warehouse_id);

end ;
DELIMITER ;
-- ----------------------
drop procedure pro_outbound_approve;
DELIMITER $$
create procedure pro_outbound_approve(in u_outbound_id int)

    -- 총관리자가 승인을 해주는 기능
BEGIN
    DECLARE a int;
    update outbound set status = '승인 수락' where outbound_id = u_outbound_id;



    SELECT outbound_quantity INTO a FROM outbound WHERE outbound_id = u_outbound_id;
    update inventory set quantity = quantity - a where product_id = (SELECT product_id FROM outbound WHERE outbound_id = u_outbound_id);

end ;
DELIMITER ;

select * from outbound;
select * from inventory;
select * from inbound;
select * from warehouse;

desc outbound;
desc inbound;
desc inventory;
desc product;
desc warehouse;

-- 재고현황을 보는 프로시저
DELIMITER $$
create procedure pro_inventory_status()
begin
    select * from inventory;
end $$
DELIMITER ;

-- 창고 정보를 출력하는 프로시저

DELIMITER $$
create procedure pro_warehouse_status()
begin
    select * from warehouse;
end $$
DELIMITER ;

-- 테스트중
DELIMITER $$

CREATE PROCEDURE pro_inbound_approve1(IN u_inbound_id INT)
BEGIN
    DECLARE a INT;
    DECLARE v_product_id VARCHAR(20);
    DECLARE v_warehouse_id VARCHAR(20);

    -- 1. inbound_id에 해당하는 데이터를 조회하여 변수에 저장
    SELECT inbound_quantity, product_id, warehouse_id
    INTO a, v_product_id, v_warehouse_id
    FROM inbound
    WHERE inbound_id = u_inbound_id;

    -- 2. inbound 테이블의 status를 승인 상태로 변경
    UPDATE inbound
    SET status = '승인 수락'
    WHERE inbound_id = u_inbound_id;

    -- 3. inventory 테이블에 해당 상품이 있는지 확인
    IF EXISTS (SELECT 1 FROM inventory WHERE product_id = v_product_id AND warehouse_id = v_warehouse_id) THEN
        -- 존재하면 수량 업데이트
        UPDATE inventory
        SET quantity = quantity + a
        WHERE product_id = v_product_id AND warehouse_id = v_warehouse_id;
    ELSE
        -- 존재하지 않으면 새로운 상품을 추가
        INSERT INTO inventory (product_id, warehouse_id, quantity)
        VALUES (v_product_id, v_warehouse_id, a);
    END IF;

END $$

DELIMITER ;

select * from inventory;

delete from inbound where inbound_id = 21;
select * from inventory;