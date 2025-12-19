package com.ruoyi.system.domain;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 专家咨询实体
 */
public class ExpertConsultation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 咨询ID */
    private Long id;

    /** 咨询者用户ID */
    private Long userId;

    /** 专家用户ID */
    private Long expertId;

    /** 咨询标题 */
    @NotBlank(message = "咨询标题不能为空")
    @Size(max = 200, message = "咨询标题长度不能超过200个字符")
    private String title;

    /** 咨询内容 */
    @NotBlank(message = "咨询内容不能为空")
    @Size(max = 2000, message = "咨询内容长度不能超过2000个字符")
    private String content;

    /** 咨询状态（pending待回复/answered已回复/closed已关闭） */
    private String status;

    /** 专家回复内容 */
    private String reply;

    /** 回复时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date replyTime;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** 软删除标记（0 未删除 1 已删除） */
    private Integer isDeleted;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getExpertId()
    {
        return expertId;
    }

    public void setExpertId(Long expertId)
    {
        this.expertId = expertId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getReply()
    {
        return reply;
    }

    public void setReply(String reply)
    {
        this.reply = reply;
    }

    public Date getReplyTime()
    {
        return replyTime;
    }

    public void setReplyTime(Date replyTime)
    {
        this.replyTime = replyTime;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public Integer getIsDeleted()
    {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted)
    {
        this.isDeleted = isDeleted;
    }
}
