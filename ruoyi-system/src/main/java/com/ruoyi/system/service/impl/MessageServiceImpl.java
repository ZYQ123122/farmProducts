package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.MessageMapper;
import com.ruoyi.system.domain.Message;
import com.ruoyi.system.service.IMessageService;

/**
 * 消息Service业务层处理
 */
@Service
public class MessageServiceImpl implements IMessageService
{
    @Autowired
    private MessageMapper messageMapper;

    /**
     * 查询消息
     * 
     * @param id 消息ID
     * @return 消息
     */
    @Override
    public Message selectMessageById(Long id)
    {
        return messageMapper.selectMessageById(id);
    }

    /**
     * 查询消息列表
     * 
     * @param message 消息
     * @return 消息
     */
    @Override
    public List<Message> selectMessageList(Message message)
    {
        return messageMapper.selectMessageList(message);
    }

    /**
     * 查询两个用户之间的聊天记录
     * 
     * @param userId1 用户1 ID
     * @param userId2 用户2 ID
     * @param relatedOrderId 关联订单ID（可选）
     * @param searchKeyword 搜索关键词（可选）
     * @return 消息集合
     */
    @Override
    public List<Message> selectConversationMessages(Long userId1, Long userId2, Long relatedOrderId, String searchKeyword)
    {
        return messageMapper.selectConversationMessages(userId1, userId2, relatedOrderId, searchKeyword);
    }

    /**
     * 查询用户的会话列表（最近联系的人）
     * 
     * @param userId 用户ID
     * @return 消息集合（每个会话的最新一条消息）
     */
    @Override
    public List<Message> selectConversationList(Long userId)
    {
        return messageMapper.selectConversationList(userId);
    }

    /**
     * 新增消息
     * 
     * @param message 消息
     * @return 结果
     */
    @Override
    public int insertMessage(Message message)
    {
        if (message.getMessageType() == null || message.getMessageType().isEmpty())
        {
            message.setMessageType("text");
        }
        return messageMapper.insertMessage(message);
    }

    /**
     * 修改消息
     * 
     * @param message 消息
     * @return 结果
     */
    @Override
    public int updateMessage(Message message)
    {
        return messageMapper.updateMessage(message);
    }

    /**
     * 批量删除消息
     * 
     * @param ids 需要删除的消息ID
     * @return 结果
     */
    @Override
    public int deleteMessageByIds(Long[] ids)
    {
        return messageMapper.deleteMessageByIds(ids);
    }

    /**
     * 删除消息信息
     * 
     * @param id 消息ID
     * @return 结果
     */
    @Override
    public int deleteMessageById(Long id)
    {
        return messageMapper.deleteMessageById(id);
    }

    /**
     * 标记消息为已读
     * 
     * @param receiverId 接收者ID
     * @param senderId 发送者ID
     * @param relatedOrderId 关联订单ID（可选，如果提供则只标记该订单的消息）
     * @return 结果
     */
    @Override
    public int markMessagesAsRead(Long receiverId, Long senderId, Long relatedOrderId)
    {
        return messageMapper.markMessagesAsRead(receiverId, senderId, relatedOrderId);
    }

    /**
     * 查询未读消息数量
     * 
     * @param receiverId 接收者ID
     * @param senderId 发送者ID（可选，如果提供则查询与特定用户的未读数）
     * @return 未读消息数量
     */
    @Override
    public int countUnreadMessages(Long receiverId, Long senderId)
    {
        return messageMapper.countUnreadMessages(receiverId, senderId);
    }

    /**
     * 删除会话（删除两个用户之间的所有消息，可选按订单过滤）
     * 
     * @param userId1 用户1 ID
     * @param userId2 用户2 ID
     * @param relatedOrderId 关联订单ID（可选，如果提供则只删除该订单的消息）
     * @return 结果
     */
    @Override
    public int deleteConversation(Long userId1, Long userId2, Long relatedOrderId)
    {
        return messageMapper.deleteConversation(userId1, userId2, relatedOrderId);
    }
}


