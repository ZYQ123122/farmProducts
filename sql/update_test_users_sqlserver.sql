-- SQL Server 版本：更新测试用户数据
-- 农户ID：100，用户名：1234，密码：123456
-- 买家ID：101，用户名：buyer1，密码：buyer1234

-- SQL Server 使用 MERGE 语句来更新或插入数据
-- 更新或插入农户用户（ID: 100）
MERGE sys_user AS target
USING (SELECT 100 AS id, '1234' AS username, 'farmer' AS role) AS source
ON target.id = source.id
WHEN MATCHED THEN
    UPDATE SET 
        username = '1234',
        password_hash = LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', '1234' + '123456'), 2)),
        role = 'farmer',
        name = '农户用户',
        email = 'farmer@example.com',
        phone = '13800001000',
        is_verified = 1
WHEN NOT MATCHED THEN
    INSERT (id, username, password_hash, role, name, email, phone, is_verified)
    VALUES (100, '1234', LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', '1234' + '123456'), 2)), 'farmer', '农户用户', 'farmer@example.com', '13800001000', 1);

-- 更新或插入买家用户（ID: 101）
MERGE sys_user AS target
USING (SELECT 101 AS id, 'buyer1' AS username, 'buyer' AS role) AS source
ON target.id = source.id
WHEN MATCHED THEN
    UPDATE SET 
        username = 'buyer1',
        password_hash = LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', 'buyer1' + 'buyer1234'), 2)),
        role = 'buyer',
        name = '买家用户',
        email = 'buyer@example.com',
        phone = '13900001001',
        is_verified = 1
WHEN NOT MATCHED THEN
    INSERT (id, username, password_hash, role, name, email, phone, is_verified)
    VALUES (101, 'buyer1', LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', 'buyer1' + 'buyer1234'), 2)), 'buyer', '买家用户', 'buyer@example.com', '13900001001', 1);

-- 确保管理员用户存在（用户名：admin，密码：admin123）
MERGE sys_user AS target
USING (SELECT 1 AS id, 'admin' AS username, 'admin' AS role) AS source
ON target.id = source.id
WHEN MATCHED THEN
    UPDATE SET 
        username = 'admin',
        password_hash = LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', 'admin' + 'admin123'), 2)),
        role = 'admin',
        name = '管理员',
        email = 'admin@example.com',
        phone = '15888888888',
        is_verified = 1
WHEN NOT MATCHED THEN
    INSERT (id, username, password_hash, role, name, email, phone, is_verified)
    VALUES (1, 'admin', LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', 'admin' + 'admin123'), 2)), 'admin', '管理员', 'admin@example.com', '15888888888', 1);















