create table shipments(
    id int auto_increment primary key,
    product_name varchar(255) not null,
    quantity int not null,
    destination varchar(255) not null
);

use mypra5;
drop procedure HelloWorld;
DELIMITER //
CREATE PROCEDURE HelloWorld()
BEGIN
    SELECT 'Hello, World!';
END //
DELIMITER ;

CALL HelloWorld();
drop procedure GetAllEmployees;
DELIMITER //
CREATE PROCEDURE GetAllEmployees(in a varchar(50))
BEGIN
    SELECT * FROM employees where role = a;
END //
DELIMITER ;

CALL GetAllEmployees('Manager');
select * from employees where role = 'Manager';

