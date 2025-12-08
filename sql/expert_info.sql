-- ----------------------------
-- 专家信息表
-- ----------------------------
DROP TABLE IF EXISTS expert_info;
CREATE TABLE expert_info (
  id            BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '专家信息ID',
  user_id       BIGINT(20)    NOT NULL                COMMENT '关联用户ID',
  specialty     VARCHAR(100)                          COMMENT '专业领域',
  experience    VARCHAR(200)                          COMMENT '工作经验',
  description   TEXT                                  COMMENT '专家简介',
  expertise     TEXT                                  COMMENT '擅长方向（多个用逗号分隔）',
  achievements  TEXT                                  COMMENT '主要成就',
  avatar        VARCHAR(200)                          COMMENT '专家头像',
  status        CHAR(1)       NOT NULL DEFAULT '0'    COMMENT '状态（0正常 1停用）',
  audit_status  CHAR(1)       NOT NULL DEFAULT '0'    COMMENT '审核状态（0待审核 1已通过 2已拒绝）',
  sort_order    INT(11)       DEFAULT 0               COMMENT '排序',
  create_by     VARCHAR(64)                          COMMENT '创建者',
  create_time   DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_by     VARCHAR(64)                          COMMENT '更新者',
  update_time   DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  remark        VARCHAR(500)                          COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_expert_user (user_id),
  KEY idx_expert_specialty (specialty),
  KEY idx_expert_status (status),
  CONSTRAINT fk_expert_info_user FOREIGN KEY (user_id) REFERENCES sys_user (user_id)
) ENGINE=InnoDB COMMENT='专家信息表';

-- ----------------------------
-- 初始化-专家信息表数据
-- ----------------------------
INSERT INTO expert_info (
  user_id, specialty, experience, description, expertise,
  achievements, avatar, status, audit_status, sort_order,
  create_by, remark
) VALUES
(
  1,
  'Greenhouse Agriculture',
  '15 years experience in greenhouse vegetable production management.',
  'Focus on climate control, pest management, and yield improvement for greenhouse vegetables.',
  'climate control,pest management,nutrition optimization',
  'Leader of 2 provincial projects and author of several technical papers.',
  '/profile/experts/greenhouse_admin.png',
  '0',
  '1',
  10,
  'admin',
  'Sample expert for admin user'
),
(
  2,
  'Livestock Farming',
  '12 years experience in large-scale pig farm operation.',
  'Skilled in farm biosecurity, vaccination planning, and manure treatment.',
  'biosecurity,vaccination,manure treatment',
  'Helped multiple farms reduce cost and improve productivity.',
  '/profile/experts/livestock_ry.png',
  '0',
  '1',
  20,
  'admin',
  'Sample expert for ry user'
);
