package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 评价实体
 */
public class Review extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 评价ID */
    private Long id;

    /** 关联订单ID */
    private Long orderId;

    /** 农户ID */
    private Long farmerId;

    /** 买家ID */
    private Long buyerId;

    /** 评分（1-5分） */
    private Integer rating;

    /** 评价内容 */
    private String content;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 订单信息（展示用） */
    private String orderInfo;

    /** 商品名称（展示用） */
    private String productName;

    /** 农户名称（展示用） */
    private String farmerName;

    /** 买家名称（展示用） */
    private String buyerName;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public Long getFarmerId()
    {
        return farmerId;
    }

    public void setFarmerId(Long farmerId)
    {
        this.farmerId = farmerId;
    }

    public Long getBuyerId()
    {
        return buyerId;
    }

    public void setBuyerId(Long buyerId)
    {
        this.buyerId = buyerId;
    }

    public Integer getRating()
    {
        return rating;
    }

    public void setRating(Integer rating)
    {
        this.rating = rating;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getOrderInfo()
    {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo)
    {
        this.orderInfo = orderInfo;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getFarmerName()
    {
        return farmerName;
    }

    public void setFarmerName(String farmerName)
    {
        this.farmerName = farmerName;
    }

    public String getBuyerName()
    {
        return buyerName;
    }

    public void setBuyerName(String buyerName)
    {
        this.buyerName = buyerName;
    }

    @Override
    public String toString()
    {
        return "Review{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", farmerId=" + farmerId +
                ", buyerId=" + buyerId +
                ", rating=" + rating +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", orderInfo='" + orderInfo + '\'' +
                ", productName='" + productName + '\'' +
                ", farmerName='" + farmerName + '\'' +
                ", buyerName='" + buyerName + '\'' +
                '}';
    }
}






