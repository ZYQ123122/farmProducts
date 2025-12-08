-- ----------------------------
-- 为专家用户分配咨询回复权限
-- ----------------------------

-- 1. 确保权限菜单已存在（menu_id=2005 对应 system:consultation:reply）
-- 如果不存在，先执行 expert_consultation_menu.sql

-- 2. 找到所有专家用户所属的角色，并为他们分配回复权限
-- 专家用户：在 expert_info 表中且 audit_status='1' 的用户

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT DISTINCT sur.role_id, 2005
FROM sys_user_role sur
INNER JOIN expert_info ei ON sur.user_id = ei.user_id
WHERE ei.audit_status = '1'  -- 只给已审核通过的专家分配权限
  AND ei.status = '0'        -- 只给正常状态的专家分配权限
  AND NOT EXISTS (
    SELECT 1 
    FROM sys_role_menu srm 
    WHERE srm.role_id = sur.role_id 
      AND srm.menu_id = 2005
  );

-- 3. 查询结果，确认权限已分配
SELECT 
    r.role_id,
    r.role_name,
    r.role_key,
    COUNT(DISTINCT ei.user_id) AS expert_count
FROM sys_role r
INNER JOIN sys_role_menu rm ON r.role_id = rm.role_id
INNER JOIN sys_user_role sur ON r.role_id = sur.role_id
INNER JOIN expert_info ei ON sur.user_id = ei.user_id
WHERE rm.menu_id = 2005
  AND ei.audit_status = '1'
  AND ei.status = '0'
GROUP BY r.role_id, r.role_name, r.role_key;

