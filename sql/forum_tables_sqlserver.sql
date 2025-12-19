-- ----------------------------
-- 社区交流模块 - SQL Server 版本
-- 包含：主题表、评论表、附件表、点赞表、分类表
-- ----------------------------

USE ry;
GO

-- ----------------------------
-- 1、论坛分类表 forum_categories
-- ----------------------------
IF OBJECT_ID('forum_categories', 'U') IS NOT NULL
    DROP TABLE forum_categories;
GO

CREATE TABLE forum_categories (
    id              BIGINT          NOT NULL IDENTITY(1,1),    -- 分类唯一标识
    name            VARCHAR(50)     NOT NULL,                   -- 分类名称
    description     NVARCHAR(255)   NULL,                       -- 分类描述
    sort_order      INT             DEFAULT 0,                  -- 排序顺序
    created_at      DATETIME        DEFAULT GETDATE(),          -- 创建时间
    updated_at      DATETIME        DEFAULT GETDATE(),          -- 更新时间
    PRIMARY KEY (id)
);
GO

-- 创建触发器自动更新 updated_at
CREATE TRIGGER trg_forum_categories_updated_at
ON forum_categories
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE forum_categories
    SET updated_at = GETDATE()
    FROM forum_categories fc
    INNER JOIN inserted i ON fc.id = i.id;
END;
GO

-- 插入默认分类数据
INSERT INTO forum_categories (name, description, sort_order) VALUES
('种植技术', '分享种植经验和技术', 1),
('市场行情', '讨论农产品市场价格和趋势', 2),
('政策法规', '农业政策法规解读', 3),
('经验交流', '农户经验分享', 4),
('问题求助', '遇到问题寻求帮助', 5);
GO

PRINT 'forum_categories 表创建完成！';
GO

-- ----------------------------
-- 2、讨论主题表 forum_topics
-- ----------------------------
IF OBJECT_ID('forum_topics', 'U') IS NOT NULL
    DROP TABLE forum_topics;
GO

CREATE TABLE forum_topics (
    id              BIGINT          NOT NULL IDENTITY(1,1),    -- 主题唯一标识
    farmer_id       BIGINT          NOT NULL,                   -- 创建农户 ID
    title           VARCHAR(100)    NOT NULL,                   -- 主题标题
    content         NVARCHAR(MAX)   NULL,                       -- 主题内容
    category_id     BIGINT          NULL,                       -- 分类 ID
    is_pinned       BIT             DEFAULT 0,                  -- 是否置顶（0: 否, 1: 是）
    view_count      INT             DEFAULT 0,                  -- 浏览次数
    reply_count     INT             DEFAULT 0,                  -- 回复次数
    created_at      DATETIME        DEFAULT GETDATE(),          -- 创建时间
    updated_at      DATETIME        DEFAULT GETDATE(),          -- 更新时间
    PRIMARY KEY (id),
    CONSTRAINT fk_forum_topics_farmer FOREIGN KEY (farmer_id) REFERENCES sys_user(id),
    CONSTRAINT fk_forum_topics_category FOREIGN KEY (category_id) REFERENCES forum_categories(id)
);
GO

-- 创建索引
CREATE INDEX idx_forum_topics_farmer_id ON forum_topics(farmer_id);
CREATE INDEX idx_forum_topics_category_id ON forum_topics(category_id);
CREATE INDEX idx_forum_topics_created_at ON forum_topics(created_at DESC);
CREATE INDEX idx_forum_topics_is_pinned ON forum_topics(is_pinned);
GO

-- 创建触发器自动更新 updated_at
CREATE TRIGGER trg_forum_topics_updated_at
ON forum_topics
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE forum_topics
    SET updated_at = GETDATE()
    FROM forum_topics ft
    INNER JOIN inserted i ON ft.id = i.id;
END;
GO

PRINT 'forum_topics 表创建完成！';
GO

-- ----------------------------
-- 3、评论表 forum_comments
-- ----------------------------
IF OBJECT_ID('forum_comments', 'U') IS NOT NULL
    DROP TABLE forum_comments;
GO

CREATE TABLE forum_comments (
    id                  BIGINT          NOT NULL IDENTITY(1,1),    -- 评论唯一标识
    topic_id            BIGINT          NOT NULL,                   -- 关联主题 ID
    farmer_id           BIGINT          NOT NULL,                   -- 评论农户 ID
    content             NVARCHAR(MAX)   NOT NULL,                   -- 评论内容
    parent_comment_id   BIGINT          NULL,                       -- 父评论 ID（回复）
    quote_comment_id    BIGINT          NULL,                       -- 引用评论 ID
    like_count          INT             DEFAULT 0,                  -- 点赞数
    created_at          DATETIME        DEFAULT GETDATE(),          -- 创建时间
    updated_at          DATETIME        DEFAULT GETDATE(),          -- 更新时间
    PRIMARY KEY (id),
    CONSTRAINT fk_forum_comments_topic FOREIGN KEY (topic_id) REFERENCES forum_topics(id) ON DELETE CASCADE,
    CONSTRAINT fk_forum_comments_farmer FOREIGN KEY (farmer_id) REFERENCES sys_user(id),
    CONSTRAINT fk_forum_comments_parent FOREIGN KEY (parent_comment_id) REFERENCES forum_comments(id),
    CONSTRAINT fk_forum_comments_quote FOREIGN KEY (quote_comment_id) REFERENCES forum_comments(id)
);
GO

-- 创建索引
CREATE INDEX idx_forum_comments_topic_id ON forum_comments(topic_id);
CREATE INDEX idx_forum_comments_farmer_id ON forum_comments(farmer_id);
CREATE INDEX idx_forum_comments_parent_id ON forum_comments(parent_comment_id);
CREATE INDEX idx_forum_comments_created_at ON forum_comments(created_at DESC);
GO

