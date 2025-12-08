DROP TABLE IF EXISTS finance_collateral;
CREATE TABLE finance_collateral (
  id             BIGINT(20)   NOT NULL AUTO_INCREMENT,
  application_id BIGINT(20)   NOT NULL,
  file_url       VARCHAR(500) NOT NULL,
  file_name      VARCHAR(255),
  file_type      VARCHAR(100),
  created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_finance_collateral_app (application_id)
) ENGINE=InnoDB;

