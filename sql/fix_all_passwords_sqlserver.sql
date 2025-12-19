-- ----------------------------
-- 修复所有用户密码 - SQL Server 版本
-- 密码加密方式：MD5(username + password)
-- 此脚本会修复管理员、农户和买家的密码
-- ----------------------------

USE ry;
GO

PRINT '开始修复用户密码...';
GO

-- ----------------------------
-- 1. 修复管理员账号（ID: 1 或 username='admin'）
-- 用户名：admin，密码：admin123
-- MD5('admin' + 'admin123') = MD5('adminadmin123')
-- ----------------------------
IF EXISTS (SELECT 1 FROM sys_user WHERE username = 'admin')
BEGIN
    UPDATE sys_user 
    SET 
        password_hash = LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', 'admin' + 'admin123'), 2)),
        updated_at = GETDATE()
    WHERE username = 'admin';
    
    PRINT '管理员密码已修复：用户名=admin，密码=admin123';
END
ELSE
BEGIN
    -- 如果管理员不存在，则创建
    IF NOT EXISTS (SELECT 1 FROM sys_user WHERE id = 1)
    BEGIN
        SET IDENTITY_INSERT sys_user ON;
        INSERT INTO sys_user (id, username, password_hash, role, name, email, phone, is_verified, created_at, updated_at)
        VALUES (1, 'admin', LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', 'admin' + 'admin123'), 2)), 'admin', '管理员', 'admin@example.com', '15888888888', 1, GETDATE(), GETDATE());
        SET IDENTITY_INSERT sys_user OFF;
        PRINT '管理员账号已创建：用户名=admin，密码=admin123';
    END
    ELSE
    BEGIN
        -- 如果ID=1存在但username不是admin，更新它
        UPDATE sys_user 
        SET 
            username = 'admin',
            password_hash = LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', 'admin' + 'admin123'), 2)),
            role = 'admin',
            name = '管理员',
            email = 'admin@example.com',
            phone = '15888888888',
            is_verified = 1,
            updated_at = GETDATE()
        WHERE id = 1;
        PRINT '管理员账号已修复：用户名=admin，密码=admin123';
    END
END
GO

-- ----------------------------
-- 2. 修复农户账号（ID: 100 或 username='1234'）
-- 用户名：1234，密码：123456
-- MD5('1234' + '123456') = MD5('1234123456')
-- ----------------------------
IF EXISTS (SELECT 1 FROM sys_user WHERE username = '1234' OR id = 100)
BEGIN
    UPDATE sys_user 
    SET 
        username = '1234',
        password_hash = LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', '1234' + '123456'), 2)),
        role = 'farmer',
        name = ISNULL(name, '测试农户'),
        email = ISNULL(email, 'farmer@example.com'),
        phone = ISNULL(phone, '13888888888'),
        is_verified = 1,
        updated_at = GETDATE()
    WHERE username = '1234' OR id = 100;
    
    PRINT '农户密码已修复：用户名=1234，密码=123456';
END
ELSE
BEGIN
    -- 如果农户不存在，则创建
    IF NOT EXISTS (SELECT 1 FROM sys_user WHERE id = 100)
    BEGIN
        SET IDENTITY_INSERT sys_user ON;
        INSERT INTO sys_user (id, username, password_hash, role, name, email, phone, is_verified, created_at, updated_at)
        VALUES (100, '1234', LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', '1234' + '123456'), 2)), 'farmer', '测试农户', 'farmer@example.com', '13888888888', 1, GETDATE(), GETDATE());
        SET IDENTITY_INSERT sys_user OFF;
        PRINT '农户账号已创建：用户名=1234，密码=123456';
    END
END
GO

-- ----------------------------
-- 3. 修复买家账号（ID: 101 或 username='buyer1'）
-- 用户名：buyer1，密码：buyer1234
-- MD5('buyer1' + 'buyer1234') = MD5('buyer1buyer1234')
-- ----------------------------
IF EXISTS (SELECT 1 FROM sys_user WHERE username = 'buyer1' OR id = 101)
BEGIN
    UPDATE sys_user 
    SET 
        username = 'buyer1',
        password_hash = LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', 'buyer1' + 'buyer1234'), 2)),
        role = 'buyer',
        name = ISNULL(name, '测试买家'),
        email = ISNULL(email, 'buyer@example.com'),
        phone = ISNULL(phone, '13988888888'),
        is_verified = 1,
        updated_at = GETDATE()
    WHERE username = 'buyer1' OR id = 101;
    
    PRINT '买家密码已修复：用户名=buyer1，密码=buyer1234';
END
ELSE
BEGIN
    -- 如果买家不存在，则创建
    IF NOT EXISTS (SELECT 1 FROM sys_user WHERE id = 101)
    BEGIN
        SET IDENTITY_INSERT sys_user ON;
        INSERT INTO sys_user (id, username, password_hash, role, name, email, phone, is_verified, created_at, updated_at)
        VALUES (101, 'buyer1', LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', 'buyer1' + 'buyer1234'), 2)), 'buyer', '测试买家', 'buyer@example.com', '13988888888', 1, GETDATE(), GETDATE());
        SET IDENTITY_INSERT sys_user OFF;
        PRINT '买家账号已创建：用户名=buyer1，密码=buyer1234';
    END
END
GO

-- ----------------------------
-- 验证修复结果
-- ----------------------------
PRINT '';
PRINT '========== 用户信息验证 ==========';
SELECT 
    id,
    username,
    password_hash,
    role,
    name,
    email,
    phone,
    is_verified,
    created_at,
    updated_at
FROM sys_user
WHERE username IN ('admin', '1234', 'buyer1') OR id IN (1, 100, 101)
ORDER BY id;
GO

-- ----------------------------
-- 显示登录信息
-- ----------------------------
PRINT '';
PRINT '========== 登录信息 ==========';
PRINT '管理员：用户名=admin，密码=admin123';
PRINT '农户：用户名=1234，密码=123456';
PRINT '买家：用户名=buyer1，密码=buyer1234';
PRINT '';
PRINT '所有用户密码已修复完成！';
GO








