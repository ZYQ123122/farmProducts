package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 专家信息对象 expert_info
 *
 * @author ruoyi
 * @date 2025-11-26
 */
public class ExpertInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 专家信息ID */
    private Long id;

    /** 关联用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 专业领域 */
    @Excel(name = "专业领域")
    private String specialty;

    /** 工作经验 */
    @Excel(name = "工作经验")
    private String experience;

    /** 专家简介 */
    @Excel(name = "专家简介")
    private String description;

    /** 擅长方向 */
    @Excel(name = "擅长方向")
    private String expertise;

    /** 主要成就 */
    @Excel(name = "主要成就")
    private String achievements;

    /** 专家头像 */
    private String avatar;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 审核状态（0待审核 1已通过 2已拒绝） */
    @Excel(name = "审核状态", readConverterExp = "0=待审核,1=已通过,2=已拒绝")
    private String auditStatus;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sortOrder;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setSpecialty(String specialty)
    {
        this.specialty = specialty;
    }

    public String getSpecialty()
    {
        return specialty;
    }

    public void setExperience(String experience)
    {
        this.experience = experience;
    }

    public String getExperience()
    {
        return experience;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public void setExpertise(String expertise)
    {
        this.expertise = expertise;
    }

    public String getExpertise()
    {
        return expertise;
    }

    public void setAchievements(String achievements)
    {
        this.achievements = achievements;
    }

    public String getAchievements()
    {
        return achievements;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setAuditStatus(String auditStatus)
    {
        this.auditStatus = auditStatus;
    }

    public String getAuditStatus()
    {
        return auditStatus;
    }

    public void setSortOrder(Integer sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    public Integer getSortOrder()
    {
        return sortOrder;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("specialty", getSpecialty())
                .append("experience", getExperience())
                .append("description", getDescription())
                .append("expertise", getExpertise())
                .append("achievements", getAchievements())
                .append("avatar", getAvatar())
                .append("status", getStatus())
                .append("auditStatus", getAuditStatus())
                .append("sortOrder", getSortOrder())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
