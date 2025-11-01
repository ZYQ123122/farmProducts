package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * ũ����Ʒ��Ϣ
 */
public class FarmerProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ��ƷΨһ��ʶ */
    private Long id;

    /** ũ���û�ID */
    private Long farmerId;

    /** ��Ʒ���� */
    @NotBlank(message = "��Ʒ���Ʋ���Ϊ��")
    @Size(max = 100, message = "��Ʒ���Ʋ��ܳ���100���ַ�")
    private String name;

    /** ��Ʒ�۸� */
    @NotNull(message = "��Ʒ�۸���Ϊ��")
    @DecimalMin(value = "0.00", message = "��Ʒ�۸���Ϊ����")
    private BigDecimal price;

    /** ������� */
    @NotNull(message = "�����������Ϊ��")
    @Min(value = 0, message = "�����������Ϊ����")
    private Integer stock;

    /** ��Ʒ���� */
    private String description;

    /** ��Ʒ״̬��on_shelf / off_shelf�� */
    private String status;

    /** ����ʱ�� */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** ����ʱ�� */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** ��ɾ����ǣ�0 δɾ�� 1 ��ɾ���� */
    private Integer isDeleted;

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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public Integer getStock()
    {
        return stock;
    }

    public void setStock(Integer stock)
    {
        this.stock = stock;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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

    public Integer getIsDeleted()
    {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted)
    {
        this.isDeleted = isDeleted;
    }
}
