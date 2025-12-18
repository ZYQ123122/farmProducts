package com.ruoyi.system.domain;

import org.apache.ibatis.type.Alias;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.ruoyi.common.core.domain.entity.SysUser;

/**
 * 用户匹配特征实体（扩展SysUser，存储匹配所需特征）
 *
 * @author 开发者
 * @date 2025-12-19
 */
@Alias("SysUserMatch") // MyBatis别名（核心，用于XML映射）
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserMatch extends SysUser {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 地区（省-市-区，如：江苏省-南京市-江宁区） */
    private String area;

    /** 经营/采购品类（如：水稻/生猪/蔬菜） */
    private String category;

    /** 需求类型（融资/技术/采购/供货） */
    private String demandType;

    /** 规模（农户：亩数/头数；买家：采购量；银行：授信额度） */
    private Integer scale;

    /** 专家擅长领域（仅专家用户，user_type=03） */
    private String expertField;

    /** 银行贷款类型（仅银行用户，user_type=04） */
    private String bankLoanType;

    /** 匹配分数（0-100分） */
    private Double matchScore;

    /** 匹配到的用户ID */
    private Long matchUserId;

    /** 匹配到的用户名 */
    private String matchUserName;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;
}