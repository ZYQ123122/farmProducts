-- ----------------------------
-- 更新用户登录凭据（用户名和密码）- SQL Server 版本
-- 密码加密方式：MD5(username + password)
-- ----------------------------

USE ry;
GO

-- ----------------------------
-- 方法1：使用 SQL Server 的 HASHBYTES 函数计算 MD5
-- 注意：SQL Server 的 HASHBYTES 返回二进制，需要转换为十六进制字符串
-- ----------------------------

-- 更新管理员用户（ID: 1 或 username='admin'）
-- 用户名：admin，密码：admin123
-- MD5('admin' + 'admin123') = MD5('adminadmin123')
UPDATE sys_user 
SET 
    username = 'admin',
    password_hash = LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', 'admin' + 'admin123'), 2)),
    updated_at = GETDATE()
WHERE username = 'admin' OR id = 1;
GO

-- 更新农户用户（ID: 100）
-- 用户名：1234，密码：123456
-- MD5('1234' + '123456') = MD5('1234123456')
UPDATE sys_user 
SET 
    username = '1234',
    password_hash = LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', '1234' + '123456'), 2)),
    updated_at = GETDATE()
WHERE id = 100;
GO

-- 更新买家用户（ID: 101）
-- 用户名：buyer1，密码：buyer1234
-- MD5('buyer1' + 'buyer1234') = MD5('buyer1buyer1234')
UPDATE sys_user 
SET 
    username = 'buyer1',
    password_hash = LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', 'buyer1' + 'buyer1234'), 2)),
    updated_at = GETDATE()
WHERE id = 101;
GO

-- ----------------------------
-- 验证更新结果
-- ----------------------------
SELECT 
    id,
    username,
    password_hash,
    role,
    name,
    is_verified,
    created_at,
    updated_at
FROM sys_user
WHERE id IN (100, 101);
GO

-- ----------------------------
-- 如果需要修改为其他用户名和密码，请按以下格式修改：
-- 
-- 示例：将农户用户名改为 'farmer001'，密码改为 'newpass123'
-- UPDATE sys_user 
-- SET 
--     username = 'farmer001',
--     password_hash = LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', 'farmer001' + 'newpass123'), 2)),
--     updated_at = GETDATE()
-- WHERE id = 100;
-- GO
-- 
-- 示例：将买家用户名改为 'buyer001'，密码改为 'newpass456'
-- UPDATE sys_user 
-- SET 
--     username = 'buyer001',
--     password_hash = LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', 'buyer001' + 'newpass456'), 2)),
--     updated_at = GETDATE()
-- WHERE id = 101;
-- GO
-- ----------------------------

PRINT '用户凭据更新完成！';
PRINT '农户登录：用户名=1234，密码=123456';
PRINT '买家登录：用户名=buyer1，密码=buyer1234';
GO

