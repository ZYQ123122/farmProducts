package com.ruoyi.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.FinanceApplication;
import com.ruoyi.system.domain.FinanceCollateral;
import com.ruoyi.system.mapper.FinanceApplicationMapper;
import com.ruoyi.system.mapper.FinanceCollateralMapper;
import com.ruoyi.system.service.IFinanceApplicationService;

/**
 * 融资申请Service业务层处理
 */
@Service
public class FinanceApplicationServiceImpl implements IFinanceApplicationService
{
    @Autowired
    private FinanceApplicationMapper financeApplicationMapper;

    @Autowired
    private FinanceCollateralMapper financeCollateralMapper;

    @Override
    public FinanceApplication selectFinanceApplicationById(Long id)
    {
        return financeApplicationMapper.selectFinanceApplicationById(id);
    }

    @Override
    public List<FinanceApplication> selectFinanceApplicationList(FinanceApplication application)
    {
        return financeApplicationMapper.selectFinanceApplicationList(application);
    }

    @Override
    public int insertFinanceApplication(FinanceApplication application, List<FinanceCollateral> collateralList)
    {
        if (application == null)
        {
            throw new ServiceException("融资申请不能为空");
        }
        int rows = financeApplicationMapper.insertFinanceApplication(application);
        if (rows > 0 && collateralList != null && !collateralList.isEmpty())
        {
            for (FinanceCollateral c : collateralList)
            {
                c.setApplicationId(application.getId());
                financeCollateralMapper.insertFinanceCollateral(c);
            }
        }
        return rows;
    }

    @Override
    public int updateFinanceApplication(FinanceApplication application)
    {
        if (application == null || application.getId() == null)
        {
            throw new ServiceException("融资申请ID不能为空");
        }
        return financeApplicationMapper.updateFinanceApplication(application);
    }
}
