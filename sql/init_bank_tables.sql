-- ============================================
-- 银行贷款系统初始化SQL脚本
-- 执行顺序：先执行此脚本，再执行 finance_application.sql 的更新部分
-- ============================================

-- ----------------------------
-- 1. 银行信息表
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
  remark         varchar(500)                         comment '备注',
  create_by      varchar(64)  default ''              comment '创建者',
  create_time    datetime     not null default current_timestamp comment '创建时间',
  update_by      varchar(64)  default ''              comment '更新者',
  update_time    datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
  primary key (id),
  unique key uk_bank_code (bank_code),
  unique key uk_bank_user (user_id),
  key idx_bank_status (status)
) engine=innodb comment='银行信息表';

-- ----------------------------
-- 2. 银行贷款产品表
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
-- 3. 融资通知表
-- ----------------------------
drop table if exists finance_notification;
create table finance_notification (
  id             bigint(20)   not null auto_increment comment '通知ID',
  application_id bigint(20)  not null                comment '融资申请ID',
  user_id        bigint(20)   not null                comment '接收用户ID（农户）',
  notification_type varchar(20) not null             comment '通知类型（approval_result/submitted等）',
  title          varchar(200) not null                comment '通知标题',
  content        text                                 comment '通知内容',
  is_read        char(1)      not null default '0'    comment '是否已读（0未读 1已读）',
  create_time    datetime     not null default current_timestamp comment '创建时间',
  read_time      datetime                              comment '阅读时间',
  primary key (id),
  key idx_notification_user (user_id),
  key idx_notification_app (application_id),
  key idx_notification_read (is_read),
  key idx_notification_time (create_time)
) engine=innodb comment='融资通知表';

-- ----------------------------
-- 4. 更新融资申请表（添加产品相关字段）
-- ----------------------------
-- 注意：如果表已存在，需要执行以下ALTER语句
-- ALTER TABLE finance_application ADD COLUMN product_id bigint(20) COMMENT '选择的贷款产品ID' AFTER farmer_id;
-- ALTER TABLE finance_application ADD COLUMN reviewer_id bigint(20) COMMENT '审批银行用户ID' AFTER status;
-- ALTER TABLE finance_application MODIFY COLUMN bank_id bigint(20) COMMENT '银行ID（产品所属银行）';
-- ALTER TABLE finance_application ADD KEY idx_finance_product (product_id);

