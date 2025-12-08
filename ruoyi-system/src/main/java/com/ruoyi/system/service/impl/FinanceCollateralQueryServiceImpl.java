package com.ruoyi.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.system.domain.FinanceCollateral;
import com.ruoyi.system.mapper.FinanceCollateralMapper;
import com.ruoyi.system.service.FinanceCollateralQueryService;

/**
 * 融资抵押物查询Service实现
 */
@Service
public class FinanceCollateralQueryServiceImpl implements FinanceCollateralQueryService
{
    @Autowired
    private FinanceCollateralMapper financeCollateralMapper;

    @Override
    public List<FinanceCollateral> selectByApplicationId(Long applicationId)
    {
        return financeCollateralMapper.selectFinanceCollateralListByAppId(applicationId);
    }
}


