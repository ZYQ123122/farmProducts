-- 修复银行端菜单显示问题
-- 删除可能存在的旧菜单
DELETE FROM sys_role_menu WHERE menu_id >= 2200 AND menu_id <= 2203;
DELETE FROM sys_menu WHERE menu_id >= 2200 AND menu_id <= 2203;

-- 获取银行角色ID
SET @bank_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'bank' LIMIT 1);

-- 如果银行角色不存在，创建它
INSERT INTO sys_role (role_id, role_name, role_key, role_sort, data_scope, status, del_flag, create_by, create_time, remark)
SELECT 3, '银行', 'bank', 3, '1', '0', '0', 'admin', NOW(), '银行角色'
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE role_key = 'bank');

SET @bank_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'bank' LIMIT 1);

-- 创建银行工作台一级菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, remark)
VALUES (2200, '银行工作台', 0, 6, '#', '', 'M', '0', '1', '', 'fa fa-bank', 'admin', NOW(), '银行工作台目录');

-- 创建融资审批菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, remark)
VALUES (2201, '融资审批', 2200, 1, '/guest/finance/list', '', 'C', '0', '1', 'bank:finance:list', 'fa fa-check-square-o', 'admin', NOW(), '融资审批菜单');

-- 创建融资管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, remark)
VALUES (2202, '融资管理', 2200, 2, '/guest/finance/list', '', 'C', '0', '1', 'bank:finance:manage', 'fa fa-list', 'admin', NOW(), '融资管理菜单');

-- 创建产品管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, remark)
VALUES (2203, '产品管理', 2200, 3, '/guest/product/manage', '', 'C', '0', '1', 'bank:product:manage', 'fa fa-cubes', 'admin', NOW(), '产品管理菜单');

-- 为银行角色分配菜单权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (@bank_role_id, 2200);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (@bank_role_id, 2201);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (@bank_role_id, 2202);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (@bank_role_id, 2203);

-- 确保银行用户有银行角色
DELETE FROM sys_user_role WHERE user_id IN (102, 103);
INSERT INTO sys_user_role (user_id, role_id) VALUES (102, @bank_role_id);
INSERT INTO sys_user_role (user_id, role_id) VALUES (103, @bank_role_id);

