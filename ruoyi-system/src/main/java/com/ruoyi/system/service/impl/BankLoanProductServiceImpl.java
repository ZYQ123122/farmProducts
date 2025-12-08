package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BankLoanProduct;
import com.ruoyi.system.mapper.BankLoanProductMapper;
import com.ruoyi.system.service.IBankLoanProductService;

/**
 * 银行贷款产品Service业务层处理
 */
@Service
public class BankLoanProductServiceImpl implements IBankLoanProductService
{
    @Autowired
    private BankLoanProductMapper bankLoanProductMapper;

    @Override
    public BankLoanProduct selectBankLoanProductById(Long id)
    {
        return bankLoanProductMapper.selectBankLoanProductById(id);
    }

    @Override
    public List<BankLoanProduct> selectBankLoanProductList(BankLoanProduct product)
    {
        return bankLoanProductMapper.selectBankLoanProductList(product);
    }

    @Override
    public List<BankLoanProduct> selectAvailableProductList()
    {
        return bankLoanProductMapper.selectAvailableProductList();
    }

    @Override
    public List<BankLoanProduct> selectProductListByBankId(Long bankId)
    {
        return bankLoanProductMapper.selectProductListByBankId(bankId);
    }

    @Override
    public int insertBankLoanProduct(BankLoanProduct product)
    {
        return bankLoanProductMapper.insertBankLoanProduct(product);
    }

    @Override
    public int updateBankLoanProduct(BankLoanProduct product)
    {
        return bankLoanProductMapper.updateBankLoanProduct(product);
    }

    @Override
    public int deleteBankLoanProductByIds(String ids)
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
                count += bankLoanProductMapper.deleteBankLoanProductById(Long.parseLong(id.trim()));
            }
        }
        return count;
    }
}

