-- ----------------------------
-- 融资申请主表
-- ----------------------------
drop table if exists finance_application;
create table finance_application (
  id             bigint(20)   not null auto_increment comment '申请ID',
  farmer_id      bigint(20)   not null                comment '农户用户ID',
  product_id     bigint(20)                           comment '选择的贷款产品ID',
  bank_id        bigint(20)                           comment '银行ID（产品所属银行）',
  amount         decimal(15,2) not null               comment '申请金额',
  term_months    int(11)      not null                comment '贷款期限（月）',
  purpose        varchar(255)                         comment '贷款用途',
  collateral_desc varchar(500)                        comment '抵押物说明',
  status         varchar(20)  not null default 'submitted' comment '状态（submitted/in_review/approved/rejected）',
  reviewer_id    bigint(20)                           comment '审批银行用户ID',
  bank_comment   varchar(500)                         comment '审批意见',
  created_at     datetime     not null default current_timestamp comment '创建时间',
  updated_at     datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
  primary key (id),
  key idx_finance_farmer (farmer_id),
  key idx_finance_product (product_id),
  key idx_finance_bank (bank_id),
  key idx_finance_status (status)
) engine=innodb comment='融资申请表';


