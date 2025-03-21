create table accounts (
  ano       varchar(20)   primary key,
  owner     varchar(20)   not null,
  balance   numeric       not null
);

insert into accounts (ano, owner, balance) 
values ('111-111-1111', '신세계', 1000000);

insert into accounts (ano, owner, balance) 
values ('222-222-2222', '이마트', 0);


