-- ----------------------------
-- 消息表
-- ----------------------------
drop table if exists messages;
create table messages (
  id                bigint(20)      not null auto_increment    comment '消息唯一标识',
  sender_id         bigint(20)      not null                   comment '发送者ID',
  receiver_id       bigint(20)      not null                   comment '接收者ID',
  content           text            not null                   comment '消息内容',
  related_order_id  bigint(20)      default null               comment '关联订单ID（可选）',
  related_demand_id bigint(20)      default null               comment '关联需求ID（可选）',
  message_type      varchar(20)     default 'text'             comment '消息类型（text/image/file）',
  file_url          varchar(500)    default null                comment '文件URL（图片或文件）',
  file_name         varchar(255)    default null                comment '文件名称',
  is_read           tinyint(1)      default 0                   comment '是否已读（0:未读,1:已读）',
  created_at        datetime        default current_timestamp   comment '发送时间',
  primary key (id),
  key idx_messages_sender (sender_id),
  key idx_messages_receiver (receiver_id),
  key idx_messages_order (related_order_id),
  key idx_messages_demand (related_demand_id),
  key idx_messages_created (created_at),
  constraint fk_messages_sender foreign key (sender_id) references sys_user (id),
  constraint fk_messages_receiver foreign key (receiver_id) references sys_user (id),
  constraint fk_messages_order foreign key (related_order_id) references orders (id) on delete set null,
  constraint fk_messages_demand foreign key (related_demand_id) references buyer_requirement (id) on delete set null
) engine=innodb default charset=utf8mb4 comment='消息表';






