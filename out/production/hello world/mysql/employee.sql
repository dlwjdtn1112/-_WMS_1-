CREATE TABLE employees (
                           eno VARCHAR(50) PRIMARY KEY,          -- 직원 ID, 고유해야 하므로 PRIMARY KEY로 설정
                           name VARCHAR(100) NOT NULL,             -- 직원 이름
                           role VARCHAR(50) NOT NULL,              -- 역할 (예: 'Staff', 'Manager', 'Secretary')
                           entryYear INT NOT NULL,                 -- 입사년도
                           entryMonth INT NOT NULL,                -- 입사월
                           entryDay INT NOT NULL,                  -- 입사일
                           secno VARCHAR(50) DEFAULT NULL          -- Manager 전용 추가 필드, Manager가 아닌 경우 NULL 허용
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
