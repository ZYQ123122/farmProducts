-- ----------------------------
-- 专家信息表
-- ----------------------------
DROP TABLE IF EXISTS expert_info;

CREATE TABLE expert_info (
                             id            BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '专家信息ID',
                             user_id       BIGINT(20)    NOT NULL                COMMENT '关联用户ID',
                             specialty     VARCHAR(100)                          COMMENT '专业领域',
                             experience    VARCHAR(200)                          COMMENT '工作经验',
                             description   TEXT                                  COMMENT '专家简介',
                             expertise     TEXT                                  COMMENT '擅长方向（多个用逗号分隔）',
                             achievements  TEXT                                  COMMENT '主要成就',
                             avatar        VARCHAR(200)                          COMMENT '专家头像',
                             status        CHAR(1)       NOT NULL DEFAULT '0'    COMMENT '状态（0正常 1停用）',
                             audit_status  CHAR(1)       NOT NULL DEFAULT '0'    COMMENT '审核状态（0待审核 1已通过 2已拒绝）',
                             sort_order    INT(11)       DEFAULT 0               COMMENT '排序',
                             create_by     VARCHAR(64)                          COMMENT '创建者',
                             create_time   DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             update_by     VARCHAR(64)                          COMMENT '更新者',
                             update_time   DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             remark        VARCHAR(500)                          COMMENT '备注',
                             PRIMARY KEY (id),
                             UNIQUE KEY uk_expert_user (user_id),
                             KEY idx_expert_specialty (specialty),
                             KEY idx_expert_status (status),
                             CONSTRAINT fk_expert_info_user FOREIGN KEY (user_id) REFERENCES sys_user (id)
) ENGINE=InnoDB COMMENT='专家信息表';

-- ----------------------------
-- 专家预约表
-- ----------------------------
DROP TABLE IF EXISTS expert_appointment;

CREATE TABLE expert_appointment (
                                    id               BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '预约ID',
                                    user_id          BIGINT(20)    NOT NULL                COMMENT '农户用户ID',
                                    expert_id        BIGINT(20)    NOT NULL                COMMENT '专家用户ID',
                                    appointment_time DATETIME      NOT NULL                COMMENT '预约时间',
                                    note             TEXT                                   COMMENT '预约说明',
                                    contact_phone    VARCHAR(20)                           COMMENT '联系方式',
                                    status           ENUM('pending','accepted','rejected','cancelled') NOT NULL DEFAULT 'pending' COMMENT '预约状态（pending待处理/accepted已接受/rejected已拒绝/cancelled已取消）',
                                    created_at       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    updated_at       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    PRIMARY KEY (id),
                                    KEY idx_expert_appointment_user (user_id),
                                    KEY idx_expert_appointment_expert (expert_id),
                                    KEY idx_expert_appointment_status (status),
                                    CONSTRAINT fk_expert_appointment_user FOREIGN KEY (user_id) REFERENCES sys_user (id),
                                    CONSTRAINT fk_expert_appointment_expert FOREIGN KEY (expert_id) REFERENCES sys_user (id)
) ENGINE=InnoDB COMMENT='专家预约表';

-- ----------------------------
-- 专家咨询表
-- ----------------------------
DROP TABLE IF EXISTS expert_consultation;

CREATE TABLE expert_consultation (
                                     id            BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '咨询ID',
                                     user_id       BIGINT(20)    NOT NULL                COMMENT '咨询者用户ID',
                                     expert_id     BIGINT(20)    NOT NULL                COMMENT '专家用户ID',
                                     title         VARCHAR(200)  NOT NULL                COMMENT '咨询标题',
                                     content       TEXT          NOT NULL                COMMENT '咨询内容',
                                     status        ENUM('pending','answered','closed') NOT NULL DEFAULT 'pending' COMMENT '咨询状态（pending待回复/answered已回复/closed已关闭）',
                                     reply         TEXT                                  COMMENT '专家回复内容',
                                     reply_time    DATETIME                              COMMENT '回复时间',
                                     created_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     updated_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     is_deleted    TINYINT(1)    NOT NULL DEFAULT 0      COMMENT '软删除标记（0未删除 1已删除）',
                                     PRIMARY KEY (id),
                                     KEY idx_expert_consultation_user (user_id),
                                     KEY idx_expert_consultation_expert (expert_id),
                                     KEY idx_expert_consultation_status (status),
                                     CONSTRAINT fk_expert_consultation_user FOREIGN KEY (user_id) REFERENCES sys_user (id),
                                     CONSTRAINT fk_expert_consultation_expert FOREIGN KEY (expert_id) REFERENCES sys_user (id)
) ENGINE=InnoDB COMMENT='专家咨询表';

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
                                CONSTRAINT fk_knowledge_author FOREIGN KEY (author_id) REFERENCES sys_user (id)
) ENGINE=InnoDB COMMENT='知识库表';

