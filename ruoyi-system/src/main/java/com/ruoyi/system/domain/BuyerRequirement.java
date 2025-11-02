package com.ruoyi.system.domain;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 买家需求实体
 */
public class BuyerRequirement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 需求ID */
    private Long id;

    /** 买家用户ID */
    private Long buyerId;

    /** 商品名称 */
    @NotBlank(message = "需求商品名称不能为空")
    @Size(max = 100, message = "需求商品名称长度不能超过100个字符")
    private String productName;

    /** 需求数量 */
    @NotNull(message = "需求数量不能为空")
    @Min(value = 1, message = "需求数量必须大于0")
    private Integer quantity;

    /** 商品规格（选填） */
    private String specs;

    /** 需求状态（unsatisfied/responded） */
    private String status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getBuyerId()
    {
        return buyerId;
    }

    public void setBuyerId(Long buyerId)
    {
        this.buyerId = buyerId;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public String getSpecs()
    {
        return specs;
    }

    public void setSpecs(String specs)
    {
        this.specs = specs;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
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
}
