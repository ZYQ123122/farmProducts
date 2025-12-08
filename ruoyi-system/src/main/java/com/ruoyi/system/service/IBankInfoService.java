package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BankInfo;

/**
 * 银行信息Service接口
 */
public interface IBankInfoService
{
    BankInfo selectBankInfoById(Long id);

    BankInfo selectBankInfoByUserId(Long userId);

    List<BankInfo> selectBankInfoList(BankInfo bankInfo);

    int insertBankInfo(BankInfo bankInfo);

    int updateBankInfo(BankInfo bankInfo);

    int deleteBankInfoByIds(String ids);
}

