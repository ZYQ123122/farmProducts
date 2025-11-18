package com.ruoyi.web.controller.farmer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.TradeOrder;
import com.ruoyi.system.service.ITradeOrderService;

import static com.ruoyi.common.core.domain.AjaxResult.CODE_TAG;
import static com.ruoyi.common.core.domain.AjaxResult.MSG_TAG;
import static com.ruoyi.common.core.domain.AjaxResult.DATA_TAG;

/**
 * 农户订单API Controller测试类
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("农户订单API Controller测试")
class FarmerOrderApiControllerTest
{
    @Mock
    private ITradeOrderService tradeOrderService;

    @Spy
    @InjectMocks
    private FarmerOrderApiController farmerOrderApiController;

    private Long testFarmerId = 1L;

    @BeforeEach
    void setUp()
    {
        doReturn(testFarmerId).when(farmerOrderApiController).getUserId();
    }

    @Test
    @DisplayName("测试获取订单列表")
    void testList()
    {
        List<TradeOrder> orders = new ArrayList<>();
        TradeOrder order1 = createTestOrder(1L, "pending");
        TradeOrder order2 = createTestOrder(2L, "confirmed");
        orders.add(order1);
        orders.add(order2);
        
        when(tradeOrderService.listOrdersForFarmer(testFarmerId))
            .thenReturn(orders);
        
        AjaxResult result = farmerOrderApiController.list();
        
        assertEquals(0, result.get(CODE_TAG));
        assertNotNull(result.get(DATA_TAG));
        @SuppressWarnings("unchecked")
        List<TradeOrder> resultOrders = (List<TradeOrder>) result.get(DATA_TAG);
        assertEquals(2, resultOrders.size());
    }

    @Test
    @DisplayName("测试接单-成功")
    void testConfirm_Success()
    {
        doReturn(true).when(tradeOrderService).confirmOrder(1L, testFarmerId);
        
        AjaxResult result = farmerOrderApiController.confirm(1L);
        
        assertEquals(0, result.get(CODE_TAG));
        assertEquals("成功接单", result.get(MSG_TAG));
        verify(tradeOrderService).confirmOrder(1L, testFarmerId);
    }

    private TradeOrder createTestOrder(Long id, String status)
    {
        TradeOrder order = new TradeOrder();
        order.setId(id);
        order.setProductId(1L);
        order.setBuyerId(2L);
        order.setFarmerId(testFarmerId);
        order.setQuantity(10);
        order.setTotalPrice(new BigDecimal("100.00"));
        order.setStatus(status);
        order.setCreatedAt(new Date());
        return order;
    }
}



