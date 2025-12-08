-- ----------------------------
-- 专家咨询表
-- ----------------------------
drop table if exists expert_consultation;
create table expert_consultation (
  id            bigint(20)    not null auto_increment comment '咨询ID',
  user_id       bigint(20)    not null                comment '咨询者用户ID',
  expert_id     bigint(20)    not null                comment '专家用户ID',
  title         varchar(200)  not null                comment '咨询标题',
  content       text          not null                comment '咨询内容',
  status        enum('pending','answered','closed') not null default 'pending' comment '咨询状态（pending待回复/answered已回复/closed已关闭）',
  reply         text                                  comment '专家回复内容',
  reply_time    datetime                              comment '回复时间',
  created_at    datetime      not null default current_timestamp comment '创建时间',
  updated_at    datetime      not null default current_timestamp on update current_timestamp comment '更新时间',
  is_deleted    tinyint(1)    not null default 0       comment '软删除标记（0未删除 1已删除）',
  primary key (id),
  key idx_expert_consultation_user (user_id),
  key idx_expert_consultation_expert (expert_id),
  key idx_expert_consultation_status (status),
  constraint fk_expert_consultation_user foreign key (user_id) references sys_user (user_id),
  constraint fk_expert_consultation_expert foreign key (expert_id) references sys_user (user_id)
) engine=innodb comment = '专家咨询表';

