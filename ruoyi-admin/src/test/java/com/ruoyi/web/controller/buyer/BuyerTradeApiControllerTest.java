package com.ruoyi.web.controller.buyer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.FarmerProduct;
import com.ruoyi.system.domain.TradeOrder;
import com.ruoyi.system.service.IFarmerProductService;
import com.ruoyi.system.service.ITradeOrderService;

import static com.ruoyi.common.core.domain.AjaxResult.CODE_TAG;
import static com.ruoyi.common.core.domain.AjaxResult.MSG_TAG;
import static com.ruoyi.common.core.domain.AjaxResult.DATA_TAG;

/**
 * 买家交易API Controller测试类
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("买家交易API Controller测试")
class BuyerTradeApiControllerTest
{
    @Mock
    private IFarmerProductService farmerProductService;

    @Mock
    private ITradeOrderService tradeOrderService;

    @Spy
    @InjectMocks
    private BuyerTradeApiController buyerTradeApiController;

    private Long testBuyerId = 1L;

    @Test
    @DisplayName("测试获取商品列表")
    void testListProducts()
    {
        List<FarmerProduct> products = new ArrayList<>();
        FarmerProduct product1 = createTestProduct(1L, "苹果", "on_shelf");
        FarmerProduct product2 = createTestProduct(2L, "香蕉", "on_shelf");
        products.add(product1);
        products.add(product2);
        
        when(farmerProductService.selectAvailableProducts())
            .thenReturn(products);
        
        AjaxResult result = buyerTradeApiController.listProducts();
        
        assertEquals(0, result.get(CODE_TAG));
        assertNotNull(result.get(DATA_TAG));
        @SuppressWarnings("unchecked")
        List<FarmerProduct> resultProducts = (List<FarmerProduct>) result.get(DATA_TAG);
        assertEquals(2, resultProducts.size());
    }

    @Test
    @DisplayName("测试获取商品详情-成功")
    void testProductDetail_Success()
    {
        FarmerProduct product = createTestProduct(1L, "苹果", "on_shelf");
        
        when(farmerProductService.selectAvailableProductById(1L))
            .thenReturn(product);
        
        AjaxResult result = buyerTradeApiController.productDetail(1L);
        
        assertEquals(0, result.get(CODE_TAG));
        assertNotNull(result.get(DATA_TAG));
        FarmerProduct resultProduct = (FarmerProduct) result.get(DATA_TAG);
        assertEquals("苹果", resultProduct.getName());
    }

    @Test
    @DisplayName("测试获取商品详情-商品不存在")
    void testProductDetail_NotFound()
    {
        when(farmerProductService.selectAvailableProductById(999L))
            .thenReturn(null);
        
        AjaxResult result = buyerTradeApiController.productDetail(999L);
        
        assertEquals(500, result.get(CODE_TAG));
        assertEquals("商品不存在或未上架", result.get(MSG_TAG));
    }

    @Test
    @DisplayName("测试获取订单列表")
    void testListOrders()
    {
        List<TradeOrder> orders = new ArrayList<>();
        TradeOrder order1 = createTestOrder(1L, "pending");
        TradeOrder order2 = createTestOrder(2L, "confirmed");
        orders.add(order1);
        orders.add(order2);
        
        when(tradeOrderService.listOrdersForBuyer(testBuyerId))
            .thenReturn(orders);
        
        mockUserId();
        AjaxResult result = buyerTradeApiController.listOrders();
        
        assertEquals(0, result.get(CODE_TAG));
        assertNotNull(result.get(DATA_TAG));
        @SuppressWarnings("unchecked")
        List<TradeOrder> resultOrders = (List<TradeOrder>) result.get(DATA_TAG);
        assertEquals(2, resultOrders.size());
    }

    @Test
    @DisplayName("测试创建订单-成功")
    void testCreateOrder_Success()
    {
        BuyerTradeApiController.OrderCreateRequest request = 
            new BuyerTradeApiController.OrderCreateRequest();
        request.setProductId(1L);
        request.setQuantity(10);
        
        TradeOrder order = createTestOrder(1L, "pending");
        
        when(tradeOrderService.createOrder(testBuyerId, 1L, 10))
            .thenReturn(order);
        
        mockUserId();
        AjaxResult result = buyerTradeApiController.createOrder(request);
        
        assertEquals(0, result.get(CODE_TAG));
        assertEquals("订单创建成功", result.get(MSG_TAG));
        assertNotNull(result.get(DATA_TAG));
        verify(tradeOrderService).createOrder(testBuyerId, 1L, 10);
    }

    @Test
    @DisplayName("测试确认收货-成功")
    void testCompleteOrder_Success()
    {
        when(tradeOrderService.completeOrder(1L, testBuyerId)).thenReturn(true);
        
        mockUserId();
        AjaxResult result = buyerTradeApiController.completeOrder(1L);
        
        assertEquals(0, result.get(CODE_TAG));
        assertEquals("确认收货成功", result.get(MSG_TAG));
        verify(tradeOrderService).completeOrder(1L, testBuyerId);
    }

    private FarmerProduct createTestProduct(Long id, String name, String status)
    {
        FarmerProduct product = new FarmerProduct();
        product.setId(id);
        product.setFarmerId(2L);
        product.setName(name);
        product.setPrice(new BigDecimal("10.50"));
        product.setStock(100);
        product.setStatus(status);
        product.setDescription("测试商品描述");
        product.setIsDeleted(0);
        return product;
    }

    private TradeOrder createTestOrder(Long id, String status)
    {
        TradeOrder order = new TradeOrder();
        order.setId(id);
        order.setProductId(1L);
        order.setBuyerId(testBuyerId);
        order.setFarmerId(2L);
        order.setQuantity(10);
        order.setTotalPrice(new BigDecimal("105.00"));
        order.setStatus(status);
        order.setCreatedAt(new Date());
        return order;
    }

    private void mockUserId()
    {
        org.mockito.Mockito.doReturn(testBuyerId).when(buyerTradeApiController).getUserId();
    }
}

