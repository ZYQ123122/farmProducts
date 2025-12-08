-- ----------------------------
-- 专家工作台菜单配置（专家端左侧菜单）
-- ----------------------------

-- 一级菜单：专家工作台（顶层菜单）
INSERT INTO sys_menu VALUES
('2100', '专家工作台', '0', '5', '#', '', 'M', '0', '1', '', 'fa fa-user-md', 'admin', sysdate(), '', NULL, '专家工作台根菜单');

-- 子菜单：咨询回复（跳转到 /manager/consultation）
INSERT INTO sys_menu VALUES
('2101', '咨询回复', '2100', '1', '/manager/consultation', '', 'C', '0', '1', '', 'fa fa-comments', 'admin', sysdate(), '', NULL, '专家回复咨询');

-- 子菜单：知识库管理（预留，对应 /manager/knowledge）
INSERT INTO sys_menu VALUES
('2102', '知识库管理', '2100', '2', '/manager/knowledge', '', 'C', '0', '1', '', 'fa fa-book', 'admin', sysdate(), '', NULL, '专家知识库管理');

-- 子菜单：我的专家资料（预留，对应 /manager/profile）
INSERT INTO sys_menu VALUES
('2103', '我的专家资料', '2100', '3', '/manager/profile', '', 'C', '0', '1', '', 'fa fa-id-card', 'admin', sysdate(), '', NULL, '专家个人资料');

-- 子菜单：预约处理（预留，对应 /manager/appointment）
INSERT INTO sys_menu VALUES
('2104', '预约处理', '2100', '4', '/manager/appointment', '', 'C', '0', '1', '', 'fa fa-calendar', 'admin', sysdate(), '', NULL, '专家处理预约');


