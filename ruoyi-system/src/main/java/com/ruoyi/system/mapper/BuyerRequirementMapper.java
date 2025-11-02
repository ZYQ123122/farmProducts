package com.ruoyi.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.system.domain.BuyerRequirement;

/**
 * 买家需求 Mapper
 */
public interface BuyerRequirementMapper
{
    /**
     * 根据买家查询需求列表
     */
    List<BuyerRequirement> selectByBuyerId(@Param("buyerId") Long buyerId);

    /**
     * 查询全部买家需求
     */
    List<BuyerRequirement> selectAll();

    /**
     * 根据ID和买家查询需求
     */
    BuyerRequirement selectByIdAndBuyerId(@Param("id") Long id, @Param("buyerId") Long buyerId);

    /**
     * 插入买家需求
     */
    int insertBuyerRequirement(BuyerRequirement requirement);

    /**
     * 更新买家需求状态
     */
    int updateStatus(@Param("id") Long id, @Param("buyerId") Long buyerId, @Param("status") String status);
}
