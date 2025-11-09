package com.ruoyi.system.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.FarmerProduct;
import com.ruoyi.system.domain.TradeOrder;
import com.ruoyi.system.mapper.TradeOrderMapper;
import com.ruoyi.system.service.IFarmerProductService;

@ExtendWith(MockitoExtension.class)
class TradeOrderServiceImplTest
{
    @InjectMocks
    private TradeOrderServiceImpl tradeOrderService;

    @Mock
    private TradeOrderMapper tradeOrderMapper;

    @Mock
    private IFarmerProductService farmerProductService;

    @Test
    @DisplayName("创建订单成功时应扣减库存并返回持久化后的订单")
    void createOrder_success()
    {
        FarmerProduct product = new FarmerProduct();
        product.setId(1L);
        product.setFarmerId(9L);
        product.setStock(5);
        product.setPrice(new BigDecimal("12.50"));

        when(farmerProductService.selectAvailableProductById(1L)).thenReturn(product);
        when(farmerProductService.decreaseProductStock(1L, 2)).thenReturn(true);
        when(tradeOrderMapper.insertOrder(any(TradeOrder.class))).thenAnswer(invocation -> {
            TradeOrder order = invocation.getArgument(0);
            order.setId(100L);
            return 1;
        });
        TradeOrder persistedOrder = new TradeOrder();
        persistedOrder.setId(100L);
        persistedOrder.setBuyerId(3L);
        persistedOrder.setFarmerId(9L);
        persistedOrder.setProductId(1L);
        persistedOrder.setQuantity(2);
        persistedOrder.setTotalPrice(new BigDecimal("25.00"));
        persistedOrder.setStatus("pending");
        when(tradeOrderMapper.selectOrderById(100L)).thenReturn(persistedOrder);

        TradeOrder result = tradeOrderService.createOrder(3L, 1L, 2);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(new BigDecimal("25.00"), result.getTotalPrice());
        verify(farmerProductService).decreaseProductStock(1L, 2);
        verify(tradeOrderMapper).insertOrder(any(TradeOrder.class));
        verify(tradeOrderMapper).selectOrderById(100L);
    }

    @Test
    @DisplayName("购买数量超过库存时应抛出异常")
    void createOrder_exceedsStock()
    {
        FarmerProduct product = new FarmerProduct();
        product.setId(1L);
        product.setStock(1);
        product.setPrice(new BigDecimal("10.00"));

        when(farmerProductService.selectAvailableProductById(1L)).thenReturn(product);

        ServiceException exception = assertThrows(ServiceException.class,
                () -> tradeOrderService.createOrder(3L, 1L, 5));

        assertEquals("购买数量不能超过库存数量", exception.getMessage());
    }

    @Test
    @DisplayName("农户接单时需校验订单归属")
    void confirmOrder_success()
    {
        TradeOrder order = new TradeOrder();
        order.setId(200L);
        order.setFarmerId(9L);
        order.setStatus("pending");
        when(tradeOrderMapper.selectOrderById(200L)).thenReturn(order);
        doReturn(1).when(tradeOrderMapper).updateOrderStatus(200L, "pending", "confirmed");

        boolean result = tradeOrderService.confirmOrder(200L, 9L);

        assertEquals(true, result);
        verify(tradeOrderMapper).updateOrderStatus(200L, "pending", "confirmed");
    }

    @Test
    @DisplayName("确认收货仅允许已确认状态的订单")
    void completeOrder_requiresConfirmed()
    {
        TradeOrder order = new TradeOrder();
        order.setId(300L);
        order.setBuyerId(5L);
        order.setStatus("pending");
        when(tradeOrderMapper.selectOrderById(300L)).thenReturn(order);

        ServiceException exception = assertThrows(ServiceException.class,
                () -> tradeOrderService.completeOrder(300L, 5L));

        assertEquals("当前订单状态不可确认收货", exception.getMessage());
    }
}

