package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.FinanceNotification;
import com.ruoyi.system.mapper.FinanceNotificationMapper;
import com.ruoyi.system.service.IFinanceNotificationService;

/**
 * 融资通知Service业务层处理
 */
@Service
public class FinanceNotificationServiceImpl implements IFinanceNotificationService
{
    @Autowired
    private FinanceNotificationMapper financeNotificationMapper;

    @Override
    public FinanceNotification selectFinanceNotificationById(Long id)
    {
        return financeNotificationMapper.selectFinanceNotificationById(id);
    }

    @Override
    public List<FinanceNotification> selectNotificationListByUserId(Long userId)
    {
        return financeNotificationMapper.selectNotificationListByUserId(userId);
    }

    @Override
    public int countUnreadNotifications(Long userId)
    {
        return financeNotificationMapper.countUnreadNotifications(userId);
    }

    @Override
    public int insertFinanceNotification(FinanceNotification notification)
    {
        return financeNotificationMapper.insertFinanceNotification(notification);
    }

    @Override
    public int markAsRead(Long id)
    {
        return financeNotificationMapper.markAsRead(id);
    }

    @Override
    public int markAllAsRead(Long userId)
    {
        return financeNotificationMapper.markAllAsRead(userId);
    }

    @Override
    public int deleteFinanceNotificationById(Long id)
    {
        return financeNotificationMapper.deleteFinanceNotificationById(id);
    }

    @Override
    public void sendApprovalNotification(Long applicationId, Long userId, String status, String comment)
    {
        FinanceNotification notification = new FinanceNotification();
        notification.setApplicationId(applicationId);
        notification.setUserId(userId);
        notification.setNotificationType("approval_result");
        
        if ("approved".equals(status))
        {
            notification.setTitle("贷款申请已通过");
            notification.setContent("恭喜！您的贷款申请已通过审批。" + (comment != null ? "审批意见：" + comment : ""));
        }
        else if ("rejected".equals(status))
        {
            notification.setTitle("贷款申请未通过");
            notification.setContent("很抱歉，您的贷款申请未通过审批。" + (comment != null ? "审批意见：" + comment : ""));
        }
        else
        {
            notification.setTitle("贷款申请状态更新");
            notification.setContent("您的贷款申请状态已更新为：" + status);
        }
        
        notification.setIsRead("0");
        financeNotificationMapper.insertFinanceNotification(notification);
    }
}

