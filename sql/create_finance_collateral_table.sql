-- ----------------------------
-- 融资抵押物附件表
-- ----------------------------
DROP TABLE IF EXISTS finance_collateral;
CREATE TABLE finance_collateral (
  id             BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  application_id BIGINT(20)   NOT NULL                COMMENT '融资申请ID',
  file_url       VARCHAR(500) NOT NULL                COMMENT '文件URL',
  file_name      VARCHAR(255)                         COMMENT '文件名',
  file_type      VARCHAR(100)                         COMMENT '文件类型',
  created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  KEY idx_finance_collateral_app (application_id)
) ENGINE=InnoDB COMMENT='融资抵押物附件表';

