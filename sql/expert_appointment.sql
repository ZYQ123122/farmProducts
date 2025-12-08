-- ----------------------------
-- 专家预约表
-- ----------------------------
drop table if exists expert_appointment;
create table expert_appointment (
  id               bigint(20)    not null auto_increment comment '预约ID',
  user_id          bigint(20)    not null                comment '农户用户ID',
  expert_id        bigint(20)    not null                comment '专家用户ID',
  appointment_time datetime      not null                comment '预约时间',
  note             text                                   comment '预约说明',
  contact_phone    varchar(20)                            comment '联系方式',
  status           enum('pending','accepted','rejected','cancelled') not null default 'pending' comment '预约状态（pending待处理/accepted已接受/rejected已拒绝/cancelled已取消）',
  created_at       datetime      not null default current_timestamp comment '创建时间',
  updated_at       datetime      not null default current_timestamp on update current_timestamp comment '更新时间',
  primary key (id),
  key idx_expert_appointment_user (user_id),
  key idx_expert_appointment_expert (expert_id),
  key idx_expert_appointment_status (status),
  constraint fk_expert_appointment_user foreign key (user_id) references sys_user (user_id),
  constraint fk_expert_appointment_expert foreign key (expert_id) references sys_user (user_id)
) engine=innodb comment = '专家预约表';


