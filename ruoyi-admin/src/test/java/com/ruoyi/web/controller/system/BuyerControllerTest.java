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
 * 买家Controller测试类
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("买家Controller测试")
class BuyerControllerTest
{
    @Mock
    private ISysConfigService configService;

    @Spy
    @InjectMocks
    private BuyerController buyerController;

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
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        testUser = new SysUser();
        testUser.setId(1L);
        testUser.setName("测试买家");
        testUser.setUsername("test_buyer");
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
        String result = buyerController.index(mmap, request);
        
        assertEquals("user/index", result);
        assertNotNull(mmap.get("user"));
    }

    @Test
    @DisplayName("测试主页路由")
    void testMain()
    {
        ModelMap mmap = new ModelMap();
        
        String result = buyerController.main(mmap);
        
        assertEquals("user/main", result);
        assertNotNull(mmap.get("version"));
    }

    @Test
    @DisplayName("测试需求页面路由")
    void testRequirements()
    {
        String result = buyerController.requirements();
        assertEquals("user/requirements", result);
    }

    @Test
    @DisplayName("测试采购页面路由")
    void testTradePurchase()
    {
        String result = buyerController.tradePurchase();
        assertEquals("user/trade/purchase", result);
    }

    @Test
    @DisplayName("测试联系页面路由")
    void testTradeContact()
    {
        ModelMap mmap = new ModelMap();
        mockSysUser();
        String result = buyerController.tradeContact(mmap);
        assertEquals("message/contact", result);
    }

    @Test
    @DisplayName("测试退货页面路由")
    void testAfterSalesReturn()
    {
        ModelMap mmap = new ModelMap();
        mockSysUser();
        String result = buyerController.afterSalesReturn(mmap);
        assertEquals("user/afterSales/return", result);
    }


    private void mockSysUser()
    {
        doReturn(testUser).when(buyerController).getSysUser();
    }
}



