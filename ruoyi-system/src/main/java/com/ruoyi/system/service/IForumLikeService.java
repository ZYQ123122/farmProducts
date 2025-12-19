package com.ruoyi.system.service;

/**
 * 点赞业务接口
 * 
 * @author ruoyi
 */
public interface IForumLikeService
{
    /**
     * 点赞/取消点赞
     */
    boolean toggleLike(Long commentId, Long farmerId);

    /**
     * 检查是否已点赞
     */
    boolean isLiked(Long commentId, Long farmerId);
}

