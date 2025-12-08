package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.FinanceNotification;

/**
 * 融资通知Service接口
 */
public interface IFinanceNotificationService
{
    FinanceNotification selectFinanceNotificationById(Long id);

    List<FinanceNotification> selectNotificationListByUserId(Long userId);

    int countUnreadNotifications(Long userId);

    int insertFinanceNotification(FinanceNotification notification);

    int markAsRead(Long id);

    int markAllAsRead(Long userId);

    int deleteFinanceNotificationById(Long id);

    /**
     * 发送审批结果通知
     */
    void sendApprovalNotification(Long applicationId, Long userId, String status, String comment);
}

