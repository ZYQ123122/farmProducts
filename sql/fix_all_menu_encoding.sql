-- ----------------------------
-- 修复所有菜单名称编码问题（使用UTF-8十六进制编码）
-- ----------------------------

SET NAMES utf8mb4;

-- 一级菜单
UPDATE sys_menu SET menu_name = UNHEX('E7B3BBE7BB9FE7AEA1E79086') WHERE menu_id = 1;
UPDATE sys_menu SET menu_name = UNHEX('E7B3BBE7BB9FE79B91E68EA7') WHERE menu_id = 2;
UPDATE sys_menu SET menu_name = UNHEX('E7B3BBE7BB9FE5B7A5E585B7') WHERE menu_id = 3;
UPDATE sys_menu SET menu_name = UNHEX('E88BA5E4BE9DE5AE98E7BD91') WHERE menu_id = 4;

-- 系统管理二级菜单
UPDATE sys_menu SET menu_name = UNHEX('E794A8E688B7E7AEA1E79086') WHERE menu_id = 100;
UPDATE sys_menu SET menu_name = UNHEX('E8A792E889B2E7AEA1E79086') WHERE menu_id = 101;
UPDATE sys_menu SET menu_name = UNHEX('E88F9CE58D95E7AEA1E79086') WHERE menu_id = 102;
UPDATE sys_menu SET menu_name = UNHEX('E983A8E997A8E7AEA1E79086') WHERE menu_id = 103;
UPDATE sys_menu SET menu_name = UNHEX('E5B297E4BD8DE7AEA1E79086') WHERE menu_id = 104;
UPDATE sys_menu SET menu_name = UNHEX('E5AD97E585B8E7AEA1E79086') WHERE menu_id = 105;
UPDATE sys_menu SET menu_name = UNHEX('E58F82E695B0E8AEBEE7BDAE') WHERE menu_id = 106;
UPDATE sys_menu SET menu_name = UNHEX('E9809AE79FA5E585ACE5918A') WHERE menu_id = 107;
UPDATE sys_menu SET menu_name = UNHEX('E697A5E5BF97E7AEA1E79086') WHERE menu_id = 108;
UPDATE sys_menu SET menu_name = UNHEX('E4B893E5AEB6E592A8E8AFA2') WHERE menu_id = 2000;
UPDATE sys_menu SET menu_name = UNHEX('E4B893E5AEB6E4BFA1E681AFE7AEA1E79086') WHERE menu_id = 2010;

-- 系统监控二级菜单
UPDATE sys_menu SET menu_name = UNHEX('E59CA8E7BABFE794A8E688B7') WHERE menu_id = 109;
UPDATE sys_menu SET menu_name = UNHEX('E5AE9AE697B6E4BBBBE58AA1') WHERE menu_id = 110;
UPDATE sys_menu SET menu_name = UNHEX('E695B0E68DAEE79B91E68EA7') WHERE menu_id = 111;
UPDATE sys_menu SET menu_name = UNHEX('E69C8DE58AA1E79B91E68EA7') WHERE menu_id = 112;
UPDATE sys_menu SET menu_name = UNHEX('E7BC93E5AD98E79B91E68EA7') WHERE menu_id = 113;

-- 系统工具二级菜单
UPDATE sys_menu SET menu_name = UNHEX('E8A1A8E58D95E69E84E5BBBA') WHERE menu_id = 114;
UPDATE sys_menu SET menu_name = UNHEX('E4BBA3E7A081E7949FE68890') WHERE menu_id = 115;
UPDATE sys_menu SET menu_name = UNHEX('E7B3BBE7BB9FE68EA5E58FA3') WHERE menu_id = 116;

-- 三级菜单
UPDATE sys_menu SET menu_name = UNHEX('E6938DE4BD9CE697A5E5BF97') WHERE menu_id = 500;
UPDATE sys_menu SET menu_name = UNHEX('E799BBE5BD95E697A5E5BF97') WHERE menu_id = 501;

