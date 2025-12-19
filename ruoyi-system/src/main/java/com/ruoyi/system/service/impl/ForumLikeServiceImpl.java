package com.ruoyi.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.ruoyi.system.domain.ForumLike;
import com.ruoyi.system.mapper.ForumLikeMapper;
import com.ruoyi.system.service.IForumLikeService;

/**
 * 点赞业务实现
 * 
 * @author ruoyi
 */
@Service
public class ForumLikeServiceImpl implements IForumLikeService
{
    @Autowired
    private ForumLikeMapper likeMapper;

    @Override
    public boolean toggleLike(Long commentId, Long farmerId)
    {
        Assert.notNull(commentId, "评论ID不能为空");
        Assert.notNull(farmerId, "农户ID不能为空");
        
        ForumLike existingLike = likeMapper.selectLike(commentId, farmerId);
        if (existingLike != null)
        {
            // 取消点赞
            likeMapper.deleteLike(commentId, farmerId);
            return false;
        }
        else
        {
            // 点赞
            ForumLike like = new ForumLike();
            like.setCommentId(commentId);
            like.setFarmerId(farmerId);
            likeMapper.insertLike(like);
            return true;
        }
    }

    @Override
    public boolean isLiked(Long commentId, Long farmerId)
    {
        Assert.notNull(commentId, "评论ID不能为空");
        Assert.notNull(farmerId, "农户ID不能为空");
        ForumLike like = likeMapper.selectLike(commentId, farmerId);
        return like != null;
    }
}

