package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.FarmerProduct;
import com.ruoyi.system.domain.TradeOrder;
import com.ruoyi.system.mapper.TradeOrderMapper;
import com.ruoyi.system.service.IFarmerProductService;
import com.ruoyi.system.service.ITradeOrderService;

/**
 * 订单业务实现
 */
@Service
public class TradeOrderServiceImpl implements ITradeOrderService
{
    private static final String STATUS_PENDING = "pending";
    private static final String STATUS_CONFIRMED = "confirmed";
    private static final String STATUS_COMPLETED = "completed";

    @Autowired
    private TradeOrderMapper tradeOrderMapper;

    @Autowired
    private IFarmerProductService farmerProductService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TradeOrder createOrder(Long buyerId, Long productId, int quantity)
    {
        Assert.notNull(buyerId, "买家不能为空");
        Assert.notNull(productId, "商品不能为空");
        if (quantity <= 0)
        {
            throw new ServiceException("购买数量必须大于0");
        }
        FarmerProduct product = farmerProductService.selectAvailableProductById(productId);
        if (product == null)
        {
            throw new ServiceException("商品不存在或未上架");
        }
        if (product.getStock() == null || product.getStock() <= 0)
        {
            throw new ServiceException("商品库存不足");
        }
        if (product.getStock() < quantity)
        {
            throw new ServiceException("购买数量不能超过库存数量");
        }

        boolean stockReduced = farmerProductService.decreaseProductStock(productId, quantity);
        if (!stockReduced)
        {
            throw new ServiceException("扣减库存失败，请刷新后重试");
        }

        TradeOrder order = new TradeOrder();
        order.setProductId(productId);
        order.setBuyerId(buyerId);
        order.setQuantity(quantity);
        order.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        order.setStatus(STATUS_PENDING);

        int rows = tradeOrderMapper.insertOrder(order);
        if (rows <= 0)
        {
            throw new ServiceException("创建订单失败");
        }

        TradeOrder created = tradeOrderMapper.selectOrderById(order.getId());
        return created != null ? created : order;
    }

    @Override
    public List<TradeOrder> listOrdersForBuyer(Long buyerId)
    {
        Assert.notNull(buyerId, "买家不能为空");
        return tradeOrderMapper.selectOrdersByBuyer(buyerId);
    }

    @Override
    public List<TradeOrder> listOrdersForFarmer(Long farmerId)
    {
        Assert.notNull(farmerId, "农户不能为空");
        return tradeOrderMapper.selectOrdersByFarmer(farmerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmOrder(Long orderId, Long farmerId)
    {
        Assert.notNull(orderId, "订单不能为空");
        Assert.notNull(farmerId, "农户不能为空");

        TradeOrder order = tradeOrderMapper.selectOrderById(orderId);
        if (order == null || order.getFarmerId() == null || !farmerId.equals(order.getFarmerId()))
        {
            throw new ServiceException("订单不存在或无权操作");
        }
        if (!STATUS_PENDING.equals(order.getStatus()))
        {
            throw new ServiceException("当前订单状态不可接单");
        }
        int rows = tradeOrderMapper.updateOrderStatus(orderId, STATUS_PENDING, STATUS_CONFIRMED);
        if (rows <= 0)
        {
            throw new ServiceException("订单接单失败，请刷新后重试");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeOrder(Long orderId, Long buyerId)
    {
        Assert.notNull(orderId, "订单不能为空");
        Assert.notNull(buyerId, "买家不能为空");

        TradeOrder order = tradeOrderMapper.selectOrderById(orderId);
        if (order == null || order.getBuyerId() == null || !buyerId.equals(order.getBuyerId()))
        {
            throw new ServiceException("订单不存在或无权操作");
        }
        if (!STATUS_CONFIRMED.equals(order.getStatus()))
        {
            throw new ServiceException("当前订单状态不可确认收货");
        }
        int rows = tradeOrderMapper.updateOrderStatus(orderId, STATUS_CONFIRMED, STATUS_COMPLETED);
        if (rows <= 0)
        {
            throw new ServiceException("确认收货失败，请刷新后重试");
        }
        return true;
    }
}

