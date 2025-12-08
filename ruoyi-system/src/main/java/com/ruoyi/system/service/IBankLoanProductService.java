package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BankLoanProduct;

/**
 * 银行贷款产品Service接口
 */
public interface IBankLoanProductService
{
    BankLoanProduct selectBankLoanProductById(Long id);

    List<BankLoanProduct> selectBankLoanProductList(BankLoanProduct product);

    List<BankLoanProduct> selectAvailableProductList();

    List<BankLoanProduct> selectProductListByBankId(Long bankId);

    int insertBankLoanProduct(BankLoanProduct product);

    int updateBankLoanProduct(BankLoanProduct product);

    int deleteBankLoanProductByIds(String ids);
}

