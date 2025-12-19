package com.ruoyi.web.controller.farmer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.BuyerRequirement;
import com.ruoyi.system.service.IBuyerRequirementService;

import static com.ruoyi.common.core.domain.AjaxResult.CODE_TAG;
import static com.ruoyi.common.core.domain.AjaxResult.DATA_TAG;

/**
 * 农户买家需求API Controller测试类
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("农户买家需求API Controller测试")
class FarmerBuyerRequirementApiControllerTest
{
    @Mock
    private IBuyerRequirementService buyerRequirementService;

    @Spy
    @InjectMocks
    private FarmerBuyerRequirementApiController farmerBuyerRequirementApiController;

    @BeforeEach
    void setUp()
    {
        // 初始化设置
    }

    @Test
    @DisplayName("测试获取买家需求列表")
    void testList()
    {
        List<BuyerRequirement> requirements = new ArrayList<>();
        BuyerRequirement req1 = createTestRequirement(1L, "苹果", "unsatisfied");
        BuyerRequirement req2 = createTestRequirement(2L, "香蕉", "responded");
        requirements.add(req1);
        requirements.add(req2);
        
        when(buyerRequirementService.selectAll())
            .thenReturn(requirements);
        
        AjaxResult result = farmerBuyerRequirementApiController.list();
        
        assertEquals(0, result.get(CODE_TAG));
        assertNotNull(result.get(DATA_TAG));
        @SuppressWarnings("unchecked")
        List<BuyerRequirement> resultRequirements = (List<BuyerRequirement>) result.get(DATA_TAG);
        assertEquals(2, resultRequirements.size());
    }

    private BuyerRequirement createTestRequirement(Long id, String productName, String status)
    {
        BuyerRequirement requirement = new BuyerRequirement();
        requirement.setId(id);
        requirement.setBuyerId(1L);
        requirement.setProductName(productName);
        requirement.setQuantity(100);
        requirement.setStatus(status);
        requirement.setCreatedAt(new Date());
        requirement.setUpdatedAt(new Date());
        return requirement;
    }
}



