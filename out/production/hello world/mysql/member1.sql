create table member1(
                        mem_id   VARCHAR(20) PRIMARY KEY,
                        mem_name VARCHAR(50),
                        mem_pass VARCHAR(50),
                        mem_tel  VARCHAR(20),
                        mem_addr VARCHAR(100)
);

select * from member1;
-- insert member1 into values("irene0329","irene","0329","031","서울");

drop table member1;