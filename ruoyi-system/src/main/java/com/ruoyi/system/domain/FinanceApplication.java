package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 融资申请实体
 */
public class FinanceApplication extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 农户用户ID */
    private Long farmerId;

    /** 选择的贷款产品ID */
    private Long productId;

    /** 银行ID（产品所属银行） */
    private Long bankId;

    /** 申请金额 */
    private BigDecimal amount;

    /** 贷款期限（月） */
    private Integer termMonths;

    /** 贷款用途 */
    private String purpose;

    /** 抵押物说明 */
    private String collateralDesc;

    /** 状态 */
    private String status;

    /** 审批银行用户ID */
    private Long reviewerId;

    /** 审批意见 */
    private String bankComment;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFarmerId() { return farmerId; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Integer getTermMonths() { return termMonths; }
    public void setTermMonths(Integer termMonths) { this.termMonths = termMonths; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getCollateralDesc() { return collateralDesc; }
    public void setCollateralDesc(String collateralDesc) { this.collateralDesc = collateralDesc; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getBankId() { return bankId; }
    public void setBankId(Long bankId) { this.bankId = bankId; }

    public Long getReviewerId() { return reviewerId; }
    public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }

    public String getBankComment() { return bankComment; }
    public void setBankComment(String bankComment) { this.bankComment = bankComment; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}

