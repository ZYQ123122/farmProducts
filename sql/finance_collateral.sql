-- ----------------------------
-- 融资抵押物附件表
-- ----------------------------
drop table if exists finance_collateral;
create table finance_collateral (
  id             bigint(20)   not null auto_increment comment '主键ID',
  application_id bigint(20)   not null                comment '融资申请ID',
  file_url       varchar(500) not null                comment '文件URL',
  file_name      varchar(255)                        comment '文件名',
  file_type      varchar(100)                        comment '文件类型',
  created_at     datetime     not null default current_timestamp comment '创建时间',
  primary key (id),
  key idx_finance_collateral_app (application_id)
) engine=innodb comment='融资抵押物附件表';


