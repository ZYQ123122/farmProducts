package com.ruoyi.system.domain;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 讨论主题对象 forum_topics
 * 
 * @author ruoyi
 */
public class ForumTopic extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主题唯一标识 */
    private Long id;

    /** 创建农户 ID */
    private Long farmerId;

    /** 主题标题 */
    private String title;

    /** 主题内容 */
    private String content;

    /** 分类 ID */
    private Long categoryId;

    /** 是否置顶（0: 否, 1: 是） */
    private Boolean isPinned;

    /** 浏览次数 */
    private Integer viewCount;

    /** 回复次数 */
    private Integer replyCount;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** 农户名称（关联查询） */
    private String farmerName;

    /** 分类名称（关联查询） */
    private String categoryName;

    /** 附件列表 */
    private List<ForumAttachment> attachments;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getFarmerId()
    {
        return farmerId;
    }

    public void setFarmerId(Long farmerId)
    {
        this.farmerId = farmerId;
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

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public Boolean getIsPinned()
    {
        return isPinned;
    }

    public void setIsPinned(Boolean isPinned)
    {
        this.isPinned = isPinned;
    }

    public Integer getViewCount()
    {
        return viewCount;
    }

    public void setViewCount(Integer viewCount)
    {
        this.viewCount = viewCount;
    }

    public Integer getReplyCount()
    {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount)
    {
        this.replyCount = replyCount;
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

    public String getFarmerName()
    {
        return farmerName;
    }

    public void setFarmerName(String farmerName)
    {
        this.farmerName = farmerName;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public List<ForumAttachment> getAttachments()
    {
        return attachments;
    }

    public void setAttachments(List<ForumAttachment> attachments)
    {
        this.attachments = attachments;
    }
}

