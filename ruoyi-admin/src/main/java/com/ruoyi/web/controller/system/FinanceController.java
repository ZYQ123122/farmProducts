package com.ruoyi.web.controller.system;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.FinanceApplication;
import com.ruoyi.system.domain.FinanceCollateral;
import com.ruoyi.system.service.IFinanceApplicationService;
import com.ruoyi.system.service.FinanceCollateralQueryService;
import com.ruoyi.system.service.IBankLoanProductService;
import com.ruoyi.system.service.IFinanceNotificationService;
import com.ruoyi.system.service.IBankInfoService;

/**
 * 融资申请相关接口（农户端 + 银行端）
 */
@Controller
@RequestMapping("/system/finance")
public class FinanceController extends BaseController
{
    @Autowired
    private IFinanceApplicationService financeApplicationService;

    @Autowired
    private FinanceCollateralQueryService financeCollateralQueryService;

    @Autowired
    private IBankLoanProductService bankLoanProductService;

    @Autowired
    private IFinanceNotificationService financeNotificationService;

    @Autowired
    private IBankInfoService bankInfoService;

    /**
     * 农户提交融资申请
     */
    @PostMapping("/apply")
    @ResponseBody
    public AjaxResult apply(Long productId, BigDecimal amount, Integer termMonths, String purpose,
                            String collateralDesc, String attachmentUrls, String attachmentNames)
    {
        if (productId == null)
        {
            return error("请选择贷款产品");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
        {
            return error("请输入正确的申请金额");
        }
        if (termMonths == null || termMonths <= 0)
        {
            return error("请输入贷款期限（月）");
        }

        // 验证产品是否存在并获取银行ID
        com.ruoyi.system.domain.BankLoanProduct product = bankLoanProductService.selectBankLoanProductById(productId);
        if (product == null || !"1".equals(product.getStatus()))
        {
            return error("选择的贷款产品不存在或已下架");
        }

        FinanceApplication app = new FinanceApplication();
        app.setFarmerId(ShiroUtils.getUserId());
        app.setProductId(productId);
        app.setBankId(product.getBankId());
        app.setAmount(amount);
        app.setTermMonths(termMonths);
        app.setPurpose(purpose);
        app.setCollateralDesc(collateralDesc);
        app.setStatus("submitted");

        List<FinanceCollateral> collaterals = new ArrayList<>();
        if (StringUtils.isNotEmpty(attachmentUrls))
        {
            String[] urls = attachmentUrls.split(",");
            String[] names = StringUtils.isNotEmpty(attachmentNames) ? attachmentNames.split(",") : new String[urls.length];
            for (int i = 0; i < urls.length; i++)
            {
                String url = urls[i].trim();
                if (StringUtils.isEmpty(url))
                {
                    continue;
                }
                FinanceCollateral c = new FinanceCollateral();
                c.setFileUrl(url);
                if (i < names.length)
                {
                    c.setFileName(names[i]);
                }
                collaterals.add(c);
            }
        }

        int rows = financeApplicationService.insertFinanceApplication(app, collaterals);

        // 发送提交通知
        if (rows > 0)
        {
            com.ruoyi.system.domain.FinanceNotification notification = new com.ruoyi.system.domain.FinanceNotification();
            notification.setApplicationId(app.getId());
            notification.setUserId(app.getFarmerId());
            notification.setNotificationType("submitted");
            notification.setTitle("贷款申请已提交");
            notification.setContent("您的贷款申请已成功提交，请等待银行审批。");
            notification.setIsRead("0");
            financeNotificationService.insertFinanceNotification(notification);
        }

        return toAjax(rows);
    }

    /**
     * 农户查看自己的融资申请列表
     */
    @PostMapping("/myList")
    @ResponseBody
    public TableDataInfo myList(String status)
    {
        FinanceApplication query = new FinanceApplication();
        query.setFarmerId(ShiroUtils.getUserId());
        if (StringUtils.isNotEmpty(status))
        {
            query.setStatus(status);
        }
        startPage();
        List<FinanceApplication> list = financeApplicationService.selectFinanceApplicationList(query);
        return getDataTable(list);
    }

    /**
     * 银行查看融资申请列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(String status)
    {
        FinanceApplication query = new FinanceApplication();

        // 如果是银行用户，只显示该银行产品的申请
        Long userId = ShiroUtils.getUserId();
        com.ruoyi.system.domain.BankInfo bankInfo = bankInfoService.selectBankInfoByUserId(userId);
        if (bankInfo != null)
        {
            query.setBankId(bankInfo.getId());
        }

        if (StringUtils.isNotEmpty(status))
        {
            query.setStatus(status);
        }
        startPage();
        List<FinanceApplication> list = financeApplicationService.selectFinanceApplicationList(query);
        return getDataTable(list);
    }

    /**
     * 银行审批融资申请
     */
    @PostMapping("/review")
    @ResponseBody
    public AjaxResult review(Long id, String decision, String bankComment)
    {
        if (id == null)
        {
            return error("参数错误");
        }
        if (!"approve".equals(decision) && !"reject".equals(decision))
        {
            return error("不支持的审批结果");
        }

        FinanceApplication app = financeApplicationService.selectFinanceApplicationById(id);
        if (app == null)
        {
            return error("融资申请不存在");
        }
        app.setReviewerId(ShiroUtils.getUserId());
        app.setBankComment(bankComment);
        String newStatus = "approve".equals(decision) ? "approved" : "rejected";
        app.setStatus(newStatus);

        int rows = financeApplicationService.updateFinanceApplication(app);

        // 发送审批结果通知
        if (rows > 0)
        {
            financeNotificationService.sendApprovalNotification(app.getId(), app.getFarmerId(), newStatus, bankComment);
        }

        return toAjax(rows);
    }

    /**
     * 查询单个申请及其附件（银行审批详情页用）
     */
    @GetMapping("/detail")
    @ResponseBody
    public AjaxResult detail(Long id)
    {
        if (id == null)
        {
            return error("参数错误：缺少申请ID");
        }
        FinanceApplication app = financeApplicationService.selectFinanceApplicationById(id);
        if (app == null)
        {
            return error("融资申请不存在");
        }
        AjaxResult result = success(app);
        result.put("collateralList", financeCollateralQueryService.selectByApplicationId(id));

        // 如果有关联的产品，查询产品信息
        if (app.getProductId() != null)
        {
            com.ruoyi.system.domain.BankLoanProduct product = bankLoanProductService.selectBankLoanProductById(app.getProductId());
            if (product != null)
            {
                result.put("product", product);
            }
        }

        return result;
    }
}
