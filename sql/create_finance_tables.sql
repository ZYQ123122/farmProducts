-- ----------------------------
-- 创建融资相关数据表
-- ----------------------------

-- 融资申请主表
DROP TABLE IF EXISTS finance_application;
CREATE TABLE finance_application (
  id             BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '申请ID',
  farmer_id      BIGINT(20)   NOT NULL                COMMENT '农户用户ID',
  amount         DECIMAL(15,2) NOT NULL               COMMENT '申请金额',
  term_months    INT(11)      NOT NULL                COMMENT '贷款期限（月）',
  purpose        VARCHAR(255)                         COMMENT '贷款用途',
  collateral_desc VARCHAR(500)                        COMMENT '抵押物说明',
  status         VARCHAR(20)  NOT NULL DEFAULT 'submitted' COMMENT '状态（submitted/in_review/approved/rejected）',
  bank_id        BIGINT(20)                           COMMENT '审批银行用户ID',
  bank_comment   VARCHAR(500)                         COMMENT '审批意见',
  created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx_finance_farmer (farmer_id),
  KEY idx_finance_status (status)
) ENGINE=InnoDB COMMENT='融资申请表';

-- 融资抵押物附件表
DROP TABLE IF EXISTS finance_collateral;
CREATE TABLE finance_collateral (
  id             BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  application_id BIGINT(20)   NOT NULL                COMMENT '融资申请ID',
  file_url       VARCHAR(500) NOT NULL                COMMENT '文件URL',
  file_name      VARCHAR(255)                        COMMENT '文件名',
  file_type      VARCHAR(100)                        COMMENT '文件类型',
  created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  KEY idx_finance_collateral_app (application_id)
) ENGINE=InnoDB COMMENT='融资抵押物附件表';

-- 插入一些测试数据（可选）
INSERT INTO finance_application (farmer_id, amount, term_months, purpose, collateral_desc, status) VALUES
(2, 50000.00, 12, '购买农机设备', '拖拉机一台，价值约8万元', 'submitted'),
(2, 30000.00, 6, '种子化肥采购', '土地使用权证书', 'in_review'),
(3, 100000.00, 24, '扩大种植规模', '农田200亩承包权', 'approved');
