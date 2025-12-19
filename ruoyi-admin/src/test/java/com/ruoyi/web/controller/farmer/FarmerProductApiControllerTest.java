package com.ruoyi.web.controller.farmer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.FarmerProduct;
import com.ruoyi.system.service.IFarmerProductService;

import static com.ruoyi.common.core.domain.AjaxResult.CODE_TAG;
import static com.ruoyi.common.core.domain.AjaxResult.MSG_TAG;
import static com.ruoyi.common.core.domain.AjaxResult.DATA_TAG;

/**
 * 农户产品API Controller测试类
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("农户产品API Controller测试")
class FarmerProductApiControllerTest
{
    @Mock
    private IFarmerProductService farmerProductService;

    @Spy
    @InjectMocks
    private FarmerProductApiController farmerProductApiController;

    private Long testFarmerId = 1L;

    @Test
    @DisplayName("测试获取产品列表-无状态过滤")
    void testList_NoStatus()
    {
        mockUserId();
        List<FarmerProduct> products = new ArrayList<>();
        FarmerProduct product1 = createTestProduct(1L, "苹果", "on_shelf");
        FarmerProduct product2 = createTestProduct(2L, "香蕉", "off_shelf");
        products.add(product1);
        products.add(product2);
        
        when(farmerProductService.selectFarmerProductList(testFarmerId, null))
            .thenReturn(products);
        
        AjaxResult result = farmerProductApiController.list(null);
        
        assertEquals(0, result.get(CODE_TAG));
        assertNotNull(result.get(DATA_TAG));
        @SuppressWarnings("unchecked")
        List<FarmerProduct> resultProducts = (List<FarmerProduct>) result.get(DATA_TAG);
        assertEquals(2, resultProducts.size());
    }

    @Test
    @DisplayName("测试获取产品列表-按状态过滤")
    void testList_WithStatus()
    {
        mockUserId();
        List<FarmerProduct> products = new ArrayList<>();
        FarmerProduct product = createTestProduct(1L, "苹果", "on_shelf");
        products.add(product);
        
        when(farmerProductService.selectFarmerProductList(testFarmerId, "on_shelf"))
            .thenReturn(products);
        
        AjaxResult result = farmerProductApiController.list("on_shelf");
        
        assertEquals(0, result.get(CODE_TAG));
        assertNotNull(result.get(DATA_TAG));
    }

    @Test
    @DisplayName("测试获取产品详情-成功")
    void testDetail_Success()
    {
        mockUserId();
        FarmerProduct product = createTestProduct(1L, "苹果", "on_shelf");
        
        when(farmerProductService.selectFarmerProductById(1L, testFarmerId))
            .thenReturn(product);
        
        AjaxResult result = farmerProductApiController.detail(1L);
        
        assertEquals(0, result.get(CODE_TAG));
        assertNotNull(result.get(DATA_TAG));
        FarmerProduct resultProduct = (FarmerProduct) result.get(DATA_TAG);
        assertEquals("苹果", resultProduct.getName());
    }

    @Test
    @DisplayName("测试获取产品详情-产品不存在")
    void testDetail_NotFound()
    {
        mockUserId();
        when(farmerProductService.selectFarmerProductById(999L, testFarmerId))
            .thenReturn(null);
        
        AjaxResult result = farmerProductApiController.detail(999L);
        
        assertEquals(500, result.get(CODE_TAG));
        assertEquals("商品不存在或已删除", result.get(MSG_TAG));
    }

    @Test
    @DisplayName("测试添加产品-成功")
    void testAdd_Success()
    {
        mockUserId();
        FarmerProduct newProduct = createTestProduct(null, "新苹果", "off_shelf");
        FarmerProduct savedProduct = createTestProduct(1L, "新苹果", "off_shelf");
        
        when(farmerProductService.insertFarmerProduct(any(FarmerProduct.class)))
            .thenAnswer(invocation -> {
                FarmerProduct p = invocation.getArgument(0);
                p.setId(1L);
                return 1;
            });
        when(farmerProductService.selectFarmerProductById(1L, testFarmerId))
            .thenReturn(savedProduct);
        
        AjaxResult result = farmerProductApiController.add(newProduct);
        
        assertEquals(0, result.get(CODE_TAG));
        assertEquals("创建成功", result.get(MSG_TAG));
        assertNotNull(result.get(DATA_TAG));
    }

    @Test
    @DisplayName("测试添加产品-失败")
    void testAdd_Failure()
    {
        mockUserId();
        FarmerProduct newProduct = createTestProduct(null, "新苹果", "off_shelf");
        
        when(farmerProductService.insertFarmerProduct(any(FarmerProduct.class)))
            .thenReturn(0);
        
        AjaxResult result = farmerProductApiController.add(newProduct);
        
        assertEquals(500, result.get(CODE_TAG));
        assertEquals("创建失败", result.get(MSG_TAG));
    }

    @Test
    @DisplayName("测试更新产品-成功")
    void testEdit_Success()
    {
        mockUserId();
        FarmerProduct existingProduct = createTestProduct(1L, "苹果", "on_shelf");
        FarmerProduct updateProduct = createTestProduct(1L, "更新苹果", "on_shelf");
        FarmerProduct updatedProduct = createTestProduct(1L, "更新苹果", "on_shelf");
        
        when(farmerProductService.selectFarmerProductById(1L, testFarmerId))
            .thenReturn(existingProduct)
            .thenReturn(updatedProduct);
        when(farmerProductService.updateFarmerProduct(any(FarmerProduct.class)))
            .thenReturn(1);
        
        AjaxResult result = farmerProductApiController.edit(1L, updateProduct);
        
        assertEquals(0, result.get(CODE_TAG));
        assertEquals("更新成功", result.get(MSG_TAG));
    }

    @Test
    @DisplayName("测试更新产品-产品不存在")
    void testEdit_NotFound()
    {
        mockUserId();
        FarmerProduct updateProduct = createTestProduct(1L, "更新苹果", "on_shelf");
        
        when(farmerProductService.selectFarmerProductById(999L, testFarmerId))
            .thenReturn(null);
        
        AjaxResult result = farmerProductApiController.edit(999L, updateProduct);
        
        assertEquals(500, result.get(CODE_TAG));
        assertEquals("商品不存在或已删除", result.get(MSG_TAG));
    }

    @Test
    @DisplayName("测试删除产品-成功")
    void testRemove_Success()
    {
        mockUserId();
        when(farmerProductService.softDeleteFarmerProduct(1L, testFarmerId))
            .thenReturn(1);
        
        AjaxResult result = farmerProductApiController.remove(1L);
        
        assertEquals(0, result.get(CODE_TAG));
    }

    @Test
    @DisplayName("测试修改产品状态-成功")
    void testChangeStatus_Success()
    {
        mockUserId();
        Map<String, String> body = new HashMap<>();
        body.put("status", "on_shelf");
        
        FarmerProduct product = createTestProduct(1L, "苹果", "on_shelf");
        
        when(farmerProductService.changeProductStatus(1L, testFarmerId, "on_shelf"))
            .thenReturn(1);
        when(farmerProductService.selectFarmerProductById(1L, testFarmerId))
            .thenReturn(product);
        
        AjaxResult result = farmerProductApiController.changeStatus(1L, body);
        
        assertEquals(0, result.get(CODE_TAG));
        assertEquals("状态更新成功", result.get(MSG_TAG));
    }

    @Test
    @DisplayName("测试修改产品状态-状态参数为空")
    void testChangeStatus_EmptyStatus()
    {
        Map<String, String> body = new HashMap<>();
        
        AjaxResult result = farmerProductApiController.changeStatus(1L, body);
        
        assertEquals(500, result.get(CODE_TAG));
        assertEquals("状态参数不能为空", result.get(MSG_TAG));
    }

    private FarmerProduct createTestProduct(Long id, String name, String status)
    {
        FarmerProduct product = new FarmerProduct();
        product.setId(id);
        product.setFarmerId(testFarmerId);
        product.setName(name);
        product.setPrice(new BigDecimal("10.50"));
        product.setStock(100);
        product.setStatus(status);
        product.setDescription("测试商品描述");
        product.setIsDeleted(0);
        return product;
    }

    private void mockUserId()
    {
        org.mockito.Mockito.doReturn(testFarmerId).when(farmerProductApiController).getUserId();
    }
}

