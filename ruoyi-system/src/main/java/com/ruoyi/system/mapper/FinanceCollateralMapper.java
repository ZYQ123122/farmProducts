package com.ruoyi.system.mapper;

import java.util.List;

import com.ruoyi.system.domain.FinanceCollateral;

/**
 * 融资抵押物附件Mapper接口
 */
public interface FinanceCollateralMapper
{
    List<FinanceCollateral> selectFinanceCollateralListByAppId(Long applicationId);

    int insertFinanceCollateral(FinanceCollateral collateral);

    int deleteFinanceCollateralByAppId(Long applicationId);
}


