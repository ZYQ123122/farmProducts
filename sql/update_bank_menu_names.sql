-- 更新银行端菜单名称（修复编码问题）
UPDATE sys_menu SET menu_name = '银行工作台' WHERE menu_id = 2200;
UPDATE sys_menu SET menu_name = '融资审批' WHERE menu_id = 2201;
UPDATE sys_menu SET menu_name = '融资管理' WHERE menu_id = 2202;
UPDATE sys_menu SET menu_name = '产品管理' WHERE menu_id = 2203;

