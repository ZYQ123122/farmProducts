-- 银行端菜单初始化SQL脚本
-- 创建银行端菜单项并分配给银行角色

-- 1. 检查并创建银行角色（如果不存在）
INSERT INTO sys_role (role_id, role_name, role_key, role_sort, data_scope, status, del_flag, create_by, create_time, remark)
SELECT 3, '银行', 'bank', 3, '1', '0', '0', 'admin', NOW(), '银行角色'
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE role_key = 'bank');

-- 2. 创建银行工作台一级菜单（如果不存在）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, remark)
SELECT 2200, '银行工作台', 0, 6, '#', '', 'M', '0', '1', '', 'fa fa-bank', 'admin', NOW(), '银行工作台目录'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2200);

-- 3. 创建融资审批菜单（如果不存在）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, remark)
SELECT 2201, '融资审批', 2200, 1, '/guest/finance/list', '', 'C', '0', '1', 'bank:finance:list', 'fa fa-check-square-o', 'admin', NOW(), '融资审批菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2201);

-- 4. 创建融资管理菜单（如果不存在）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, remark)
SELECT 2202, '融资管理', 2200, 2, '/guest/finance/list', '', 'C', '0', '1', 'bank:finance:manage', 'fa fa-list', 'admin', NOW(), '融资管理菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2202);

-- 5. 创建产品管理菜单（如果不存在）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, remark)
SELECT 2203, '产品管理', 2200, 3, '/guest/product/manage', '', 'C', '0', '1', 'bank:product:manage', 'fa fa-cubes', 'admin', NOW(), '产品管理菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2203);

-- 6. 获取银行角色ID
SET @bank_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'bank' LIMIT 1);

-- 7. 为银行角色分配菜单权限
-- 银行工作台
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @bank_role_id, 2200
WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id = @bank_role_id AND menu_id = 2200);

-- 融资审批
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @bank_role_id, 2201
WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id = @bank_role_id AND menu_id = 2201);

-- 融资管理
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @bank_role_id, 2202
WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id = @bank_role_id AND menu_id = 2202);

-- 产品管理
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @bank_role_id, 2203
WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id = @bank_role_id AND menu_id = 2203);

-- 8. 为银行用户分配银行角色（如果还没有分配）
-- 先删除银行用户现有的角色（如果需要）
DELETE FROM sys_user_role WHERE user_id IN (102, 103);

-- 为银行用户分配银行角色
INSERT INTO sys_user_role (user_id, role_id)
SELECT 102, @bank_role_id
WHERE NOT EXISTS (SELECT 1 FROM sys_user_role WHERE user_id = 102 AND role_id = @bank_role_id);

INSERT INTO sys_user_role (user_id, role_id)
SELECT 103, @bank_role_id
WHERE NOT EXISTS (SELECT 1 FROM sys_user_role WHERE user_id = 103 AND role_id = @bank_role_id);

