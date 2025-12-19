-- Step 1: Disable foreign key checks
SET FOREIGN_KEY_CHECKS = 0;

-- Step 2: Drop tables first (this will automatically drop foreign key constraints)
-- Foreign keys will be dropped when we drop the tables in Step 4-5

-- Step 3: Drop all views
DROP VIEW IF EXISTS v_user_info;
DROP VIEW IF EXISTS v_user_role;
DROP VIEW IF EXISTS v_user_dept;
DROP VIEW IF EXISTS v_user_menu;

-- Step 4: Drop admin related tables
DROP TABLE IF EXISTS sys_role_dept;
DROP TABLE IF EXISTS sys_role_menu;
DROP TABLE IF EXISTS sys_user_post;
DROP TABLE IF EXISTS sys_user_role;
DROP TABLE IF EXISTS sys_post;
DROP TABLE IF EXISTS sys_role;
DROP TABLE IF EXISTS sys_dept;
DROP TABLE IF EXISTS sys_menu;
DROP TABLE IF EXISTS sys_dict_data;
DROP TABLE IF EXISTS sys_dict_type;
DROP TABLE IF EXISTS sys_notice;
DROP TABLE IF EXISTS sys_oper_log;
DROP TABLE IF EXISTS sys_user_online;

-- Step 5: Drop existing tables that will be recreated
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS buyer_requirement;
DROP TABLE IF EXISTS farmer_product;
DROP TABLE IF EXISTS sys_user;

-- Step 6: Create new user table
CREATE TABLE sys_user (
  id                BIGINT(20)      NOT NULL AUTO_INCREMENT,
  username          VARCHAR(50)     NOT NULL,
  password_hash     VARCHAR(255)    NOT NULL,
  role              ENUM('farmer','expert','bank','buyer','admin') NOT NULL,
  name              VARCHAR(100)    DEFAULT NULL,
  contact           VARCHAR(100)    DEFAULT NULL,
  email             VARCHAR(100)    DEFAULT NULL,
  phone             VARCHAR(20)     DEFAULT NULL,
  is_verified       TINYINT(1)      DEFAULT 0,
  created_at        DATETIME        DEFAULT CURRENT_TIMESTAMP,
  updated_at        DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_username (username),
  UNIQUE KEY uk_email (email),
  UNIQUE KEY uk_phone (phone)
) ENGINE=INNODB AUTO_INCREMENT=1;

-- Step 7: Insert default test data
-- Password encryption: MD5(username + password)
-- admin: MD5('admin' + 'admin123') = MD5('adminadmin123')
-- farmer1: MD5('farmer1' + '123456') = MD5('farmer1123456')
-- buyer1: MD5('buyer1' + '123456') = MD5('buyer1123456')
INSERT INTO sys_user (username, password_hash, role, name, email, phone, is_verified) 
VALUES('admin', LOWER(MD5(CONCAT('admin', 'admin123'))), 'admin', 'Admin', 'admin@example.com', '15888888888', 1);

INSERT INTO sys_user (username, password_hash, role, name, email, phone, is_verified) 
VALUES('farmer1', LOWER(MD5(CONCAT('farmer1', '123456'))), 'farmer', 'Test Farmer', 'farmer@example.com', '13888888888', 1);

INSERT INTO sys_user (username, password_hash, role, name, email, phone, is_verified) 
VALUES('buyer1', LOWER(MD5(CONCAT('buyer1', '123456'))), 'buyer', 'Test Buyer', 'buyer@example.com', '13988888888', 1);

-- Step 8: Create farmer product table
CREATE TABLE farmer_product (
  id                BIGINT(20)      NOT NULL AUTO_INCREMENT,
  farmer_id         BIGINT(20)      NOT NULL,
  name              VARCHAR(100)    NOT NULL,
  price             DECIMAL(10,2)   NOT NULL,
  stock             INT             NOT NULL,
  description       TEXT,
  status            ENUM('on_shelf','off_shelf') NOT NULL DEFAULT 'off_shelf',
  created_at        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted        TINYINT(1)      NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  KEY idx_farmer_product_farmer (farmer_id),
  CONSTRAINT fk_farmer_product_user FOREIGN KEY (farmer_id) REFERENCES sys_user (id) ON DELETE CASCADE
) ENGINE=INNODB;

-- Step 9: Create buyer requirement table
CREATE TABLE buyer_requirement (
  id                BIGINT(20)      NOT NULL AUTO_INCREMENT,
  buyer_id          BIGINT(20)      NOT NULL,
  product_name      VARCHAR(100)    NOT NULL,
  quantity          INT             NOT NULL,
  specs             TEXT,
  status            ENUM('unsatisfied','responded') NOT NULL DEFAULT 'unsatisfied',
  created_at        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_buyer_requirement_buyer (buyer_id),
  CONSTRAINT fk_buyer_requirement_user FOREIGN KEY (buyer_id) REFERENCES sys_user (id) ON DELETE CASCADE
) ENGINE=INNODB;

-- Step 10: Create orders table
CREATE TABLE orders (
  id                BIGINT(20)      NOT NULL AUTO_INCREMENT,
  product_id        BIGINT(20)      NOT NULL,
  buyer_id          BIGINT(20)      NOT NULL,
  farmer_id         BIGINT(20)      NOT NULL,
  quantity          INT             NOT NULL,
  total_price       DECIMAL(10,2)   DEFAULT NULL,
  status            ENUM('pending','confirmed','completed') DEFAULT 'pending',
  created_at        DATETIME        DEFAULT CURRENT_TIMESTAMP,
  updated_at        DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_orders_product_id (product_id),
  KEY idx_orders_buyer_id (buyer_id),
  KEY idx_orders_farmer_id (farmer_id),
  CONSTRAINT fk_orders_product FOREIGN KEY (product_id) REFERENCES farmer_product (id) ON DELETE CASCADE,
  CONSTRAINT fk_orders_buyer FOREIGN KEY (buyer_id) REFERENCES sys_user (id) ON DELETE CASCADE,
  CONSTRAINT fk_orders_farmer FOREIGN KEY (farmer_id) REFERENCES sys_user (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- Step 11: Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

