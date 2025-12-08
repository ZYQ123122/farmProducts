-- ----------------------------
-- 专家咨询模块菜单配置
-- ----------------------------

-- 二级菜单：专家咨询（放在系统管理下）
insert into sys_menu values('2000', '专家咨询', '1', '10', '/system/consultation', '', 'C', '0', '1', 'system:consultation:view', 'fa fa-comments', 'admin', sysdate(), '', null, '专家咨询菜单');

-- 专家咨询按钮权限
insert into sys_menu values('2001', '咨询查询', '2000', '1', '#', '', 'F', '0', '1', 'system:consultation:list', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2002', '咨询新增', '2000', '2', '#', '', 'F', '0', '1', 'system:consultation:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2003', '咨询修改', '2000', '3', '#', '', 'F', '0', '1', 'system:consultation:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2004', '咨询删除', '2000', '4', '#', '', 'F', '0', '1', 'system:consultation:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2005', '咨询回复', '2000', '5', '#', '', 'F', '0', '1', 'system:consultation:reply', '#', 'admin', sysdate(), '', null, '');

