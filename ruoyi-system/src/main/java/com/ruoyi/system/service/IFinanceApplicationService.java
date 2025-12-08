package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.system.domain.FinanceApplication;
import com.ruoyi.system.domain.FinanceCollateral;

/**
 * 融资申请Service接口
 */
public interface IFinanceApplicationService
{
    FinanceApplication selectFinanceApplicationById(Long id);

    List<FinanceApplication> selectFinanceApplicationList(FinanceApplication application);

    int insertFinanceApplication(FinanceApplication application, List<FinanceCollateral> collateralList);

    int updateFinanceApplication(FinanceApplication application);
}


