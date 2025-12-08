-- ----------------------------
-- 知识库表
-- ----------------------------
DROP TABLE IF EXISTS knowledge_base;
CREATE TABLE knowledge_base (
  id            BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '知识ID',
  title         VARCHAR(200)  NOT NULL                COMMENT '知识标题',
  content       TEXT          NOT NULL                COMMENT '知识内容',
  category      VARCHAR(50)                          COMMENT '知识分类（planting种植/breeding养殖/pest病虫害/market市场/other其他）',
  category_name VARCHAR(50)                          COMMENT '分类名称',
  tags          VARCHAR(200)                         COMMENT '标签（多个用逗号分隔）',
  author_id     BIGINT(20)                            COMMENT '作者ID（关联专家）',
  author_name   VARCHAR(50)                          COMMENT '作者姓名',
  view_count    INT(11)       DEFAULT 0               COMMENT '浏览次数',
  like_count    INT(11)       DEFAULT 0               COMMENT '点赞数',
  status        CHAR(1)       NOT NULL DEFAULT '0'    COMMENT '状态（0正常 1停用）',
  is_top        CHAR(1)       DEFAULT '0'             COMMENT '是否置顶（0否 1是）',
  publish_time  DATETIME                              COMMENT '发布时间',
  create_by     VARCHAR(64)                          COMMENT '创建者',
  create_time   DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_by     VARCHAR(64)                          COMMENT '更新者',
  update_time   DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  remark        VARCHAR(500)                          COMMENT '备注',
  PRIMARY KEY (id),
  KEY idx_knowledge_category (category),
  KEY idx_knowledge_author (author_id),
  KEY idx_knowledge_status (status),
  KEY idx_knowledge_publish_time (publish_time),
  CONSTRAINT fk_knowledge_author FOREIGN KEY (author_id) REFERENCES sys_user (user_id)
) ENGINE=InnoDB COMMENT='知识库表';

