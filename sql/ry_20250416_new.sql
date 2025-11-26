-- ----------------------------
-- 1、用户信息表（新结构）
-- ----------------------------
drop table if exists sys_user;
create table sys_user (
  id                bigint(20)      not null auto_increment    comment '用户唯一标识',
  username          varchar(50)     not null                   comment '用户名（手机号或邮箱）',
  password_hash     varchar(255)    not null                   comment '密码哈希值',
  role              enum('farmer','expert','bank','buyer','admin') not null comment '用户角色（农户/专家/银行/买家/管理员）',
  name              varchar(100)    default null               comment '用户姓名',
  contact           varchar(100)    default null               comment '联系方式',
  email             varchar(100)    default null               comment '邮箱（可选）',
  phone             varchar(20)     default null               comment '手机号（可选）',
  is_verified       tinyint(1)      default 0                   comment '是否通过资质验证（0:未通过,1:已通过）',
  created_at        datetime        default current_timestamp   comment '创建时间',
  updated_at        datetime        default current_timestamp on update current_timestamp comment '更新时间',
  primary key (id),
  unique key uk_username (username),
  unique key uk_email (email),
  unique key uk_phone (phone)
) engine=innodb auto_increment=1 comment = '用户信息表';

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
-- 管理员用户（密码：admin123，需要在实际使用时进行哈希处理）
insert into sys_user (username, password_hash, role, name, email, phone, is_verified) 
values('admin', '29c67a30398638269fe600f73a054934', 'admin', '管理员', 'admin@example.com', '15888888888', 1);

-- 农户用户（密码：123456，需要在实际使用时进行哈希处理）
insert into sys_user (username, password_hash, role, name, email, phone, is_verified) 
values('1234', 'e10adc3949ba59abbe56e057f20f883e', 'farmer', '测试农户', 'farmer@example.com', '13888888888', 1);

-- 买家用户（密码：buyer1234，需要在实际使用时进行哈希处理）
insert into sys_user (username, password_hash, role, name, email, phone, is_verified) 
values('buyer1', 'buyer1234_hash', 'buyer', '测试买家', 'buyer@example.com', '13988888888', 1);


-- ----------------------------
-- 2、农户商品信息表
-- ----------------------------
drop table if exists farmer_product;
create table farmer_product (
  id                bigint(20)      not null auto_increment    comment '商品唯一标识',
  farmer_id         bigint(20)      not null                   comment '农户用户ID',
  name              varchar(100)    not null                   comment '商品名称',
  price             decimal(10,2)  not null                   comment '商品价格',
  stock             int             not null                   comment '库存数量',
  description       text                                        comment '商品描述',
  status            enum('on_shelf','off_shelf') not null default 'off_shelf' comment '商品状态（上架/下架）',
  created_at        datetime        not null default current_timestamp comment '创建时间',
  updated_at        datetime        not null default current_timestamp on update current_timestamp comment '更新时间',
  is_deleted        tinyint(1)      not null default 0         comment '软删除（0未删除 1已删除）',
  primary key (id),
  key idx_farmer_product_farmer (farmer_id),
  constraint fk_farmer_product_user foreign key (farmer_id) references sys_user (id)
) engine=innodb comment = '农户商品信息表';

-- ----------------------------
-- 3、买家需求信息表
-- ----------------------------
drop table if exists buyer_requirement;
create table buyer_requirement (
  id                bigint(20)      not null auto_increment    comment '需求ID',
  buyer_id          bigint(20)      not null                   comment '买家用户ID',
  product_name      varchar(100)    not null                   comment '需求商品名称',
  quantity          int             not null                   comment '需求数量',
  specs             text                                        comment '商品规格',
  status            enum('unsatisfied','responded') not null default 'unsatisfied' comment '需求状态',
  created_at        datetime        not null default current_timestamp comment '创建时间',
  updated_at        datetime        not null default current_timestamp on update current_timestamp comment '更新时间',
  primary key (id),
  key idx_buyer_requirement_buyer (buyer_id),
  constraint fk_buyer_requirement_user foreign key (buyer_id) references sys_user (id)
) engine=innodb comment = '买家需求信息表';

-- ----------------------------
-- 4、订单表
-- ----------------------------
drop table if exists orders;
create table orders (
  id                bigint(20)      not null auto_increment    comment '订单唯一标识',
  product_id        bigint(20)      not null                   comment '商品ID',
  buyer_id          bigint(20)      not null                   comment '买家用户ID',
  farmer_id         bigint(20)      not null                   comment '农户用户ID',
  quantity          int             not null                   comment '购买数量',
  total_price       decimal(10,2)   default null               comment '总价',
  status            enum('pending','confirmed','completed') default 'pending' comment '订单状态',
  created_at        datetime        default current_timestamp   comment '创建时间',
  updated_at        datetime        default current_timestamp on update current_timestamp comment '更新时间',
  primary key (id),
  key idx_orders_product_id (product_id),
  key idx_orders_buyer_id (buyer_id),
  key idx_orders_farmer_id (farmer_id),
  constraint fk_orders_product foreign key (product_id) references farmer_product (id),
  constraint fk_orders_buyer foreign key (buyer_id) references sys_user (id),
  constraint fk_orders_farmer foreign key (farmer_id) references sys_user (id)
) engine=innodb default charset=utf8mb4 comment='订单表';

