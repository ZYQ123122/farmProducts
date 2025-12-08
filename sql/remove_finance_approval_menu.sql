-- 删除银行工作台下的"融资审批"菜单

-- 1. 删除角色菜单关联
DELETE FROM sys_role_menu WHERE menu_id = 2201;

-- 2. 删除菜单项
DELETE FROM sys_menu WHERE menu_id = 2201;

-- 3. 验证删除结果
SELECT 'Remaining menus under 银行工作台:' as info;
SELECT menu_id, menu_name, parent_id, url FROM sys_menu WHERE parent_id = 2200;

