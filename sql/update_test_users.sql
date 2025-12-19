-- 更新测试用户数据
-- 农户ID：100，用户名：1234，密码：123456
-- 买家ID：101，用户名：buyer1，密码：buyer1234

-- 更新或插入农户用户（ID: 100）
INSERT INTO sys_user (id, username, password_hash, role, name, email, phone, is_verified) 
VALUES(100, '1234', LOWER(MD5(CONCAT('1234', '123456'))), 'farmer', '农户用户', 'farmer@example.com', '13800001000', 1)
ON DUPLICATE KEY UPDATE 
    username = '1234',
    password_hash = LOWER(MD5(CONCAT('1234', '123456'))),
    role = 'farmer',
    name = '农户用户',
    email = 'farmer@example.com',
    phone = '13800001000',
    is_verified = 1;

-- 更新或插入买家用户（ID: 101）
INSERT INTO sys_user (id, username, password_hash, role, name, email, phone, is_verified) 
VALUES(101, 'buyer1', LOWER(MD5(CONCAT('buyer1', 'buyer1234'))), 'buyer', '买家用户', 'buyer@example.com', '13900001001', 1)
ON DUPLICATE KEY UPDATE 
    username = 'buyer1',
    password_hash = LOWER(MD5(CONCAT('buyer1', 'buyer1234'))),
    role = 'buyer',
    name = '买家用户',
    email = 'buyer@example.com',
    phone = '13900001001',
    is_verified = 1;

-- 确保管理员用户存在（用户名：admin，密码：admin123）
INSERT INTO sys_user (id, username, password_hash, role, name, email, phone, is_verified) 
VALUES(1, 'admin', LOWER(MD5(CONCAT('admin', 'admin123'))), 'admin', '管理员', 'admin@example.com', '15888888888', 1)
ON DUPLICATE KEY UPDATE 
    username = 'admin',
    password_hash = LOWER(MD5(CONCAT('admin', 'admin123'))),
    role = 'admin',
    name = '管理员',
    email = 'admin@example.com',
    phone = '15888888888',
    is_verified = 1;














