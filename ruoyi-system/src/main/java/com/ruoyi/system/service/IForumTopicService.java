package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.ForumTopic;

/**
 * 讨论主题业务接口
 * 
 * @author ruoyi
 */
public interface IForumTopicService
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
    int updatePinnedStatus(Long id, Boolean isPinned);
}

