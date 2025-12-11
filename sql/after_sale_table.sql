-- ----------------------------
-- 售后表
-- ----------------------------
drop table if exists after_sale;
create table after_sale (
  id                bigint(20)      not null auto_increment    comment '售后唯一标识',
  order_id          bigint(20)      not null                   comment '关联订单 ID',
  farmer_id         bigint(20)      not null                   comment '农户 ID',
  buyer_id          bigint(20)      not null                   comment '买家 ID',
  issue             text                                        comment '问题描述（如退货请求）',
  status            enum('pending','resolved') not null default 'pending' comment '售后状态（待处理/已解决）',
  created_at        datetime        not null default current_timestamp comment '创建时间',
  primary key (id),
  key idx_after_sale_order (order_id),
  key idx_after_sale_farmer (farmer_id),
  key idx_after_sale_buyer (buyer_id),
  constraint fk_after_sale_order foreign key (order_id) references orders (id),
  constraint fk_after_sale_farmer foreign key (farmer_id) references sys_user (id),
  constraint fk_after_sale_buyer foreign key (buyer_id) references sys_user (id)
) engine=innodb auto_increment=1 comment = '售后表';






