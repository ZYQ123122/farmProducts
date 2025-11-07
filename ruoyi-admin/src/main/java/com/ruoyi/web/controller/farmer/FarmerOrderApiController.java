package com.ruoyi.web.controller.farmer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.TradeOrder;
import com.ruoyi.system.service.ITradeOrderService;

/**
 * 农户订单管理API
 */
@RestController
@RequestMapping("/farmer/api/order")
public class FarmerOrderApiController extends BaseController
{
    @Autowired
    private ITradeOrderService tradeOrderService;

    /**
     * 农户订单列表
     */
    @GetMapping("/list")
    public AjaxResult list()
    {
        Long farmerId = getUserId();
        List<TradeOrder> orders = tradeOrderService.listOrdersForFarmer(farmerId);
        return AjaxResult.success(orders);
    }

    /**
     * 农户接单
     */
    @PutMapping("/{id}/confirm")
    public AjaxResult confirm(@PathVariable("id") Long id)
    {
        Long farmerId = getUserId();
        tradeOrderService.confirmOrder(id, farmerId);
        return AjaxResult.success("订单接单成功");
    }
}

