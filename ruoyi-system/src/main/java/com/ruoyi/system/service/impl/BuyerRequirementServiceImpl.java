package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BuyerRequirement;
import com.ruoyi.system.mapper.BuyerRequirementMapper;
import com.ruoyi.system.service.IBuyerRequirementService;

/**
 * 买家需求 服务实现
 */
@Service
public class BuyerRequirementServiceImpl implements IBuyerRequirementService
{
    private static final String STATUS_UNSATISFIED = "unsatisfied";
    private static final String STATUS_RESPONDED = "responded";

    @Autowired
    private BuyerRequirementMapper buyerRequirementMapper;

    @Override
    public List<BuyerRequirement> selectByBuyerId(Long buyerId)
    {
        return buyerRequirementMapper.selectByBuyerId(buyerId);
    }

    @Override
    public List<BuyerRequirement> selectAll()
    {
        return buyerRequirementMapper.selectAll();
    }

    @Override
    public BuyerRequirement createRequirement(BuyerRequirement requirement)
    {
        requirement.setStatus(STATUS_UNSATISFIED);
        requirement.setCreatedAt(new Date());
        requirement.setUpdatedAt(requirement.getCreatedAt());
        buyerRequirementMapper.insertBuyerRequirement(requirement);
        return buyerRequirementMapper.selectByIdAndBuyerId(requirement.getId(), requirement.getBuyerId());
    }

    @Override
    public BuyerRequirement selectByIdAndBuyerId(Long id, Long buyerId)
    {
        return buyerRequirementMapper.selectByIdAndBuyerId(id, buyerId);
    }

    @Override
    public int updateStatus(Long id, Long buyerId, String status)
    {
        if (!StringUtils.equalsAny(status, STATUS_UNSATISFIED, STATUS_RESPONDED))
        {
            return 0;
        }
        return buyerRequirementMapper.updateStatus(id, buyerId, status);
    }
}
