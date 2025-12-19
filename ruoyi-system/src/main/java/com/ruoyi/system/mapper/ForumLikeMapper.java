package com.ruoyi.system.mapper;

import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.ForumLike;

/**
 * 点赞 Mapper
 * 
 * @author ruoyi
 */
public interface ForumLikeMapper
{
    /**
     * 查询点赞记录
     */
    ForumLike selectLike(@Param("commentId") Long commentId, @Param("farmerId") Long farmerId);

    /**
     * 新增点赞
     */
    int insertLike(ForumLike like);

    /**
     * 取消点赞
     */
    int deleteLike(@Param("commentId") Long commentId, @Param("farmerId") Long farmerId);
}

