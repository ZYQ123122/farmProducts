-- ----------------------------
-- 若依系统基础表 - SQL Server 版本
-- 包含系统启动必需的基础表
-- ----------------------------

USE ry;
GO

-- ----------------------------
-- 1、参数配置表 sys_config
-- ----------------------------
IF OBJECT_ID('sys_config', 'U') IS NOT NULL
    DROP TABLE sys_config;
GO

CREATE TABLE sys_config (
  config_id      INT             NOT NULL IDENTITY(1,1),    -- 参数主键
  config_name    VARCHAR(100)    NULL DEFAULT '',           -- 参数名称
  config_key     VARCHAR(100)    NULL DEFAULT '',           -- 参数键名
  config_value   VARCHAR(500)    NULL DEFAULT '',           -- 参数键值
  config_type     CHAR(1)         NULL DEFAULT 'N',          -- 系统内置（Y是 N否）
  create_by      VARCHAR(64)     NULL DEFAULT '',           -- 创建者
  create_time    DATETIME        NULL DEFAULT GETDATE(),    -- 创建时间
  update_by      VARCHAR(64)     NULL DEFAULT '',           -- 更新者
  update_time    DATETIME        NULL DEFAULT GETDATE(),    -- 更新时间
  remark         VARCHAR(500)    NULL DEFAULT NULL,         -- 备注
  PRIMARY KEY (config_id)
);
GO

-- 创建触发器自动更新 update_time
CREATE TRIGGER trg_sys_config_updated_at
ON sys_config
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE sys_config
    SET update_time = GETDATE()
    FROM sys_config sc
    INNER JOIN inserted i ON sc.config_id = i.config_id;
END;
GO

