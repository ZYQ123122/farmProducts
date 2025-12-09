package com.ruoyi.web.controller.message;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.config.ServerConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.system.domain.Message;
import com.ruoyi.system.service.IMessageService;
import com.ruoyi.system.service.ITradeOrderService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.domain.TradeOrder;

/**
 * 消息API Controller
 */
@RestController
@RequestMapping("/api/message")
public class MessageApiController extends BaseController
{
    @Autowired
    private IMessageService messageService;

    @Autowired
    private ITradeOrderService tradeOrderService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 获取会话列表
     */
    @GetMapping("/conversations")
    public AjaxResult getConversations()
    {
        try
        {
            SysUser user = getSysUser();
            if (user == null)
            {
                return AjaxResult.error("未登录");
            }
            List<Message> conversations = messageService.selectConversationList(getUserId());
            return AjaxResult.success(conversations);
        }
        catch (Exception e)
        {
            logger.error("获取会话列表失败", e);
            return AjaxResult.error("获取会话列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取聊天记录
     */
    @GetMapping("/messages")
    public AjaxResult getMessages(@RequestParam Long otherUserId,
                                  @RequestParam(required = false) Long relatedOrderId,
                                  @RequestParam(required = false) String searchKeyword)
    {
        try
        {
            SysUser user = getSysUser();
            if (user == null)
            {
                return AjaxResult.error("未登录");
            }
            List<Message> messages = messageService.selectConversationMessages(
                getUserId(), otherUserId, relatedOrderId, searchKeyword);
            // 标记为已读（如果指定了订单，只标记该订单的消息）
            messageService.markMessagesAsRead(getUserId(), otherUserId, relatedOrderId);
            return AjaxResult.success(messages);
        }
        catch (Exception e)
        {
            logger.error("获取聊天记录失败", e);
            return AjaxResult.error("获取聊天记录失败：" + e.getMessage());
        }
    }

    /**
     * 发送消息
     */
    @PostMapping("/send")
    public AjaxResult sendMessage(@RequestParam Long receiverId,
                                  @RequestParam(required = false) String content,
                                  @RequestParam(required = false) Long relatedOrderId,
                                  @RequestParam(required = false) Long relatedDemandId,
                                  @RequestParam(required = false) MultipartFile file)
    {
        try
        {
            SysUser user = getSysUser();
            if (user == null)
            {
                return AjaxResult.error("未登录");
            }

            Message message = new Message();
            message.setSenderId(getUserId());
            message.setReceiverId(receiverId);
            message.setRelatedOrderId(relatedOrderId);
            message.setRelatedDemandId(relatedDemandId);

            if (file != null && !file.isEmpty())
            {
                // 上传文件
                String filePath = RuoYiConfig.getUploadPath();
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = serverConfig.getUrl() + fileName;
                
                message.setFileUrl(url);
                message.setFileName(file.getOriginalFilename());
                
                // 判断文件类型
                String originalFilename = file.getOriginalFilename().toLowerCase();
                if (originalFilename.endsWith(".jpg") || originalFilename.endsWith(".jpeg") 
                    || originalFilename.endsWith(".png") || originalFilename.endsWith(".gif")
                    || originalFilename.endsWith(".bmp") || originalFilename.endsWith(".webp"))
                {
                    message.setMessageType("image");
                }
                else
                {
                    message.setMessageType("file");
                }
            }
            else
            {
                message.setContent(content);
                message.setMessageType("text");
            }

            int result = messageService.insertMessage(message);
            if (result > 0)
            {
                Message savedMessage = messageService.selectMessageById(message.getId());
                return AjaxResult.success("发送成功", savedMessage);
            }
            else
            {
                return AjaxResult.error("发送失败");
            }
        }
        catch (Exception e)
        {
            logger.error("发送消息失败", e);
            return AjaxResult.error("发送消息失败：" + e.getMessage());
        }
    }

    /**
     * 获取未读消息数量
     */
    @GetMapping("/unread/count")
    public AjaxResult getUnreadCount(@RequestParam(required = false) Long senderId)
    {
        try
        {
            SysUser user = getSysUser();
            if (user == null)
            {
                return AjaxResult.error("未登录");
            }
            int count = messageService.countUnreadMessages(getUserId(), senderId);
            return AjaxResult.success(count);
        }
        catch (Exception e)
        {
            logger.error("获取未读消息数量失败", e);
            return AjaxResult.error("获取未读消息数量失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户的订单列表（用于选择订单进行沟通）
     */
    @GetMapping("/orders")
    public AjaxResult getOrders()
    {
        try
        {
            SysUser user = getSysUser();
            if (user == null)
            {
                return AjaxResult.error("未登录");
            }
            
            List<TradeOrder> orders;
            if ("farmer".equals(user.getRole()))
            {
                orders = tradeOrderService.listOrdersForFarmer(getUserId());
            }
            else if ("buyer".equals(user.getRole()))
            {
                orders = tradeOrderService.listOrdersForBuyer(getUserId());
            }
            else
            {
                return AjaxResult.error("当前用户角色不支持");
            }
            
            return AjaxResult.success(orders);
        }
        catch (Exception e)
        {
            logger.error("获取订单列表失败", e);
            return AjaxResult.error("获取订单列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据用户ID获取用户信息
     */
    @GetMapping("/user/{userId}")
    public AjaxResult getUserInfo(@PathVariable Long userId)
    {
        try
        {
            SysUser user = userService.selectUserById(userId);
            if (user == null)
            {
                return AjaxResult.error("用户不存在");
            }
            // 只返回必要信息，不返回敏感信息
            SysUser userInfo = new SysUser();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            userInfo.setName(user.getName());
            userInfo.setRole(user.getRole());
            return AjaxResult.success(userInfo);
        }
        catch (Exception e)
        {
            logger.error("获取用户信息失败", e);
            return AjaxResult.error("获取用户信息失败：" + e.getMessage());
        }
    }

    /**
     * 删除会话
     */
    @PostMapping("/conversation/delete")
    public AjaxResult deleteConversation(@RequestParam Long otherUserId,
                                         @RequestParam(required = false) Long relatedOrderId)
    {
        try
        {
            SysUser user = getSysUser();
            if (user == null)
            {
                return AjaxResult.error("未登录");
            }
            
            int result = messageService.deleteConversation(getUserId(), otherUserId, relatedOrderId);
            if (result > 0)
            {
                return AjaxResult.success("删除成功");
            }
            else
            {
                return AjaxResult.error("删除失败，可能会话不存在");
            }
        }
        catch (Exception e)
        {
            logger.error("删除会话失败", e);
            return AjaxResult.error("删除会话失败：" + e.getMessage());
        }
    }
}

