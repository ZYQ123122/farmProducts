package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.Message;

/**
 * 消息Mapper接口
 */
public interface MessageMapper
{
    /**
     * 查询消息
     * 
     * @param id 消息ID
     * @return 消息
     */
    public Message selectMessageById(Long id);

    /**
     * 查询消息列表
     * 
     * @param message 消息
     * @return 消息集合
     */
    public List<Message> selectMessageList(Message message);

    /**
     * 查询两个用户之间的聊天记录
     * 
     * @param userId1 用户1 ID
     * @param userId2 用户2 ID
     * @param relatedOrderId 关联订单ID（可选）
     * @param searchKeyword 搜索关键词（可选）
     * @return 消息集合
     */
    public List<Message> selectConversationMessages(@Param("userId1") Long userId1, 
                                                     @Param("userId2") Long userId2,
                                                     @Param("relatedOrderId") Long relatedOrderId,
                                                     @Param("searchKeyword") String searchKeyword);

    /**
     * 查询用户的会话列表（最近联系的人）
     * 
     * @param userId 用户ID
     * @return 消息集合（每个会话的最新一条消息）
     */
    public List<Message> selectConversationList(Long userId);

    /**
     * 新增消息
     * 
     * @param message 消息
     * @return 结果
     */
    public int insertMessage(Message message);

    /**
     * 修改消息
     * 
     * @param message 消息
     * @return 结果
     */
    public int updateMessage(Message message);

    /**
     * 删除消息
     * 
     * @param id 消息ID
     * @return 结果
     */
    public int deleteMessageById(Long id);

    /**
     * 批量删除消息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteMessageByIds(Long[] ids);

    /**
     * 标记消息为已读
     * 
     * @param receiverId 接收者ID
     * @param senderId 发送者ID
     * @param relatedOrderId 关联订单ID（可选，如果提供则只标记该订单的消息）
     * @return 结果
     */
    public int markMessagesAsRead(@Param("receiverId") Long receiverId, @Param("senderId") Long senderId, @Param("relatedOrderId") Long relatedOrderId);

    /**
     * 查询未读消息数量
     * 
     * @param receiverId 接收者ID
     * @param senderId 发送者ID（可选，如果提供则查询与特定用户的未读数）
     * @return 未读消息数量
     */
    public int countUnreadMessages(@Param("receiverId") Long receiverId, @Param("senderId") Long senderId);

    /**
     * 删除会话（删除两个用户之间的所有消息，可选按订单过滤）
     * 
     * @param userId1 用户1 ID
     * @param userId2 用户2 ID
     * @param relatedOrderId 关联订单ID（可选，如果提供则只删除该订单的消息）
     * @return 结果
     */
    public int deleteConversation(@Param("userId1") Long userId1, @Param("userId2") Long userId2, @Param("relatedOrderId") Long relatedOrderId);
}


