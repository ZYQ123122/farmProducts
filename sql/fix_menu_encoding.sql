-- ----------------------------
-- 修复菜单名称编码问题
-- ----------------------------

-- 修复通知公告
UPDATE sys_menu SET menu_name = '通知公告' WHERE menu_id = 107;

-- 修复专家咨询
UPDATE sys_menu SET menu_name = '专家咨询' WHERE menu_id = 2000;

-- 修复专家信息管理
UPDATE sys_menu SET menu_name = '专家信息管理' WHERE menu_id = 2010;

