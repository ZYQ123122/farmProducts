package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.ruoyi.system.domain.ForumComment;
import com.ruoyi.system.mapper.ForumCommentMapper;
import com.ruoyi.system.service.IForumCommentService;

/**
 * 评论业务实现
 * 
 * @author ruoyi
 */
@Service
public class ForumCommentServiceImpl implements IForumCommentService
{
    @Autowired
    private ForumCommentMapper commentMapper;

    @Override
    public List<ForumComment> selectCommentListByTopicId(Long topicId)
    {
        Assert.notNull(topicId, "主题ID不能为空");
        return commentMapper.selectCommentListByTopicId(topicId);
    }

    @Override
    public ForumComment selectCommentById(Long id)
    {
        Assert.notNull(id, "评论ID不能为空");
        return commentMapper.selectCommentById(id);
    }

    @Override
    public int insertComment(ForumComment comment)
    {
        Assert.notNull(comment.getTopicId(), "主题ID不能为空");
        Assert.notNull(comment.getFarmerId(), "农户ID不能为空");
        Assert.hasText(comment.getContent(), "评论内容不能为空");
        return commentMapper.insertComment(comment);
    }

    @Override
    public int updateComment(ForumComment comment)
    {
        Assert.notNull(comment.getId(), "评论ID不能为空");
        Assert.hasText(comment.getContent(), "评论内容不能为空");
        return commentMapper.updateComment(comment);
    }

    @Override
    public int deleteCommentById(Long id)
    {
        Assert.notNull(id, "评论ID不能为空");
        return commentMapper.deleteCommentById(id);
    }
}

