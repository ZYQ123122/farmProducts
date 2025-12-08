package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BankInfo;
import com.ruoyi.system.mapper.BankInfoMapper;
import com.ruoyi.system.service.IBankInfoService;

/**
 * 银行信息Service业务层处理
 */
@Service
public class BankInfoServiceImpl implements IBankInfoService
{
    @Autowired
    private BankInfoMapper bankInfoMapper;

    @Override
    public BankInfo selectBankInfoById(Long id)
    {
        return bankInfoMapper.selectBankInfoById(id);
    }

    @Override
    public BankInfo selectBankInfoByUserId(Long userId)
    {
        return bankInfoMapper.selectBankInfoByUserId(userId);
    }

    @Override
    public List<BankInfo> selectBankInfoList(BankInfo bankInfo)
    {
        return bankInfoMapper.selectBankInfoList(bankInfo);
    }

    @Override
    public int insertBankInfo(BankInfo bankInfo)
    {
        return bankInfoMapper.insertBankInfo(bankInfo);
    }

    @Override
    public int updateBankInfo(BankInfo bankInfo)
    {
        return bankInfoMapper.updateBankInfo(bankInfo);
    }

    @Override
    public int deleteBankInfoByIds(String ids)
    {
        if (StringUtils.isEmpty(ids))
        {
            return 0;
        }
        String[] idArray = ids.split(",");
        int count = 0;
        for (String id : idArray)
        {
            if (StringUtils.isNotEmpty(id))
            {
                count += bankInfoMapper.deleteBankInfoById(Long.parseLong(id.trim()));
            }
        }
        return count;
    }
}

