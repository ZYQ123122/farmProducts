package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BankInfo;

/**
 * 银行信息Mapper接口
 */
public interface BankInfoMapper
{
    /**
     * 查询银行信息
     */
    BankInfo selectBankInfoById(Long id);

    /**
     * 根据用户ID查询银行信息
     */
    BankInfo selectBankInfoByUserId(Long userId);

    /**
     * 根据银行代码查询银行信息
     */
    BankInfo selectBankInfoByCode(String bankCode);

    /**
     * 查询银行信息列表
     */
    List<BankInfo> selectBankInfoList(BankInfo bankInfo);

    /**
     * 新增银行信息
     */
    int insertBankInfo(BankInfo bankInfo);

    /**
     * 修改银行信息
     */
    int updateBankInfo(BankInfo bankInfo);

    /**
     * 删除银行信息
     */
    int deleteBankInfoById(Long id);
}

