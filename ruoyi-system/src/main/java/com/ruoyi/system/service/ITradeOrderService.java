package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.system.domain.TradeOrder;

/**
 * 订单业务接口
 */
public interface ITradeOrderService
{
    /**
     * 买家创建订单
     */
    TradeOrder createOrder(Long buyerId, Long productId, int quantity);

    /**
     * 买家订单列表
     */
    List<TradeOrder> listOrdersForBuyer(Long buyerId);

    /**
     * 农户订单列表
     */
    List<TradeOrder> listOrdersForFarmer(Long farmerId);

    /**
     * 农户确认订单
     */
    boolean confirmOrder(Long orderId, Long farmerId);

    /**
     * 买家确认收货
     */
    boolean completeOrder(Long orderId, Long buyerId);
}

