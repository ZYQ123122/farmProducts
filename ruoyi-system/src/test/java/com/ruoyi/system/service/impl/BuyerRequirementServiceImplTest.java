package com.ruoyi.system.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ruoyi.system.domain.BuyerRequirement;
import com.ruoyi.system.mapper.BuyerRequirementMapper;

/**
 * 买家需求服务实现测试类
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("买家需求服务实现测试")
class BuyerRequirementServiceImplTest
{
    @Mock
    private BuyerRequirementMapper buyerRequirementMapper;

    @InjectMocks
    private BuyerRequirementServiceImpl buyerRequirementService;

    private Long testBuyerId = 1L;

    @BeforeEach
    void setUp()
    {
        // 初始化设置
    }

    @Test
    @DisplayName("测试根据买家ID查询需求列表")
    void testSelectByBuyerId()
    {
        List<BuyerRequirement> requirements = new ArrayList<>();
        BuyerRequirement req1 = createTestRequirement(1L, "苹果", "unsatisfied");
        BuyerRequirement req2 = createTestRequirement(2L, "香蕉", "responded");
        requirements.add(req1);
        requirements.add(req2);
        
        when(buyerRequirementMapper.selectByBuyerId(testBuyerId))
            .thenReturn(requirements);
        
        List<BuyerRequirement> result = buyerRequirementService.selectByBuyerId(testBuyerId);
        
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(buyerRequirementMapper).selectByBuyerId(testBuyerId);
    }

    @Test
    @DisplayName("测试查询所有需求")
    void testSelectAll()
    {
        List<BuyerRequirement> requirements = new ArrayList<>();
        BuyerRequirement req1 = createTestRequirement(1L, "苹果", "unsatisfied");
        requirements.add(req1);
        
        when(buyerRequirementMapper.selectAll())
            .thenReturn(requirements);
        
        List<BuyerRequirement> result = buyerRequirementService.selectAll();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(buyerRequirementMapper).selectAll();
    }

    @Test
    @DisplayName("测试创建需求-成功")
    void testCreateRequirement_Success()
    {
        BuyerRequirement requirement = createTestRequirement(null, "苹果", null);
        BuyerRequirement created = createTestRequirement(1L, "苹果", "unsatisfied");
        
        when(buyerRequirementMapper.insertBuyerRequirement(any(BuyerRequirement.class)))
            .thenAnswer(invocation -> {
                BuyerRequirement req = invocation.getArgument(0);
                req.setId(1L);
                return 1;
            });
        when(buyerRequirementMapper.selectByIdAndBuyerId(1L, testBuyerId))
            .thenReturn(created);
        
        BuyerRequirement result = buyerRequirementService.createRequirement(requirement);
        
        assertNotNull(result);
        assertEquals("unsatisfied", result.getStatus());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(buyerRequirementMapper).insertBuyerRequirement(any(BuyerRequirement.class));
        verify(buyerRequirementMapper).selectByIdAndBuyerId(1L, testBuyerId);
    }

    @Test
    @DisplayName("测试根据ID和买家ID查询需求")
    void testSelectByIdAndBuyerId()
    {
        BuyerRequirement requirement = createTestRequirement(1L, "苹果", "unsatisfied");
        
        when(buyerRequirementMapper.selectByIdAndBuyerId(1L, testBuyerId))
            .thenReturn(requirement);
        
        BuyerRequirement result = buyerRequirementService.selectByIdAndBuyerId(1L, testBuyerId);
        
        assertNotNull(result);
        assertEquals("苹果", result.getProductName());
        verify(buyerRequirementMapper).selectByIdAndBuyerId(1L, testBuyerId);
    }

    @Test
    @DisplayName("测试更新状态-成功-unsatisfied")
    void testUpdateStatus_Success_Unsatisfied()
    {
        when(buyerRequirementMapper.updateStatus(1L, testBuyerId, "unsatisfied"))
            .thenReturn(1);
        
        int result = buyerRequirementService.updateStatus(1L, testBuyerId, "unsatisfied");
        
        assertEquals(1, result);
        verify(buyerRequirementMapper).updateStatus(1L, testBuyerId, "unsatisfied");
    }

    @Test
    @DisplayName("测试更新状态-成功-responded")
    void testUpdateStatus_Success_Responded()
    {
        when(buyerRequirementMapper.updateStatus(1L, testBuyerId, "responded"))
            .thenReturn(1);
        
        int result = buyerRequirementService.updateStatus(1L, testBuyerId, "responded");
        
        assertEquals(1, result);
        verify(buyerRequirementMapper).updateStatus(1L, testBuyerId, "responded");
    }

    @Test
    @DisplayName("测试更新状态-非法状态")
    void testUpdateStatus_InvalidStatus()
    {
        int result = buyerRequirementService.updateStatus(1L, testBuyerId, "invalid_status");
        
        assertEquals(0, result);
    }

    @Test
    @DisplayName("测试更新状态-空状态")
    void testUpdateStatus_EmptyStatus()
    {
        int result = buyerRequirementService.updateStatus(1L, testBuyerId, "");
        
        assertEquals(0, result);
    }

    @Test
    @DisplayName("测试更新状态-更新失败")
    void testUpdateStatus_UpdateFailed()
    {
        when(buyerRequirementMapper.updateStatus(1L, testBuyerId, "responded"))
            .thenReturn(0);
        
        int result = buyerRequirementService.updateStatus(1L, testBuyerId, "responded");
        
        assertEquals(0, result);
    }

    private BuyerRequirement createTestRequirement(Long id, String productName, String status)
    {
        BuyerRequirement requirement = new BuyerRequirement();
        requirement.setId(id);
        requirement.setBuyerId(testBuyerId);
        requirement.setProductName(productName);
        requirement.setQuantity(100);
        requirement.setSpecs("规格要求");
        requirement.setStatus(status);
        requirement.setCreatedAt(new Date());
        requirement.setUpdatedAt(new Date());
        return requirement;
    }
}

