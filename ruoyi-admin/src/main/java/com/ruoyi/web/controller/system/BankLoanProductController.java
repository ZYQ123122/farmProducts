package com.ruoyi.web.controller.system;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.BankLoanProduct;
import com.ruoyi.system.service.IBankInfoService;
import com.ruoyi.system.service.IBankLoanProductService;

/**
 * 银行贷款产品Controller
 */
@Controller
@RequestMapping("/system/bank/product")
public class BankLoanProductController extends BaseController
{
    @Autowired
    private IBankLoanProductService bankLoanProductService;

    @Autowired
    private IBankInfoService bankInfoService;

    /**
     * 农户端：获取可用的贷款产品列表
     */
    @GetMapping("/available")
    @ResponseBody
    public AjaxResult getAvailableProducts()
    {
        List<BankLoanProduct> list = bankLoanProductService.selectAvailableProductList();
        return success(list);
    }

    /**
     * 银行端：查询产品列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BankLoanProduct product)
    {
        // 支持通过ID查询单个产品（编辑页面用）
        if (product.getId() != null)
        {
            BankLoanProduct single = bankLoanProductService.selectBankLoanProductById(product.getId());
            if (single != null)
            {
                TableDataInfo dataTable = new TableDataInfo();
                dataTable.setRows(java.util.Arrays.asList(single));
                dataTable.setTotal(1);
                return dataTable;
            }
        }
        // 如果是银行用户，只查询自己银行的产品
        Long userId = ShiroUtils.getUserId();
        com.ruoyi.system.domain.BankInfo bankInfo = bankInfoService.selectBankInfoByUserId(userId);
        if (bankInfo != null)
        {
            product.setBankId(bankInfo.getId());
        }
        startPage();
        List<BankLoanProduct> list = bankLoanProductService.selectBankLoanProductList(product);
        return getDataTable(list);
    }

    /**
     * 银行端：新增产品
     */
    @PostMapping("/add")
    @ResponseBody
    @Log(title = "银行贷款产品", businessType = BusinessType.INSERT)
    public AjaxResult add(BankLoanProduct product)
    {
        // 参数验证
        if (product.getProductName() == null || product.getProductName().trim().isEmpty())
        {
            return error("产品名称不能为空");
        }
        if (product.getMinAmount() == null || product.getMinAmount().compareTo(java.math.BigDecimal.ZERO) < 0)
        {
            return error("最小贷款金额必须大于等于0");
        }
        if (product.getMaxAmount() == null || product.getMaxAmount().compareTo(product.getMinAmount()) < 0)
        {
            return error("最大贷款金额必须大于等于最小贷款金额");
        }
        if (product.getMinTermMonths() == null || product.getMinTermMonths() < 1)
        {
            return error("最短期限必须大于等于1个月");
        }
        if (product.getMaxTermMonths() == null || product.getMaxTermMonths() < product.getMinTermMonths())
        {
            return error("最长期限必须大于等于最短期限");
        }
        
        // 设置银行ID
        Long userId = ShiroUtils.getUserId();
        com.ruoyi.system.domain.BankInfo bankInfo = bankInfoService.selectBankInfoByUserId(userId);
        if (bankInfo == null)
        {
            return error("当前用户不是银行用户");
        }
        product.setBankId(bankInfo.getId());
        product.setCreateBy(ShiroUtils.getLoginName());
        
        // 设置默认值
        if (product.getStatus() == null || product.getStatus().isEmpty())
        {
            product.setStatus("1"); // 默认上架
        }
        if (product.getSortOrder() == null)
        {
            product.setSortOrder(0);
        }
        
        int rows = bankLoanProductService.insertBankLoanProduct(product);
        if (rows > 0)
        {
            return success("新增产品成功");
        }
        return error("新增产品失败");
    }

    /**
     * 银行端：修改产品
     */
    @PostMapping("/edit")
    @ResponseBody
    @Log(title = "银行贷款产品", businessType = BusinessType.UPDATE)
    public AjaxResult edit(BankLoanProduct product)
    {
        product.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(bankLoanProductService.updateBankLoanProduct(product));
    }

    /**
     * 银行端：删除产品
     */
    @PostMapping("/remove")
    @ResponseBody
    @Log(title = "银行贷款产品", businessType = BusinessType.DELETE)
    public AjaxResult remove(String ids)
    {
        return toAjax(bankLoanProductService.deleteBankLoanProductByIds(ids));
    }
}