-- 插入基础配置数据
INSERT INTO sys_config (config_name, config_key, config_value, config_type, create_by, remark) VALUES
('主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow'),
('用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '初始化密码 123456'),
('主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '深色主题theme-dark，浅色主题theme-light'),
('账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', '是否开启验证码功能（true开启，false关闭）'),
('账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'true', 'Y', 'admin', '是否开启注册用户功能（true开启，false关闭）');
GO

PRINT 'sys_config 表创建完成！';
GO

-- ----------------------------
-- 2、字典类型表 sys_dict_type
-- ----------------------------
IF OBJECT_ID('sys_dict_type', 'U') IS NOT NULL
    DROP TABLE sys_dict_type;
GO

CREATE TABLE sys_dict_type (
  dict_id      INT             NOT NULL IDENTITY(1,1),    -- 字典主键
  dict_name    VARCHAR(100)    NULL DEFAULT '',           -- 字典名称
  dict_type    VARCHAR(100)    NULL DEFAULT '',           -- 字典类型
  status       CHAR(1)         NULL DEFAULT '0',          -- 状态（0正常 1停用）
  create_by    VARCHAR(64)     NULL DEFAULT '',           -- 创建者
  create_time  DATETIME        NULL DEFAULT GETDATE(),    -- 创建时间
  update_by    VARCHAR(64)     NULL DEFAULT '',           -- 更新者
  update_time  DATETIME        NULL DEFAULT GETDATE(),    -- 更新时间
  remark       VARCHAR(500)    NULL DEFAULT NULL,         -- 备注
  PRIMARY KEY (dict_id),
  CONSTRAINT uk_dict_type UNIQUE (dict_type)
);
GO

-- 创建触发器自动更新 update_time
CREATE TRIGGER trg_sys_dict_type_updated_at
ON sys_dict_type
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE sys_dict_type
    SET update_time = GETDATE()
    FROM sys_dict_type sdt
    INNER JOIN inserted i ON sdt.dict_id = i.dict_id;
END;
GO

PRINT 'sys_dict_type 表创建完成！';
GO

-- ----------------------------
-- 3、字典数据表 sys_dict_data
-- ----------------------------
IF OBJECT_ID('sys_dict_data', 'U') IS NOT NULL
    DROP TABLE sys_dict_data;
GO

CREATE TABLE sys_dict_data (
  dict_code    INT             NOT NULL IDENTITY(1,1),    -- 字典编码
  dict_sort    INT             NULL DEFAULT 0,             -- 字典排序
  dict_label   VARCHAR(100)    NULL DEFAULT '',           -- 字典标签
  dict_value   VARCHAR(100)    NULL DEFAULT '',           -- 字典键值
  dict_type    VARCHAR(100)    NULL DEFAULT '',           -- 字典类型
  css_class    VARCHAR(100)    NULL DEFAULT NULL,          -- 样式属性（其他样式扩展）
  list_class   VARCHAR(100)    NULL DEFAULT NULL,          -- 表格字典样式
  is_default   CHAR(1)         NULL DEFAULT 'N',          -- 是否默认（Y是 N否）
  status       CHAR(1)         NULL DEFAULT '0',          -- 状态（0正常 1停用）
  create_by    VARCHAR(64)     NULL DEFAULT '',           -- 创建者
  create_time  DATETIME        NULL DEFAULT GETDATE(),    -- 创建时间
  update_by    VARCHAR(64)     NULL DEFAULT '',           -- 更新者
  update_time  DATETIME        NULL DEFAULT GETDATE(),    -- 更新时间
  remark       VARCHAR(500)    NULL DEFAULT NULL,         -- 备注
  PRIMARY KEY (dict_code)
);
GO

-- 创建触发器自动更新 update_time
CREATE TRIGGER trg_sys_dict_data_updated_at
ON sys_dict_data
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE sys_dict_data
    SET update_time = GETDATE()
    FROM sys_dict_data sdd
    INNER JOIN inserted i ON sdd.dict_code = i.dict_code;
END;
GO

PRINT 'sys_dict_data 表创建完成！';
GO

-- ----------------------------
-- 4、定时任务调度表 sys_job
-- ----------------------------
IF OBJECT_ID('sys_job', 'U') IS NOT NULL
    DROP TABLE sys_job;
GO

CREATE TABLE sys_job (
  job_id          INT             NOT NULL IDENTITY(1,1),    -- 任务ID
  job_name        VARCHAR(64)     NOT NULL DEFAULT '',       -- 任务名称
  job_group       VARCHAR(64)     NOT NULL DEFAULT '',       -- 任务组名
  invoke_target   VARCHAR(500)    NOT NULL DEFAULT '',       -- 调用目标字符串
  cron_expression VARCHAR(255)    NULL DEFAULT '',           -- cron执行表达式
  misfire_policy  VARCHAR(20)     NULL DEFAULT '3',          -- 计划策略（0默认 1立即触发执行 2触发一次执行 3不触发立即执行）
  concurrent      CHAR(1)         NULL DEFAULT '1',          -- 是否并发执行（0允许 1禁止）
  status          CHAR(1)         NULL DEFAULT '0',          -- 任务状态（0正常 1暂停）
  create_by       VARCHAR(64)     NULL DEFAULT '',           -- 创建者
  create_time     DATETIME        NULL DEFAULT GETDATE(),    -- 创建时间
  update_by       VARCHAR(64)     NULL DEFAULT '',           -- 更新者
  update_time     DATETIME        NULL DEFAULT GETDATE(),    -- 更新时间
  remark          VARCHAR(500)    NULL DEFAULT '',           -- 备注信息
  PRIMARY KEY (job_id)
);
GO

-- 创建触发器自动更新 update_time
CREATE TRIGGER trg_sys_job_updated_at
ON sys_job
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE sys_job
    SET update_time = GETDATE()
    FROM sys_job sj
    INNER JOIN inserted i ON sj.job_id = i.job_id;
END;
GO

PRINT 'sys_job 表创建完成！';
GO

-- ----------------------------
-- 5、定时任务调度日志表 sys_job_log
-- ----------------------------
IF OBJECT_ID('sys_job_log', 'U') IS NOT NULL
    DROP TABLE sys_job_log;
GO

CREATE TABLE sys_job_log (
  job_log_id      BIGINT          NOT NULL IDENTITY(1,1),    -- 任务日志ID
  job_name        VARCHAR(64)     NOT NULL DEFAULT '',       -- 任务名称
  job_group       VARCHAR(64)     NOT NULL DEFAULT '',       -- 任务组名
  invoke_target   VARCHAR(500)    NOT NULL DEFAULT '',       -- 调用目标字符串
  job_message     VARCHAR(500)    NULL DEFAULT NULL,         -- 日志信息
  status          CHAR(1)         NULL DEFAULT '0',          -- 执行状态（0正常 1失败）
  exception_info  VARCHAR(2000)   NULL DEFAULT '',           -- 异常信息
  create_time     DATETIME        NULL DEFAULT GETDATE(),    -- 创建时间
  PRIMARY KEY (job_log_id)
);
GO

PRINT 'sys_job_log 表创建完成！';
GO

PRINT '所有基础表创建完成！';
GO

