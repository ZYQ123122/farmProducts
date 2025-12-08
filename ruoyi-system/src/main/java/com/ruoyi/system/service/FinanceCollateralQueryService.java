package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.system.domain.FinanceCollateral;

/**
 * 融资抵押物只读查询Service（用于聚合展示）
 */
public interface FinanceCollateralQueryService
{
    List<FinanceCollateral> selectByApplicationId(Long applicationId);
}


