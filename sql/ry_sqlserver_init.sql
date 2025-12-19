-- ----------------------------
-- SQL Server 版本初始化脚本
-- 适用于若依系统
-- ----------------------------

USE ry;
GO

-- ----------------------------
-- 1、用户信息表（新结构）
-- ----------------------------
IF OBJECT_ID('sys_user', 'U') IS NOT NULL
    DROP TABLE sys_user;
GO

CREATE TABLE sys_user (
  id                BIGINT          NOT NULL IDENTITY(1,1),    -- 用户唯一标识
  username          VARCHAR(50)     NOT NULL,                   -- 用户名（手机号或邮箱）
  password_hash     VARCHAR(255)    NOT NULL,                   -- 密码哈希值
  role              VARCHAR(20)     NOT NULL,                   -- 用户角色（农户/专家/银行/买家/管理员）
  name              VARCHAR(100)    NULL,                       -- 用户姓名
  contact           VARCHAR(100)    NULL,                       -- 联系方式
  email             VARCHAR(100)    NULL,                       -- 邮箱（可选）
  phone             VARCHAR(20)     NULL,                       -- 手机号（可选）
  is_verified       TINYINT         DEFAULT 0,                  -- 是否通过资质验证（0:未通过,1:已通过）
  created_at        DATETIME        DEFAULT GETDATE(),          -- 创建时间
  updated_at        DATETIME        DEFAULT GETDATE(),          -- 更新时间
  PRIMARY KEY (id),
  CONSTRAINT uk_username UNIQUE (username),
  CONSTRAINT uk_email UNIQUE (email),
  CONSTRAINT uk_phone UNIQUE (phone),
  CONSTRAINT chk_role CHECK (role IN ('farmer','expert','bank','buyer','admin'))
);
GO

-- 创建触发器自动更新 updated_at
CREATE TRIGGER trg_sys_user_updated_at
ON sys_user
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE sys_user
    SET updated_at = GETDATE()
    FROM sys_user u
    INNER JOIN inserted i ON u.id = i.id;
END;
GO

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
-- 管理员用户（密码：admin123）
INSERT INTO sys_user (username, password_hash, role, name, email, phone, is_verified) 
VALUES('admin', '29c67a30398638269fe600f73a054934', 'admin', '管理员', 'admin@example.com', '15888888888', 1);

-- 农户用户（密码：123456）
INSERT INTO sys_user (username, password_hash, role, name, email, phone, is_verified) 
VALUES('1234', 'e10adc3949ba59abbe56e057f20f883e', 'farmer', '测试农户', 'farmer@example.com', '13888888888', 1);

-- 买家用户（密码：buyer1234）
INSERT INTO sys_user (username, password_hash, role, name, email, phone, is_verified) 
VALUES('buyer1', 'buyer1234_hash', 'buyer', '测试买家', 'buyer@example.com', '13988888888', 1);
GO

-- ----------------------------
-- 2、农户商品信息表
-- ----------------------------
IF OBJECT_ID('farmer_product', 'U') IS NOT NULL
    DROP TABLE farmer_product;
GO

CREATE TABLE farmer_product (
  id                BIGINT          NOT NULL IDENTITY(1,1),    -- 商品唯一标识
  farmer_id         BIGINT          NOT NULL,                   -- 农户用户ID
  name              VARCHAR(100)    NOT NULL,                   -- 商品名称
  price             DECIMAL(10,2)   NOT NULL,                   -- 商品价格
  stock             INT             NOT NULL,                   -- 库存数量
  description       NVARCHAR(MAX)   NULL,                       -- 商品描述
  status            VARCHAR(20)     NOT NULL DEFAULT 'off_shelf', -- 商品状态（上架/下架）
  created_at        DATETIME        NOT NULL DEFAULT GETDATE(), -- 创建时间
  updated_at        DATETIME        NOT NULL DEFAULT GETDATE(), -- 更新时间
  is_deleted        TINYINT         NOT NULL DEFAULT 0,         -- 软删除（0未删除 1已删除）
  PRIMARY KEY (id),
  CONSTRAINT fk_farmer_product_user FOREIGN KEY (farmer_id) REFERENCES sys_user (id),
  CONSTRAINT chk_farmer_product_status CHECK (status IN ('on_shelf','off_shelf'))
);
GO

