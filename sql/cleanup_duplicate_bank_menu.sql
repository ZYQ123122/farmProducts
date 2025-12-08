-- 清理重复的银行工作台菜单
-- 查找所有银行工作台相关的菜单
SELECT 'Before cleanup' as status, menu_id, menu_name, parent_id, url FROM sys_menu 
WHERE menu_name LIKE '%银行工作台%' OR (parent_id = 0 AND menu_name LIKE '%银行%')
ORDER BY menu_id;

-- 删除所有可能的重复菜单（保留2200-2203）
DELETE FROM sys_role_menu WHERE menu_id IN (
    SELECT menu_id FROM (
        SELECT menu_id FROM sys_menu 
        WHERE (menu_name LIKE '%银行工作台%' OR menu_name LIKE '%融资审批%' OR menu_name LIKE '%融资管理%' OR menu_name LIKE '%产品管理%')
        AND menu_id NOT BETWEEN 2200 AND 2203
    ) AS temp
);

DELETE FROM sys_menu 
WHERE (menu_name LIKE '%银行工作台%' OR menu_name LIKE '%融资审批%' OR menu_name LIKE '%融资管理%' OR menu_name LIKE '%产品管理%')
AND menu_id NOT BETWEEN 2200 AND 2203;

-- 确保菜单配置正确
-- 融资审批应该指向审批列表（可以筛选待审批的）
-- 融资管理应该指向管理列表（所有申请）
UPDATE sys_menu SET url = '/guest/finance/list', remark = '融资审批菜单-显示待审批申请' WHERE menu_id = 2201;
UPDATE sys_menu SET url = '/guest/finance/list', remark = '融资管理菜单-显示所有申请' WHERE menu_id = 2202;

SELECT 'After cleanup' as status, menu_id, menu_name, parent_id, url FROM sys_menu 
WHERE menu_id BETWEEN 2200 AND 2203
ORDER BY menu_id;

