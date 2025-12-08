-- ----------------------------
-- 专家信息管理菜单配置
-- ----------------------------

-- 二级菜单：专家信息管理（放在系统管理下）
INSERT INTO sys_menu VALUES('2010', '专家信息管理', '1', '11', '/system/expert', '', 'C', '0', '1', 'system:expert:view', 'fa fa-user-md', 'admin', sysdate(), '', NULL, '专家信息管理菜单');

-- 专家信息管理按钮权限
INSERT INTO sys_menu VALUES('2011', '专家查询', '2010', '1', '#', '', 'F', '0', '1', 'system:expert:list', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2012', '专家审核', '2010', '2', '#', '', 'F', '0', '1', 'system:expert:audit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2013', '专家删除', '2010', '3', '#', '', 'F', '0', '1', 'system:expert:remove', '#', 'admin', sysdate(), '', NULL, '');

