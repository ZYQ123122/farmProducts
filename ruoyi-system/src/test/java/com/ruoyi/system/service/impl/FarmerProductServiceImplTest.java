package com.ruoyi.system.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.FarmerProduct;
import com.ruoyi.system.mapper.FarmerProductMapper;

/**
 * 农户产品服务实现测试类
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("农户产品服务实现测试")
class FarmerProductServiceImplTest
{
    @Mock
    private FarmerProductMapper farmerProductMapper;

    @InjectMocks
    private FarmerProductServiceImpl farmerProductService;

    private Long testFarmerId = 1L;

    @BeforeEach
    void setUp()
    {
        // 初始化设置
    }

    @Test
    @DisplayName("测试查询农户产品列表-无状态过滤")
    void testSelectFarmerProductList_NoStatus()
    {
        List<FarmerProduct> products = new ArrayList<>();
        FarmerProduct product = createTestProduct(1L, "苹果", "on_shelf");
        products.add(product);
        
        when(farmerProductMapper.selectFarmerProductList(testFarmerId, null))
            .thenReturn(products);
        
        List<FarmerProduct> result = farmerProductService.selectFarmerProductList(testFarmerId, null);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("苹果", result.get(0).getName());
    }

    @Test
    @DisplayName("测试查询农户产品列表-按状态过滤")
    void testSelectFarmerProductList_WithStatus()
    {
        List<FarmerProduct> products = new ArrayList<>();
        FarmerProduct product = createTestProduct(1L, "苹果", "on_shelf");
        products.add(product);
        
        when(farmerProductMapper.selectFarmerProductList(testFarmerId, "on_shelf"))
            .thenReturn(products);
        
        List<FarmerProduct> result = farmerProductService.selectFarmerProductList(testFarmerId, "on_shelf");
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("测试查询农户产品列表-非法状态")
    void testSelectFarmerProductList_InvalidStatus()
    {
        assertThrows(ServiceException.class, () -> {
            farmerProductService.selectFarmerProductList(testFarmerId, "invalid_status");
        });
    }

    @Test
    @DisplayName("测试查询所有上架商品")
    void testSelectAvailableProducts()
    {
        List<FarmerProduct> products = new ArrayList<>();
        FarmerProduct product = createTestProduct(1L, "苹果", "on_shelf");
        products.add(product);
        
        when(farmerProductMapper.selectAvailableProductList())
            .thenReturn(products);
        
        List<FarmerProduct> result = farmerProductService.selectAvailableProducts();
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("测试根据ID查询产品-成功")
    void testSelectFarmerProductById_Success()
    {
        FarmerProduct product = createTestProduct(1L, "苹果", "on_shelf");
        
        when(farmerProductMapper.selectFarmerProductById(1L, testFarmerId))
            .thenReturn(product);
        
        FarmerProduct result = farmerProductService.selectFarmerProductById(1L, testFarmerId);
        
        assertNotNull(result);
        assertEquals("苹果", result.getName());
    }

    @Test
    @DisplayName("测试根据ID查询产品-ID为空")
    void testSelectFarmerProductById_NullId()
    {
        FarmerProduct result = farmerProductService.selectFarmerProductById(null, testFarmerId);
        
        assertNull(result);
    }

    @Test
    @DisplayName("测试新增产品-成功")
    void testInsertFarmerProduct_Success()
    {
        FarmerProduct product = createTestProduct(null, "新苹果", null);
        
        when(farmerProductMapper.insertFarmerProduct(any(FarmerProduct.class)))
            .thenAnswer(invocation -> {
                FarmerProduct p = invocation.getArgument(0);
                p.setId(1L);
                return 1;
            });
        
        int result = farmerProductService.insertFarmerProduct(product);
        
        assertEquals(1, result);
        assertEquals("off_shelf", product.getStatus());
        assertEquals(0, product.getIsDeleted());
        verify(farmerProductMapper).insertFarmerProduct(any(FarmerProduct.class));
    }

    @Test
    @DisplayName("测试新增产品-带状态")
    void testInsertFarmerProduct_WithStatus()
    {
        FarmerProduct product = createTestProduct(null, "新苹果", "on_shelf");
        
        when(farmerProductMapper.insertFarmerProduct(any(FarmerProduct.class)))
            .thenReturn(1);
        
        int result = farmerProductService.insertFarmerProduct(product);
        
        assertEquals(1, result);
        assertEquals("on_shelf", product.getStatus());
    }

    @Test
    @DisplayName("测试新增产品-非法状态")
    void testInsertFarmerProduct_InvalidStatus()
    {
        FarmerProduct product = createTestProduct(null, "新苹果", "invalid_status");
        
        assertThrows(ServiceException.class, () -> {
            farmerProductService.insertFarmerProduct(product);
        });
    }

    @Test
    @DisplayName("测试更新产品-成功")
    void testUpdateFarmerProduct_Success()
    {
        FarmerProduct product = createTestProduct(1L, "更新苹果", "on_shelf");
        
        when(farmerProductMapper.updateFarmerProduct(product))
            .thenReturn(1);
        
        int result = farmerProductService.updateFarmerProduct(product);
        
        assertEquals(1, result);
        verify(farmerProductMapper).updateFarmerProduct(product);
    }

    @Test
    @DisplayName("测试软删除产品-成功")
    void testSoftDeleteFarmerProduct_Success()
    {
        when(farmerProductMapper.softDeleteFarmerProduct(1L, testFarmerId))
            .thenReturn(1);
        
        int result = farmerProductService.softDeleteFarmerProduct(1L, testFarmerId);
        
        assertEquals(1, result);
        verify(farmerProductMapper).softDeleteFarmerProduct(1L, testFarmerId);
    }

    @Test
    @DisplayName("测试修改产品状态-成功")
    void testChangeProductStatus_Success()
    {
        when(farmerProductMapper.changeProductStatus(1L, testFarmerId, "on_shelf"))
            .thenReturn(1);
        
        int result = farmerProductService.changeProductStatus(1L, testFarmerId, "on_shelf");
        
        assertEquals(1, result);
        verify(farmerProductMapper).changeProductStatus(1L, testFarmerId, "on_shelf");
    }

    @Test
    @DisplayName("测试修改产品状态-非法状态")
    void testChangeProductStatus_InvalidStatus()
    {
        assertThrows(ServiceException.class, () -> {
            farmerProductService.changeProductStatus(1L, testFarmerId, "invalid_status");
        });
    }

    @Test
    @DisplayName("测试查询上架商品详情-成功")
    void testSelectAvailableProductById_Success()
    {
        FarmerProduct product = createTestProduct(1L, "苹果", "on_shelf");
        
        when(farmerProductMapper.selectAvailableProductById(1L))
            .thenReturn(product);
        
        FarmerProduct result = farmerProductService.selectAvailableProductById(1L);
        
        assertNotNull(result);
        assertEquals("苹果", result.getName());
    }

    @Test
    @DisplayName("测试查询上架商品详情-ID为空")
    void testSelectAvailableProductById_NullId()
    {
        FarmerProduct result = farmerProductService.selectAvailableProductById(null);
        
        assertNull(result);
    }

    @Test
    @DisplayName("测试扣减库存-成功")
    void testDecreaseProductStock_Success()
    {
        when(farmerProductMapper.decreaseProductStock(1L, 10))
            .thenReturn(1);
        
        boolean result = farmerProductService.decreaseProductStock(1L, 10);
        
        assertTrue(result);
        verify(farmerProductMapper).decreaseProductStock(1L, 10);
    }

    @Test
    @DisplayName("测试扣减库存-失败")
    void testDecreaseProductStock_Failure()
    {
        when(farmerProductMapper.decreaseProductStock(1L, 10))
            .thenReturn(0);
        
        boolean result = farmerProductService.decreaseProductStock(1L, 10);
        
        assertEquals(false, result);
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
}

