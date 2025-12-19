package com.ruoyi.web.controller.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 农户Controller测试类
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("农户Controller测试")
class FarmerControllerTest
{
    @Mock
    private ISysConfigService configService;

    @Spy
    @InjectMocks
    private FarmerController farmerController;

    private SysUser testUser;
    private MockHttpServletRequest request;
    private MockHttpSession session;

    @BeforeEach
    void setUp()
    {
        request = new MockHttpServletRequest();
        session = new MockHttpSession();
        request.setSession(session);
        request.addHeader("User-Agent", "JUnit");
        @SuppressWarnings("null")
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        testUser = new SysUser();
        testUser.setId(1L);
        testUser.setName("测试农户");
        testUser.setUsername("test_farmer");
        // 新表结构：不再有pwdUpdateDate字段

        new RuoYiConfig().setVersion("1.0.0");
    }

    @AfterEach
    void tearDown()
    {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    @DisplayName("测试首页路由")
    void testIndex()
    {
        ModelMap mmap = new ModelMap();
        
        when(configService.selectConfigByKey("sys.index.sideTheme")).thenReturn("default");
        when(configService.selectConfigByKey("sys.index.skinName")).thenReturn("blue");
        when(configService.selectConfigByKey("sys.index.footer")).thenReturn("true");
        when(configService.selectConfigByKey("sys.index.tagsView")).thenReturn("true");
        when(configService.selectConfigByKey("sys.account.initPasswordModify")).thenReturn("0");
        when(configService.selectConfigByKey("sys.account.passwordValidateDays")).thenReturn("90");
        mockSysUser();
        String result = farmerController.index(mmap, request);
        
        assertEquals("farmer/index", result);
        assertNotNull(mmap.get("user"));
    }

    @Test
    @DisplayName("测试主页路由")
    void testMain()
    {
        ModelMap mmap = new ModelMap();
        
        String result = farmerController.main(mmap);
        
        assertEquals("farmer/main", result);
        assertNotNull(mmap.get("version"));
    }

    @Test
    @DisplayName("测试产品信息页面路由")
    void testProductInfo()
    {
        String result = farmerController.productInfo();
        assertEquals("farmer/product/info", result);
    }

    @Test
    @DisplayName("测试产品管理页面路由")
    void testProductManage()
    {
        String result = farmerController.productManage();
        assertEquals("farmer/product/manage", result);
    }

    @Test
    @DisplayName("测试买家需求页面路由")
    void testProductDemand()
    {
        String result = farmerController.productDemand();
        assertEquals("farmer/product/demand", result);
    }

    @Test
    @DisplayName("测试联系买家页面路由")
    void testProductContact()
    {
        String result = farmerController.productContact();
        assertEquals("farmer/product/contact", result);
    }

    @Test
    @DisplayName("测试订单管理页面路由")
    void testOrderManage()
    {
        String result = farmerController.orderManage();
        assertEquals("farmer/order/manage", result);
    }

    @Test
    @DisplayName("测试预约专家页面路由")
    void testExpertAppointment()
    {
        String result = farmerController.expertAppointment();
        assertEquals("farmer/expert/appointment", result);
    }

    @Test
    @DisplayName("测试询问专家页面路由")
    void testExpertInquiry()
    {
        String result = farmerController.expertInquiry();
        assertEquals("farmer/expert/inquiry", result);
    }

    @Test
    @DisplayName("测试查看知识库页面路由")
    void testExpertKnowledge()
    {
        String result = farmerController.expertKnowledge();
        assertEquals("farmer/expert/knowledge", result);
    }

    @Test
    @DisplayName("测试匹配农户页面路由")
    void testFinanceMatch()
    {
        String result = farmerController.financeMatch();
        assertEquals("farmer/finance/match", result);
    }

    @Test
    @DisplayName("测试融资申请页面路由")
    void testFinanceApply()
    {
        String result = farmerController.financeApply();
        assertEquals("farmer/finance/apply", result);
    }

    @Test
    @DisplayName("测试社区首页路由")
    void testCommunityIndex()
    {
        String result = farmerController.communityIndex();
        assertEquals("farmer/community/index", result);
    }

    private void mockSysUser()
    {
        doReturn(testUser).when(farmerController).getSysUser();
    }
}



