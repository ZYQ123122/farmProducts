package com.ruoyi.web.controller.system;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.FinanceNotification;
import com.ruoyi.system.service.IFinanceNotificationService;

/**
 * 融资通知Controller
 */
@Controller
@RequestMapping("/system/notification")
public class FinanceNotificationController extends BaseController
{
    @Autowired
    private IFinanceNotificationService financeNotificationService;

    /**
     * 查询用户通知列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list()
    {
        Long userId = ShiroUtils.getUserId();
        startPage();
        List<FinanceNotification> list = financeNotificationService.selectNotificationListByUserId(userId);
        return getDataTable(list);
    }

    /**
     * 查询未读通知数量
     */
    @GetMapping("/unreadCount")
    @ResponseBody
    public AjaxResult unreadCount()
    {
        Long userId = ShiroUtils.getUserId();
        int count = financeNotificationService.countUnreadNotifications(userId);
        return success(count);
    }

    /**
     * 标记为已读
     */
    @PostMapping("/read")
    @ResponseBody
    public AjaxResult markAsRead(Long id)
    {
        return toAjax(financeNotificationService.markAsRead(id));
    }

    /**
     * 标记所有为已读
     */
    @PostMapping("/readAll")
    @ResponseBody
    public AjaxResult markAllAsRead()
    {
        Long userId = ShiroUtils.getUserId();
        return toAjax(financeNotificationService.markAllAsRead(userId));
    }
}

