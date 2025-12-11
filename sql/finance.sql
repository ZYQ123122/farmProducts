-- ----------------------------
-- 银行信息表
-- ----------------------------
drop table if exists bank_info;
create table bank_info (
                           id             bigint(20)   not null auto_increment comment '银行ID',
                           bank_code      varchar(50)  not null                comment '银行代码（唯一标识）',
                           bank_name      varchar(100) not null                comment '银行名称',
                           user_id        bigint(20)   not null                comment '关联系统用户ID（银行登录账号）',
                           contact_person varchar(50)                          comment '联系人',
                           contact_phone  varchar(20)                          comment '联系电话',
                           contact_email  varchar(100)                         comment '联系邮箱',
                           address        varchar(255)                         comment '银行地址',
                           status         char(1)      not null default '0'    comment '状态（0正常 1停用）',
                           remark         varchar(500)                          comment '备注',
                           create_by      varchar(64)  default ''              comment '创建者',
                           create_time    datetime     not null default current_timestamp comment '创建时间',
                           update_by      varchar(64)  default ''              comment '更新者',
                           update_time    datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
                           primary key (id),
                           unique key uk_bank_code (bank_code),
                           unique key uk_bank_user (user_id),
                           key idx_bank_status (status),
                           constraint fk_bank_info_user foreign key (user_id) references sys_user (id)
) engine=innodb comment='银行信息表';

-- ----------------------------
-- 银行贷款产品表
-- ----------------------------
drop table if exists bank_loan_product;
create table bank_loan_product (
                                   id             bigint(20)   not null auto_increment comment '产品ID',
                                   bank_id        bigint(20)   not null                comment '银行ID',
                                   product_name   varchar(100) not null                comment '产品名称',
                                   product_code   varchar(50)                          comment '产品代码',
                                   min_amount     decimal(15,2) not null default 0     comment '最小贷款金额（元）',
                                   max_amount     decimal(15,2) not null               comment '最大贷款金额（元）',
                                   min_term_months int(11)     not null default 1      comment '最短期限（月）',
                                   max_term_months int(11)     not null                comment '最长期限（月）',
                                   interest_rate  decimal(5,2)                         comment '年利率（%）',
                                   description    text                                 comment '产品描述',
                                   requirements   text                                 comment '申请条件',
                                   status         char(1)      not null default '1'    comment '状态（0下架 1上架）',
                                   sort_order     int(11)      default 0                comment '排序',
                                   create_by      varchar(64)  default ''              comment '创建者',
                                   create_time    datetime     not null default current_timestamp comment '创建时间',
                                   update_by      varchar(64)  default ''              comment '更新者',
                                   update_time    datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
                                   remark         varchar(500)                         comment '备注',
                                   primary key (id),
                                   key idx_bank_product_bank (bank_id),
                                   key idx_bank_product_status (status),
                                   key idx_bank_product_code (product_code),
                                   constraint fk_bank_product_bank foreign key (bank_id) references bank_info (id)
) engine=innodb comment='银行贷款产品表';

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

-- ----------------------------
-- 融资通知表
-- ----------------------------
DROP TABLE IF EXISTS finance_notification;

CREATE TABLE finance_notification (
                                      id               BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '通知ID',
                                      application_id   BIGINT(20)   NOT NULL                COMMENT '融资申请ID',
                                      user_id          BIGINT(20)   NOT NULL                COMMENT '接收用户ID（农户）',
                                      notification_type VARCHAR(20) NOT NULL                COMMENT '通知类型（approval_result/submitted等）',
                                      title            VARCHAR(200) NOT NULL                COMMENT '通知标题',
                                      content          TEXT                                 COMMENT '通知内容',
                                      is_read          CHAR(1)       NOT NULL DEFAULT '0'    COMMENT '是否已读（0未读 1已读）',
                                      create_time      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      read_time        DATETIME                             COMMENT '阅读时间',
                                      PRIMARY KEY (id),
                                      KEY idx_notification_user (user_id),
                                      KEY idx_notification_app (application_id),
                                      KEY idx_notification_read (is_read),
                                      KEY idx_notification_time (create_time),
                                      CONSTRAINT fk_finance_notification_user FOREIGN KEY (user_id) REFERENCES sys_user (id),
                                      CONSTRAINT fk_finance_notification_app FOREIGN KEY (application_id) REFERENCES finance_application (id)
) ENGINE=InnoDB COMMENT='融资通知表';