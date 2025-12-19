package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.FinanceNotification;

/**
 * 融资通知Mapper接口
 */
public interface FinanceNotificationMapper
{
    /**
     * 查询通知
     */
    FinanceNotification selectFinanceNotificationById(Long id);

    /**
     * 查询用户通知列表
     */
    List<FinanceNotification> selectNotificationListByUserId(Long userId);

    /**
     * 查询未读通知数量
     */
    int countUnreadNotifications(Long userId);

    /**
     * 新增通知
     */
    int insertFinanceNotification(FinanceNotification notification);

    /**
     * 标记为已读
     */
    int markAsRead(Long id);

    /**
     * 标记所有为已读
     */
    int markAllAsRead(Long userId);

    /**
     * 删除通知
     */
    int deleteFinanceNotificationById(Long id);
}