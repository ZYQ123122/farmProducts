package com.ruoyi.system.domain;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 评论对象 forum_comments
 * 
 * @author ruoyi
 */
public class ForumComment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 评论唯一标识 */
    private Long id;

    /** 关联主题 ID */
    private Long topicId;

    /** 评论农户 ID */
    private Long farmerId;

    /** 评论内容 */
    private String content;

    /** 父评论 ID（回复） */
    private Long parentCommentId;

    /** 引用评论 ID */
    private Long quoteCommentId;

    /** 点赞数 */
    private Integer likeCount;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** 农户名称（关联查询） */
    private String farmerName;

    /** 父评论（关联查询） */
    private ForumComment parentComment;

    /** 引用评论（关联查询） */
    private ForumComment quoteComment;

    /** 子评论列表 */
    private List<ForumComment> children;

    /** 附件列表 */
    private List<ForumAttachment> attachments;

    /** 当前用户是否已点赞 */
    private Boolean isLiked;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getTopicId()
    {
        return topicId;
    }

    public void setTopicId(Long topicId)
    {
        this.topicId = topicId;
    }

    public Long getFarmerId()
    {
        return farmerId;
    }

    public void setFarmerId(Long farmerId)
    {
        this.farmerId = farmerId;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Long getParentCommentId()
    {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId)
    {
        this.parentCommentId = parentCommentId;
    }

    public Long getQuoteCommentId()
    {
        return quoteCommentId;
    }

    public void setQuoteCommentId(Long quoteCommentId)
    {
        this.quoteCommentId = quoteCommentId;
    }

    public Integer getLikeCount()
    {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount)
    {
        this.likeCount = likeCount;
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

    public ForumComment getParentComment()
    {
        return parentComment;
    }

    public void setParentComment(ForumComment parentComment)
    {
        this.parentComment = parentComment;
    }

    public ForumComment getQuoteComment()
    {
        return quoteComment;
    }

    public void setQuoteComment(ForumComment quoteComment)
    {
        this.quoteComment = quoteComment;
    }

    public List<ForumComment> getChildren()
    {
        return children;
    }

    public void setChildren(List<ForumComment> children)
    {
        this.children = children;
    }

    public List<ForumAttachment> getAttachments()
    {
        return attachments;
    }

    public void setAttachments(List<ForumAttachment> attachments)
    {
        this.attachments = attachments;
    }

    public Boolean getIsLiked()
    {
        return isLiked;
    }

    public void setIsLiked(Boolean isLiked)
    {
        this.isLiked = isLiked;
    }
}

