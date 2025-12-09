package com.ruoyi.web.controller.system;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.ShiroConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysConfigService;

@Controller
@RequestMapping("/user")
public class BuyerController extends BaseController
{

    @Autowired
    private ISysConfigService configService;

    @GetMapping("/index")
    public String index(ModelMap mmap, HttpServletRequest request)
    {
        SysUser user = getSysUser();
        mmap.put("user", user);
        mmap.put("sideTheme", configService.selectConfigByKey("sys.index.sideTheme"));
        mmap.put("skinName", configService.selectConfigByKey("sys.index.skinName"));
        Boolean footer = Convert.toBool(configService.selectConfigByKey("sys.index.footer"), true);
        Boolean tagsView = Convert.toBool(configService.selectConfigByKey("sys.index.tagsView"), true);
        mmap.put("footer", footer);
        mmap.put("tagsView", tagsView);
        mmap.put("mainClass", contentMainClass(footer, tagsView));
        mmap.put("copyrightYear", RuoYiConfig.getCopyrightYear());
        mmap.put("demoEnabled", RuoYiConfig.isDemoEnabled());
        // 新表结构：不再有pwdUpdateDate字段
        mmap.put("isDefaultModifyPwd", false);
        mmap.put("isPasswordExpired", false);
        boolean isMobile = ServletUtils.checkAgentIsMobile(ServletUtils.getRequest().getHeader("User-Agent"));
        mmap.put("isMobile", isMobile);
        request.getSession().setAttribute(ShiroConstants.CSRF_TOKEN, ServletUtils.generateToken());
        return "user/index";
    }

    @GetMapping("/main")
    public String main(ModelMap mmap)
    {
        mmap.put("version", RuoYiConfig.getVersion());
        return "user/main";
    }

    @GetMapping("/home")
    public String home()
    {
        return "user/home";
    }

    @GetMapping("/requirements")
    public String requirements()
    {
        return "user/requirements";
    }

    @GetMapping("/trade/purchase")
    public String tradePurchase()
    {
        return "user/trade/purchase";
    }

    @GetMapping("/trade/contact")
    public String tradeContact(ModelMap mmap)
    {
        SysUser user = getSysUser();
        if (user == null)
        {
            return "redirect:/login";
        }
        mmap.put("user", user);
        return "message/contact";
    }

    @GetMapping("/after-sales/return")
    public String afterSalesReturn(ModelMap mmap)
    {
        SysUser user = getSysUser();
        if (user == null)
        {
            return "redirect:/login";
        }
        mmap.put("user", user);
        return "user/afterSales/return";
    }

    private String contentMainClass(Boolean footer, Boolean tagsView)
    {
        if (!footer && !tagsView)
        {
            return "tagsview-footer-hide";
        }
        else if (!footer)
        {
            return "footer-hide";
        }
        else if (!tagsView)
        {
            return "tagsview-hide";
        }
        return StringUtils.EMPTY;
    }

}