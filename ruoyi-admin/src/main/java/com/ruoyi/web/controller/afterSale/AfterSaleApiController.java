package com.ruoyi.web.controller.afterSale;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.AfterSale;
import com.ruoyi.system.service.IAfterSaleService;
import com.ruoyi.system.service.ITradeOrderService;
import com.ruoyi.system.domain.TradeOrder;

/**
 * 售后API Controller
 */
@RestController
@RequestMapping("/api/afterSale")
public class AfterSaleApiController extends BaseController
{
    @Autowired
    private IAfterSaleService afterSaleService;

    @Autowired
    private ITradeOrderService tradeOrderService;

    /**
     * 获取买家的售后列表
     */
    @GetMapping("/buyer/list")
    public AjaxResult getBuyerAfterSaleList()
    {
        try
        {
            SysUser user = getSysUser();
            if (user == null)
            {
                return AjaxResult.error("未登录");
            }
            List<AfterSale> list = afterSaleService.selectAfterSaleListByBuyer(getUserId());
            return AjaxResult.success(list);
        }
        catch (Exception e)
        {
            logger.error("获取买家售后列表失败", e);
            return AjaxResult.error("获取售后列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取农户的售后列表
     */
    @GetMapping("/farmer/list")
    public AjaxResult getFarmerAfterSaleList()
    {
        try
        {
            SysUser user = getSysUser();
            if (user == null)
            {
                return AjaxResult.error("未登录");
            }
            List<AfterSale> list = afterSaleService.selectAfterSaleListByFarmer(getUserId());
            return AjaxResult.success(list);
        }
        catch (Exception e)
        {
            logger.error("获取农户售后列表失败", e);
            return AjaxResult.error("获取售后列表失败：" + e.getMessage());
        }
    }

    /**
     * 创建退货申请
     */
    @PostMapping("/create")
    public AjaxResult createAfterSale(@RequestParam Long orderId,
                                      @RequestParam String issue)
    {
        try
        {
            SysUser user = getSysUser();
            if (user == null)
            {
                return AjaxResult.error("未登录");
            }

            // 验证订单是否存在且属于当前用户
            TradeOrder order = tradeOrderService.listOrdersForBuyer(getUserId()).stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElse(null);

            if (order == null)
            {
                return AjaxResult.error("订单不存在或不属于当前用户");
            }

            // 检查是否已有售后申请
            AfterSale existing = new AfterSale();
            existing.setOrderId(orderId);
            existing.setBuyerId(getUserId());
            List<AfterSale> existingList = afterSaleService.selectAfterSaleList(existing);
            if (existingList != null && !existingList.isEmpty())
            {
                // 检查是否有待处理的申请
                boolean hasPending = existingList.stream()
                    .anyMatch(a -> "pending".equals(a.getStatus()));
                if (hasPending)
                {
                    return AjaxResult.error("该订单已有待处理的退货申请");
                }
            }

            // 创建售后申请
            AfterSale afterSale = new AfterSale();
            afterSale.setOrderId(orderId);
            afterSale.setFarmerId(order.getFarmerId());
            afterSale.setBuyerId(getUserId());
            afterSale.setIssue(issue);
            afterSale.setStatus("pending");

            int result = afterSaleService.insertAfterSale(afterSale);
            if (result > 0)
            {
                AfterSale saved = afterSaleService.selectAfterSaleById(afterSale.getId());
                return AjaxResult.success("申请成功", saved);
            }
            else
            {
                return AjaxResult.error("申请失败");
            }
        }
        catch (Exception e)
        {
            logger.error("创建退货申请失败", e);
            return AjaxResult.error("创建退货申请失败：" + e.getMessage());
        }
    }

    /**
     * 处理售后申请（接受或拒绝）
     */
    @PostMapping("/process")
    public AjaxResult processAfterSale(@RequestParam Long id,
                                       @RequestParam boolean accept)
    {
        try
        {
            SysUser user = getSysUser();
            if (user == null)
            {
                return AjaxResult.error("未登录");
            }

            int result = afterSaleService.processAfterSale(id, getUserId(), accept);
            if (result > 0)
            {
                AfterSale afterSale = afterSaleService.selectAfterSaleById(id);
                String message = accept ? "退货成功" : "退货失败";
                return AjaxResult.success(message, afterSale);
            }
            else
            {
                return AjaxResult.error("处理失败，可能售后申请不存在或已被处理");
            }
        }
        catch (Exception e)
        {
            logger.error("处理售后申请失败", e);
            return AjaxResult.error("处理售后申请失败：" + e.getMessage());
        }
    }
}





