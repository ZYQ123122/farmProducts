package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.ForumTopic;

/**
 * 讨论主题 Mapper
 * 
 * @author ruoyi
 */
public interface ForumTopicMapper
{
    /**
     * 查询主题列表
     */
    List<ForumTopic> selectTopicList(ForumTopic topic);

    /**
     * 根据ID查询主题详情
     */
    ForumTopic selectTopicById(Long id);

    /**
     * 新增主题
     */
    int insertTopic(ForumTopic topic);

    /**
     * 修改主题
     */
    int updateTopic(ForumTopic topic);

    /**
     * 删除主题
     */
    int deleteTopicById(Long id);

    /**
     * 增加浏览次数
     */
    int incrementViewCount(Long id);

    /**
     * 置顶/取消置顶
     */
    int updatePinnedStatus(@Param("id") Long id, @Param("isPinned") Boolean isPinned);
}

