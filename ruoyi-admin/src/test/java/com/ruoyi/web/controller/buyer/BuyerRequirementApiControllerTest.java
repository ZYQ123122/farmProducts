package com.ruoyi.web.controller.buyer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
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
import com.ruoyi.system.domain.BuyerRequirement;
import com.ruoyi.system.service.IBuyerRequirementService;

import static com.ruoyi.common.core.domain.AjaxResult.CODE_TAG;
import static com.ruoyi.common.core.domain.AjaxResult.MSG_TAG;
import static com.ruoyi.common.core.domain.AjaxResult.DATA_TAG;

/**
 * 买家需求API Controller测试类
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("买家需求API Controller测试")
class BuyerRequirementApiControllerTest
{
    @Mock
    private IBuyerRequirementService buyerRequirementService;

    @Spy
    @InjectMocks
    private BuyerRequirementApiController buyerRequirementApiController;

    private Long testBuyerId = 1L;

    @Test
    @DisplayName("测试获取需求列表")
    void testList()
    {
        List<BuyerRequirement> requirements = new ArrayList<>();
        BuyerRequirement req1 = createTestRequirement(1L, "苹果", "unsatisfied");
        BuyerRequirement req2 = createTestRequirement(2L, "香蕉", "responded");
        requirements.add(req1);
        requirements.add(req2);
        
        when(buyerRequirementService.selectByBuyerId(testBuyerId))
            .thenReturn(requirements);
        
        mockUserId();
        AjaxResult result = buyerRequirementApiController.list();
        
        assertEquals(0, result.get(CODE_TAG));
        assertNotNull(result.get(DATA_TAG));
        @SuppressWarnings("unchecked")
        List<BuyerRequirement> resultRequirements = (List<BuyerRequirement>) result.get(DATA_TAG);
        assertEquals(2, resultRequirements.size());
    }

    @Test
    @DisplayName("测试创建需求-成功")
    void testAdd_Success()
    {
        BuyerRequirement requirement = createTestRequirement(null, "苹果", null);
        BuyerRequirement created = createTestRequirement(1L, "苹果", "unsatisfied");
        
        when(buyerRequirementService.createRequirement(requirement))
            .thenReturn(created);
        
        mockUserId();
        AjaxResult result = buyerRequirementApiController.add(requirement);
        
        assertEquals(0, result.get(CODE_TAG));
        assertEquals("创建成功", result.get(MSG_TAG));
        assertNotNull(result.get(DATA_TAG));
        verify(buyerRequirementService).createRequirement(requirement);
    }

    @Test
    @DisplayName("测试创建需求-失败")
    void testAdd_Failure()
    {
        BuyerRequirement requirement = createTestRequirement(null, "苹果", null);
        
        when(buyerRequirementService.createRequirement(requirement))
            .thenReturn(null);
        
        mockUserId();
        AjaxResult result = buyerRequirementApiController.add(requirement);
        
        assertEquals(500, result.get(CODE_TAG));
        assertEquals("创建失败", result.get(MSG_TAG));
    }

    @Test
    @DisplayName("测试修改需求状态-成功")
    void testChangeStatus_Success()
    {
        Map<String, String> body = new HashMap<>();
        body.put("status", "responded");
        
        BuyerRequirement requirement = createTestRequirement(1L, "苹果", "responded");
        
        when(buyerRequirementService.updateStatus(1L, testBuyerId, "responded"))
            .thenReturn(1);
        when(buyerRequirementService.selectByIdAndBuyerId(1L, testBuyerId))
            .thenReturn(requirement);
        
        mockUserId();
        AjaxResult result = buyerRequirementApiController.changeStatus(1L, body);
        
        assertEquals(0, result.get(CODE_TAG));
        assertEquals("状态更新成功", result.get(MSG_TAG));
        assertNotNull(result.get(DATA_TAG));
    }

    @Test
    @DisplayName("测试修改需求状态-状态为空")
    void testChangeStatus_EmptyStatus()
    {
        Map<String, String> body = new HashMap<>();
        body.put("status", "");
        
        AjaxResult result = buyerRequirementApiController.changeStatus(1L, body);
        
        assertEquals(500, result.get(CODE_TAG));
        assertEquals("状态不能为空", result.get(MSG_TAG));
    }

    @Test
    @DisplayName("测试修改需求状态-更新失败")
    void testChangeStatus_UpdateFailed()
    {
        Map<String, String> body = new HashMap<>();
        body.put("status", "responded");
        
        when(buyerRequirementService.updateStatus(1L, testBuyerId, "responded"))
            .thenReturn(0);
        
        mockUserId();
        AjaxResult result = buyerRequirementApiController.changeStatus(1L, body);
        
        assertEquals(500, result.get(CODE_TAG));
        assertEquals("状态更新失败", result.get(MSG_TAG));
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

    private void mockUserId()
    {
        org.mockito.Mockito.doReturn(testBuyerId).when(buyerRequirementApiController).getUserId();
    }
}

