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
  key idx_bank_status (status)
) engine=innodb comment='银行信息表';

