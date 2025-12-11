-- ----------------------------
-- 评价表
-- ----------------------------
drop table if exists reviews;
create table reviews (
  id                bigint(20)      not null auto_increment    comment '评价唯一标识',
  order_id          bigint(20)      not null                   comment '关联订单 ID',
  farmer_id         bigint(20)      not null                   comment '农户 ID',
  buyer_id          bigint(20)      not null                   comment '买家 ID',
  rating            int             not null                   comment '评分（1-5分）',
  content           text                                        comment '评价内容',
  created_at        datetime        not null default current_timestamp comment '创建时间',
  primary key (id),
  key idx_reviews_order (order_id),
  key idx_reviews_farmer (farmer_id),
  key idx_reviews_buyer (buyer_id),
  constraint fk_reviews_order foreign key (order_id) references orders (id),
  constraint fk_reviews_farmer foreign key (farmer_id) references sys_user (id),
  constraint fk_reviews_buyer foreign key (buyer_id) references sys_user (id)
) engine=innodb auto_increment=1 comment = '评价表';






