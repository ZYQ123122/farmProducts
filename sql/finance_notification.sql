-- ----------------------------
-- 融资通知表
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

