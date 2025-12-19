package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.ruoyi.system.domain.ForumTopic;
import com.ruoyi.system.mapper.ForumTopicMapper;
import com.ruoyi.system.service.IForumTopicService;

/**
 * 讨论主题业务实现
 * 
 * @author ruoyi
 */
@Service
public class ForumTopicServiceImpl implements IForumTopicService
{
    @Autowired
    private ForumTopicMapper topicMapper;

    @Override
    public List<ForumTopic> selectTopicList(ForumTopic topic)
    {
        return topicMapper.selectTopicList(topic);
    }

    @Override
    public ForumTopic selectTopicById(Long id)
    {
        Assert.notNull(id, "主题ID不能为空");
        return topicMapper.selectTopicById(id);
    }

    @Override
    public int insertTopic(ForumTopic topic)
    {
        Assert.notNull(topic.getFarmerId(), "农户ID不能为空");
        Assert.hasText(topic.getTitle(), "主题标题不能为空");
        if (topic.getIsPinned() == null)
        {
            topic.setIsPinned(false);
        }
        return topicMapper.insertTopic(topic);
    }

    @Override
    public int updateTopic(ForumTopic topic)
    {
        Assert.notNull(topic.getId(), "主题ID不能为空");
        return topicMapper.updateTopic(topic);
    }

    @Override
    public int deleteTopicById(Long id)
    {
        Assert.notNull(id, "主题ID不能为空");
        return topicMapper.deleteTopicById(id);
    }

    @Override
    public int incrementViewCount(Long id)
    {
        Assert.notNull(id, "主题ID不能为空");
        return topicMapper.incrementViewCount(id);
    }

    @Override
    public int updatePinnedStatus(Long id, Boolean isPinned)
    {
        Assert.notNull(id, "主题ID不能为空");
        Assert.notNull(isPinned, "置顶状态不能为空");
        return topicMapper.updatePinnedStatus(id, isPinned);
    }
}

