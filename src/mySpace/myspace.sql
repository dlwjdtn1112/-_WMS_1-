DELIMITER //

CREATE PROCEDURE HelloWorld()
BEGIN
SELECT 'Hello, World!';
END //

DELIMITER ;
CALL HelloWorld();