-- 创建触发器自动更新 updated_at
CREATE TRIGGER trg_forum_comments_updated_at
ON forum_comments
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE forum_comments
    SET updated_at = GETDATE()
    FROM forum_comments fc
    INNER JOIN inserted i ON fc.id = i.id;
END;
GO

PRINT 'forum_comments 表创建完成！';
GO

-- ----------------------------
-- 4、附件表 forum_attachments
-- ----------------------------
IF OBJECT_ID('forum_attachments', 'U') IS NOT NULL
    DROP TABLE forum_attachments;
GO

CREATE TABLE forum_attachments (
    id              BIGINT          NOT NULL IDENTITY(1,1),    -- 附件唯一标识
    related_id      BIGINT          NOT NULL,                   -- 关联 ID（主题或评论 ID）
    related_type    VARCHAR(20)     NOT NULL,                   -- 关联类型（topic/comment）
    url             VARCHAR(255)    NOT NULL,                   -- 附件 URL（如图片、文档）
    type            VARCHAR(50)     NULL,                       -- 文件类型
    file_name       VARCHAR(255)    NULL,                       -- 原始文件名
    file_size       BIGINT          NULL,                       -- 文件大小（字节）
    created_at      DATETIME        DEFAULT GETDATE(),          -- 创建时间
    PRIMARY KEY (id),
    CONSTRAINT chk_forum_attachments_type CHECK (related_type IN ('topic', 'comment'))
);
GO

-- 创建索引
CREATE INDEX idx_forum_attachments_related ON forum_attachments(related_id, related_type);
GO

PRINT 'forum_attachments 表创建完成！';
GO

-- ----------------------------
-- 5、点赞表 forum_likes
-- ----------------------------
IF OBJECT_ID('forum_likes', 'U') IS NOT NULL
    DROP TABLE forum_likes;
GO

CREATE TABLE forum_likes (
    id              BIGINT          NOT NULL IDENTITY(1,1),    -- 点赞唯一标识
    comment_id      BIGINT          NOT NULL,                   -- 关联评论 ID
    farmer_id       BIGINT          NOT NULL,                   -- 点赞农户 ID
    created_at      DATETIME        DEFAULT GETDATE(),          -- 创建时间
    PRIMARY KEY (id),
    CONSTRAINT fk_forum_likes_comment FOREIGN KEY (comment_id) REFERENCES forum_comments(id) ON DELETE CASCADE,
    CONSTRAINT fk_forum_likes_farmer FOREIGN KEY (farmer_id) REFERENCES sys_user(id),
    CONSTRAINT uk_forum_likes_user_comment UNIQUE (comment_id, farmer_id)  -- 防止重复点赞
);
GO

-- 创建索引
CREATE INDEX idx_forum_likes_comment_id ON forum_likes(comment_id);
CREATE INDEX idx_forum_likes_farmer_id ON forum_likes(farmer_id);
GO

PRINT 'forum_likes 表创建完成！';
GO

-- ----------------------------
-- 创建触发器：自动更新主题的回复数
-- ----------------------------
IF OBJECT_ID('trg_forum_comments_update_reply_count', 'TR') IS NOT NULL
    DROP TRIGGER trg_forum_comments_update_reply_count;
GO

CREATE TRIGGER trg_forum_comments_update_reply_count
ON forum_comments
AFTER INSERT, DELETE
AS
BEGIN
    SET NOCOUNT ON;
    
    -- 处理插入
    IF EXISTS (SELECT 1 FROM inserted)
    BEGIN
        UPDATE forum_topics
        SET reply_count = (
            SELECT COUNT(*)
            FROM forum_comments
            WHERE topic_id = forum_topics.id
        )
        WHERE id IN (SELECT DISTINCT topic_id FROM inserted);
    END
    
    -- 处理删除
    IF EXISTS (SELECT 1 FROM deleted)
    BEGIN
        UPDATE forum_topics
        SET reply_count = (
            SELECT COUNT(*)
            FROM forum_comments
            WHERE topic_id = forum_topics.id
        )
        WHERE id IN (SELECT DISTINCT topic_id FROM deleted);
    END
END;
GO

PRINT '触发器 trg_forum_comments_update_reply_count 创建完成！';
GO

-- ----------------------------
-- 创建触发器：自动更新评论的点赞数
-- ----------------------------
IF OBJECT_ID('trg_forum_likes_update_like_count', 'TR') IS NOT NULL
    DROP TRIGGER trg_forum_likes_update_like_count;
GO

CREATE TRIGGER trg_forum_likes_update_like_count
ON forum_likes
AFTER INSERT, DELETE
AS
BEGIN
    SET NOCOUNT ON;
    
    -- 处理插入
    IF EXISTS (SELECT 1 FROM inserted)
    BEGIN
        UPDATE forum_comments
        SET like_count = (
            SELECT COUNT(*)
            FROM forum_likes
            WHERE comment_id = forum_comments.id
        )
        WHERE id IN (SELECT DISTINCT comment_id FROM inserted);
    END
    
    -- 处理删除
    IF EXISTS (SELECT 1 FROM deleted)
    BEGIN
        UPDATE forum_comments
        SET like_count = (
            SELECT COUNT(*)
            FROM forum_likes
            WHERE comment_id = forum_comments.id
        )
        WHERE id IN (SELECT DISTINCT comment_id FROM deleted);
    END
END;
GO

PRINT '触发器 trg_forum_likes_update_like_count 创建完成！';
GO

PRINT '========================================';
PRINT '社区交流模块所有表创建完成！';
PRINT '========================================';
GO