CREATE INDEX idx_farmer_product_farmer ON farmer_product(farmer_id);
GO

-- 创建触发器自动更新 updated_at
CREATE TRIGGER trg_farmer_product_updated_at
ON farmer_product
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE farmer_product
    SET updated_at = GETDATE()
    FROM farmer_product fp
    INNER JOIN inserted i ON fp.id = i.id;
END;
GO

-- ----------------------------
-- 3、买家需求信息表
-- ----------------------------
IF OBJECT_ID('buyer_requirement', 'U') IS NOT NULL
    DROP TABLE buyer_requirement;
GO

CREATE TABLE buyer_requirement (
  id                BIGINT          NOT NULL IDENTITY(1,1),    -- 需求ID
  buyer_id          BIGINT          NOT NULL,                   -- 买家用户ID
  product_name      VARCHAR(100)    NOT NULL,                   -- 需求商品名称
  quantity          INT             NOT NULL,                   -- 需求数量
  specs             NVARCHAR(MAX)   NULL,                       -- 商品规格
  status            VARCHAR(20)     NOT NULL DEFAULT 'unsatisfied', -- 需求状态
  created_at        DATETIME        NOT NULL DEFAULT GETDATE(), -- 创建时间
  updated_at        DATETIME        NOT NULL DEFAULT GETDATE(), -- 更新时间
  PRIMARY KEY (id),
  CONSTRAINT fk_buyer_requirement_user FOREIGN KEY (buyer_id) REFERENCES sys_user (id),
  CONSTRAINT chk_buyer_requirement_status CHECK (status IN ('unsatisfied','responded'))
);
GO

CREATE INDEX idx_buyer_requirement_buyer ON buyer_requirement(buyer_id);
GO

-- 创建触发器自动更新 updated_at
CREATE TRIGGER trg_buyer_requirement_updated_at
ON buyer_requirement
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE buyer_requirement
    SET updated_at = GETDATE()
    FROM buyer_requirement br
    INNER JOIN inserted i ON br.id = i.id;
END;
GO

-- ----------------------------
-- 4、订单表
-- ----------------------------
IF OBJECT_ID('orders', 'U') IS NOT NULL
    DROP TABLE orders;
GO

CREATE TABLE orders (
  id                BIGINT          NOT NULL IDENTITY(1,1),    -- 订单唯一标识
  product_id        BIGINT          NOT NULL,                   -- 商品ID
  buyer_id          BIGINT          NOT NULL,                   -- 买家用户ID
  farmer_id         BIGINT          NOT NULL,                   -- 农户用户ID
  quantity          INT             NOT NULL,                   -- 购买数量
  total_price       DECIMAL(10,2)   NULL,                       -- 总价
  status            VARCHAR(20)     DEFAULT 'pending',          -- 订单状态
  created_at        DATETIME        DEFAULT GETDATE(),          -- 创建时间
  updated_at        DATETIME        DEFAULT GETDATE(),          -- 更新时间
  PRIMARY KEY (id),
  CONSTRAINT fk_orders_product FOREIGN KEY (product_id) REFERENCES farmer_product (id),
  CONSTRAINT fk_orders_buyer FOREIGN KEY (buyer_id) REFERENCES sys_user (id),
  CONSTRAINT fk_orders_farmer FOREIGN KEY (farmer_id) REFERENCES sys_user (id),
  CONSTRAINT chk_orders_status CHECK (status IN ('pending','confirmed','completed'))
);
GO

CREATE INDEX idx_orders_product_id ON orders(product_id);
CREATE INDEX idx_orders_buyer_id ON orders(buyer_id);
CREATE INDEX idx_orders_farmer_id ON orders(farmer_id);
GO

-- 创建触发器自动更新 updated_at
CREATE TRIGGER trg_orders_updated_at
ON orders
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE orders
    SET updated_at = GETDATE()
    FROM orders o
    INNER JOIN inserted i ON o.id = i.id;
END;
GO

PRINT '表结构创建完成！';
GO








