-- 修复重复的银行工作台菜单问题
-- 1. 查找所有可能的重复菜单
SELECT '查找重复菜单' as step, menu_id, menu_name, parent_id, url FROM sys_menu 
WHERE (menu_name LIKE '%银行工作台%' OR menu_name LIKE '%融资审批%' OR menu_name LIKE '%融资管理%' OR menu_name LIKE '%产品管理%')
ORDER BY menu_id;

-- 2. 删除不在2200-2203范围内的重复菜单及其关联
DELETE FROM sys_role_menu WHERE menu_id IN (
    SELECT menu_id FROM (
        SELECT menu_id FROM sys_menu 
        WHERE (menu_name LIKE '%银行工作台%' OR menu_name LIKE '%融资审批%' OR menu_name LIKE '%融资管理%' OR menu_name LIKE '%产品管理%')
        AND menu_id NOT BETWEEN 2200 AND 2203
    ) AS temp_table
);

DELETE FROM sys_menu 
WHERE (menu_name LIKE '%银行工作台%' OR menu_name LIKE '%融资审批%' OR menu_name LIKE '%融资管理%' OR menu_name LIKE '%产品管理%')
AND menu_id NOT BETWEEN 2200 AND 2203;

-- 3. 修复菜单名称（确保编码正确）
UPDATE sys_menu SET menu_name = UNHEX('E993B6E8A18CE5B7A5E4BD9CE58FB0') WHERE menu_id = 2200;
UPDATE sys_menu SET menu_name = UNHEX('E89E8DE8B584E5AEA1E689B9') WHERE menu_id = 2201;
UPDATE sys_menu SET menu_name = UNHEX('E89E8DE8B584E7AEA1E79086') WHERE menu_id = 2202;
UPDATE sys_menu SET menu_name = UNHEX('E4BAA7E59381E7AEA1E79086') WHERE menu_id = 2203;

-- 4. 区分融资审批和融资管理的功能
-- 融资审批：默认显示待审批的申请（可以通过URL参数区分）
-- 融资管理：显示所有申请
-- 由于它们使用同一个页面，我们通过不同的URL来区分，前端可以根据URL显示不同的默认筛选
UPDATE sys_menu SET url = '/guest/finance/list?type=approval', remark = '融资审批-待审批申请' WHERE menu_id = 2201;
UPDATE sys_menu SET url = '/guest/finance/list?type=manage', remark = '融资管理-所有申请' WHERE menu_id = 2202;

-- 5. 验证结果
SELECT '修复后菜单' as step, menu_id, menu_name, parent_id, url FROM sys_menu 
WHERE menu_id BETWEEN 2200 AND 2203
ORDER BY menu_id;

