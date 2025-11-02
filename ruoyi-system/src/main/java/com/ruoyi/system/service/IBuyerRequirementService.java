package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.system.domain.BuyerRequirement;

/**
 * 买家需求 服务接口
 */
public interface IBuyerRequirementService
{
    /**
     * 根据买家ID查询需求列表
     */
    List<BuyerRequirement> selectByBuyerId(Long buyerId);

    /**
     * 查询全部买家需求
     */
    List<BuyerRequirement> selectAll();

    /**
     * 新增买家需求
     */
    BuyerRequirement createRequirement(BuyerRequirement requirement);

    /**
     * 根据ID和买家查询需求
     */
    BuyerRequirement selectByIdAndBuyerId(Long id, Long buyerId);

    /**
     * 更新需求状态
     */
    int updateStatus(Long id, Long buyerId, String status);
}
