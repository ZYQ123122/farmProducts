package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 消息实体
 */
public class Message extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 消息ID */
    private Long id;

    /** 发送者ID */
    private Long senderId;

    /** 接收者ID */
    private Long receiverId;

    /** 消息内容 */
    private String content;

    /** 关联订单ID（可选） */
    private Long relatedOrderId;

    /** 关联需求ID（可选） */
    private Long relatedDemandId;

    /** 消息类型（text/image/file） */
    private String messageType;

    /** 文件URL（图片或文件） */
    private String fileUrl;

    /** 文件名称 */
    private String fileName;

    /** 是否已读（0:未读,1:已读） */
    private Integer isRead;

    /** 发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 发送者姓名（展示用） */
    private String senderName;

    /** 接收者姓名（展示用） */
    private String receiverName;

    /** 发送者角色（展示用） */
    private String senderRole;

    /** 接收者角色（展示用） */
    private String receiverRole;

    /** 订单信息（展示用） */
    private String orderInfo;

    /** 需求信息（展示用） */
    private String demandInfo;

    /** 未读消息数量（展示用） */
    private Integer unreadCount;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getSenderId()
    {
        return senderId;
    }

    public void setSenderId(Long senderId)
    {
        this.senderId = senderId;
    }

    public Long getReceiverId()
    {
        return receiverId;
    }

    public void setReceiverId(Long receiverId)
    {
        this.receiverId = receiverId;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Long getRelatedOrderId()
    {
        return relatedOrderId;
    }

    public void setRelatedOrderId(Long relatedOrderId)
    {
        this.relatedOrderId = relatedOrderId;
    }

    public Long getRelatedDemandId()
    {
        return relatedDemandId;
    }

    public void setRelatedDemandId(Long relatedDemandId)
    {
        this.relatedDemandId = relatedDemandId;
    }

    public String getMessageType()
    {
        return messageType;
    }

    public void setMessageType(String messageType)
    {
        this.messageType = messageType;
    }

    public String getFileUrl()
    {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl)
    {
        this.fileUrl = fileUrl;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public Integer getIsRead()
    {
        return isRead;
    }

    public void setIsRead(Integer isRead)
    {
        this.isRead = isRead;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getSenderName()
    {
        return senderName;
    }

    public void setSenderName(String senderName)
    {
        this.senderName = senderName;
    }

    public String getReceiverName()
    {
        return receiverName;
    }

    public void setReceiverName(String receiverName)
    {
        this.receiverName = receiverName;
    }

    public String getOrderInfo()
    {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo)
    {
        this.orderInfo = orderInfo;
    }

    public String getDemandInfo()
    {
        return demandInfo;
    }

    public void setDemandInfo(String demandInfo)
    {
        this.demandInfo = demandInfo;
    }

    public String getSenderRole()
    {
        return senderRole;
    }

    public void setSenderRole(String senderRole)
    {
        this.senderRole = senderRole;
    }

    public String getReceiverRole()
    {
        return receiverRole;
    }

    public void setReceiverRole(String receiverRole)
    {
        this.receiverRole = receiverRole;
    }

    public Integer getUnreadCount()
    {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount)
    {
        this.unreadCount = unreadCount;
    }
}


