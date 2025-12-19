package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.ForumComment;

/**
 * 评论业务接口
 * 
 * @author ruoyi
 */
public interface IForumCommentService
{
    /**
     * 查询评论列表（按主题ID）
     */
    List<ForumComment> selectCommentListByTopicId(Long topicId);

    /**
     * 根据ID查询评论
     */
    ForumComment selectCommentById(Long id);

    /**
     * 新增评论
     */
    int insertComment(ForumComment comment);

    /**
     * 修改评论
     */
    int updateComment(ForumComment comment);

    /**
     * 删除评论
     */
    int deleteCommentById(Long id);
}

