package com.ruoyi.system.mapper;

import java.util.List;

import com.ruoyi.system.domain.FinanceApplication;

/**
 * 融资申请Mapper接口
 */
public interface FinanceApplicationMapper
{
    FinanceApplication selectFinanceApplicationById(Long id);

    List<FinanceApplication> selectFinanceApplicationList(FinanceApplication application);

    int insertFinanceApplication(FinanceApplication application);

    int updateFinanceApplication(FinanceApplication application);
}

