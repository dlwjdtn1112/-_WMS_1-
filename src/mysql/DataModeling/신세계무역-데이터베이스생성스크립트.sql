#######################################################
# 과정명 : 자바백엔드개발자 과정
# 실습 DATABASE명: 신세계무역
# 작성일:2025-03-05
#######################################################
GRANT ALL PRIVILEGES ON `신세계무역`.* TO 'mypra5'@'localhost';


DROP DATABASE IF EXISTS 신세계무역;

CREATE DATABASE 신세계무역 DEFAULT CHARSET  utf8mb4 COLLATE  utf8mb4_general_ci;

USE 신세계무역;

CREATE TABLE 부서(
  부서번호 CHAR(2) PRIMARY KEY,
  부서명 VARCHAR(20)
 ) DEFAULT CHARSET=utf8mb4;



CREATE TABLE 사원(
  사원번호 CHAR(3) PRIMARY KEY,
  이름 VARCHAR(20),
  영문이름 VARCHAR(20),
  직위 VARCHAR(10),
  성별 CHAR(2),
  생일 DATE,
  입사일 DATE,
  주소 VARCHAR(50),
  도시 VARCHAR(20),
  지역 VARCHAR(20),
  집전화 VARCHAR(20),
  상사번호 CHAR(3),
  부서번호 CHAR(2)
  ) DEFAULT CHARSET=utf8mb4;
  
  


CREATE TABLE 고객(
   고객번호 CHAR(5) PRIMARY KEY,
   고객회사명 VARCHAR(30),
   담당자명 VARCHAR(20),
   담당자직위 VARCHAR(20),
   주소 VARCHAR(50),
   도시 VARCHAR(20),
   지역 VARCHAR(20),
   전화번호 VARCHAR(20),
   마일리지 INT
  ) DEFAULT CHARSET=utf8mb4;



CREATE TABLE 제품(
  제품번호 INT PRIMARY KEY,
  제품명 VARCHAR(50),
  포장단위 VARCHAR(30),
  단가 INT,
  재고 INT
  ) DEFAULT CHARSET=utf8mb4;




CREATE TABLE 주문(
  주문번호 CHAR(5) PRIMARY KEY,
  고객번호 CHAR(5),
  사원번호 CHAR(3),
  주문일 DATE,
  요청일 DATE,
  발송일 DATE
  ) DEFAULT CHARSET=utf8mb4;




CREATE TABLE 주문세부(
  주문번호 CHAR(5),
  제품번호 INT,
  단가 INT,
  주문수량 INT,
  할인율 FLOAT,
  PRIMARY KEY(주문번호, 제품번호)
 ) DEFAULT CHARSET=utf8mb4;




CREATE TABLE 마일리지등급(
  등급명 CHAR(1) PRIMARY KEY,
  하한마일리지 INT,
  상한마일리지 INT
  ) DEFAULT CHARSET=utf8mb4;

select * from 마일리지등급;
select * from 주문세부;

select * from 제품;

select * from 주문;

select * from 주문세부;

select * from 제품 where 제품명  like '%주스%' and  단가 > 4999 and 단가 < 10001;

select * from 제품 where 제품명  like '%주스%';

select * from 제품 where 제품번호 in(1,2,4,7,11,20);

select * from 고객 where 담당자명 like '%정%' ;

select 주문번호,sum(주문수량) as 주문수량합, sum(단가) as 주문단가합 from 주문세부 group by 제품번호;

select 제품번호,sum(단가) from 주문세부 group by 주문번호;

select * from 사원;

select 이름,직위,count(*) from 사원 group by 직위;

select * from 사원 group by 직위;
select * from 제품 where 제품명 = '아이스크림';
select 제품명 from 제품  where 단가 = 12300;

select 제품명 from 제품 where 단가 = (select max(단가) from 제품);
select count(*) from 제품 where 단가 = 12300;
select count(제품명) as '단가가 가장 비싼 제품 수'  from 제품 where 단가 = (select max(단가) from 제품);


select * from 고객;

drop  table 영화;
create table 영화(
    영화번호 char(5) primary key ,
    타이틀 varchar(100) not null ,
    장르 varchar(20) check ( 장르 in('코미디','드라마','다큐','SF','액션','기타') ),
    배우 varchar(100) not null ,
    감독 varchar(50) not null ,
    제작사 varchar(50) not null ,
    개봉일 DATE,
    등록일 DATE default CURRENT_DATE
         );
select * from 고객;

# DELIMITER $$
# create procedure callrealMadridupp5(in user_id int)
# begin
#     select * from realMadrid where player_id > user_id;
# end $$
# DELIMITER ;

drop procedure  call1;
DELIMITER $$
create procedure  call1(in user_name varchar(50))
begin
    select * from 고객 where 담당자명 = user_name;
end $$
DELIMITER ;


call call1('이두원');

select * from 제품;

SELECT
    제품번호,
    제품명,
    재고,
    CASE
        WHEN 재고 >= 100 THEN '과다재고'
        WHEN 재고 >= 10 THEN '적정'
        ELSE '재고부족'
        END AS 재고구분
FROM 제품;

select 제품번호,제품명,재고 ,
       case
           when 재고 >= 100 then '재고가 많음'
           when 재고 >= 10 then '적절하다'
           else '너무 적다'

           end as 재고구분


       from 제품;
select * from 사원;
select 이름,부서번호,직위,입사일,datediff(curdate(),입사일) as '압사일 수' , datediff(curdate(),입사일)  / 12 as '입사일 개월 수' from 사원 where datediff(curdate(),입사일) /12 > 40;

select datediff(curdate(),'2025-03-05');

select count(고객번호) from 주문 where  year(주문일) = '2021' group by 고객번호 order by count(고객번호) desc limit  3;

select * from 주문 where month(주문일) = '6';
select * from 주문;

select * from 사원;

select count(고객번호) from 주문 group by 고객번호;

select * from 주문 where 주문일 = '2020-04-09';
select count(도시) from 고객 group by 도시;

create table account (name varchar(30),balance int
                     );

insert into account values ('박지성',100000);
insert into account values ('김연아',100000);

start transaction;
update account set balance = balance - 1000000 where name = '박지성';
update account set balance = balance + 1000000 where name = '김연아';
commit;

select * from account;



select * from 고객;
select count(*) from 주문 where 고객번호 in (select 고객번호 from 고객 where 도시 = '서울특별시');
select * from 고객;
insert into 고객 (고객번호,담당자명,고객회사명,도시) values ('ZZZAA','한동욱','자유트레이딩','서울특별시');
select * from 고객 where 고객번호 = 'ZZZAA';

update 고객 set 도시 = '부산광역시'  where 고객번호 = 'ZZZAA';
update 고객 set 담당자직위 = '대표이사'  where 고객번호 = 'ZZZAA';
update 고객 set 마일리지 = (select avg(마일리지) from 고객)  where 고객번호 = 'ZZZAA';

UPDATE 고객
SET 마일리지 = (SELECT AVG(마일리지) FROM 고객)
WHERE 고객번호 = 'ZZZAA';
delete from 고객 where 고객번호  = 'ZZZAA';
-- CREATE VIEW VIP고객 AS
 -- SELECT 고객번호, 이름, 등급, 마일리지
-- FROM 고객
-- WHERE 등급 = 'VIP';
create view 상위3명  as select 고객번호,고객회사명,담당자명,담당자직위 from 고객;
select * from 상위3명;

select * from 고객 where 담당자명 like '%정%';

select 단가,재고,제품번호,제품명,(단가*재고) as 재고금액 from 제품 order by 재고금액 desc limit 10;
















