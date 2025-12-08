package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BankLoanProduct;

/**
 * 银行贷款产品Mapper接口
 */
public interface BankLoanProductMapper
{
    /**
     * 查询贷款产品
     */
    BankLoanProduct selectBankLoanProductById(Long id);

    /**
     * 查询贷款产品列表（包含银行名称）
     */
    List<BankLoanProduct> selectBankLoanProductList(BankLoanProduct product);

    /**
     * 查询上架的贷款产品列表（农户端使用）
     */
    List<BankLoanProduct> selectAvailableProductList();

    /**
     * 根据银行ID查询产品列表
     */
    List<BankLoanProduct> selectProductListByBankId(Long bankId);

    /**
     * 新增贷款产品
     */
    int insertBankLoanProduct(BankLoanProduct product);

    /**
     * 修改贷款产品
     */
    int updateBankLoanProduct(BankLoanProduct product);

    /**
     * 删除贷款产品
     */
    int deleteBankLoanProductById(Long id);
}

