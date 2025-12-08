-- 添加专家信息审核权限
-- 确保专家信息管理的按钮权限已创建并分配给管理员角色

-- 1. 添加专家查询权限按钮
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, remark)
VALUES (2011, '专家查询', 2010, 1, '#', '', 'F', '0', '1', 'system:expert:list', '#', 'admin', NOW(), '')
ON DUPLICATE KEY UPDATE menu_name=VALUES(menu_name);

-- 2. 添加专家审核权限按钮
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, remark)
VALUES (2012, '专家审核', 2010, 2, '#', '', 'F', '0', '1', 'system:expert:audit', '#', 'admin', NOW(), '')
ON DUPLICATE KEY UPDATE menu_name=VALUES(menu_name);

-- 3. 添加专家删除权限按钮
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, remark)
VALUES (2013, '专家删除', 2010, 3, '#', '', 'F', '0', '1', 'system:expert:remove', '#', 'admin', NOW(), '')
ON DUPLICATE KEY UPDATE menu_name=VALUES(menu_name);

-- 4. 为管理员角色（role_id=1）分配这些权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, 2011
WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id = 1 AND menu_id = 2011);

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, 2012
WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id = 1 AND menu_id = 2012);

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, 2013
WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id = 1 AND menu_id = 2013);

-- 5. 验证
SELECT 'Menu buttons:' as info;
SELECT menu_id, menu_name, perms FROM sys_menu WHERE parent_id = 2010 ORDER BY order_num;

SELECT 'Admin role permissions:' as info;
SELECT role_id, menu_id FROM sys_role_menu WHERE role_id = 1 AND menu_id IN (2011, 2012, 2013);

