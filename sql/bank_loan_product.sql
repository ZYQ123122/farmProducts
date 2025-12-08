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

