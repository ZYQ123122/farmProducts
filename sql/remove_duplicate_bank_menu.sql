-- 删除重复的银行工作台菜单（保留2200系列，删除2106系列）

-- 1. 删除2106系列的菜单权限关联
DELETE FROM sys_role_menu WHERE menu_id IN (2106, 2107, 2108);

-- 2. 删除2106系列的菜单
DELETE FROM sys_menu WHERE menu_id IN (2106, 2107, 2108);

-- 3. 确保2200系列菜单配置正确
-- 融资审批：默认显示待审批的申请（submitted和in_review状态）
-- 融资管理：显示所有申请
UPDATE sys_menu SET url = '/guest/finance/list?defaultStatus=submitted', remark = '融资审批-待审批申请' WHERE menu_id = 2201;
UPDATE sys_menu SET url = '/guest/finance/list', remark = '融资管理-所有申请' WHERE menu_id = 2202;

-- 4. 验证结果
SELECT '最终菜单配置' as info, menu_id, menu_name, parent_id, url FROM sys_menu 
WHERE menu_id BETWEEN 2200 AND 2203
ORDER BY menu_id;

