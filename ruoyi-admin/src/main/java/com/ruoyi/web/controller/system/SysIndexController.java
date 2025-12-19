package com.ruoyi.web.controller.system;

import java.util.ArrayList;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.ShiroConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.CookieUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.shiro.service.SysPasswordService;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 首页 业务处理
 * 
 * @author ruoyi
 */
@Controller
public class SysIndexController extends BaseController
{
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private SysPasswordService passwordService;

    // 系统首页
    @GetMapping("/index")
    public String index(ModelMap mmap, HttpServletRequest request)
    {
        try
        {
            // 取身份信息
            SysUser user = getSysUser();
            if (user == null)
            {
                // 如果用户未登录，重定向到登录页
                return "redirect:/login";
            }
            
            // 根据用户角色重定向到对应的首页
            String userRole = user.getRole();
            if (userRole != null)
            {
                if ("farmer".equals(userRole))
                {
                    return "redirect:/farmer/index";
                }
                else if ("buyer".equals(userRole))
                {
                    return "redirect:/user/index";
                }
                else if ("expert".equals(userRole))
                {
                    return "redirect:/manager/index";
                }
                else if ("bank".equals(userRole))
                {
                    return "redirect:/guest/index";
                }
            }
            
            // 管理员或默认情况：显示管理员首页
            // 简化：不再使用菜单系统
            mmap.put("menus", new ArrayList<>());
            mmap.put("user", user);
            
            // 安全地获取配置，如果失败则使用默认值
            try
            {
                mmap.put("sideTheme", getConfigValue("sys.index.sideTheme", "dark"));
                mmap.put("skinName", getConfigValue("sys.index.skinName", "blue"));
                Boolean footer = Convert.toBool(getConfigValue("sys.index.footer", "true"), true);
                Boolean tagsView = Convert.toBool(getConfigValue("sys.index.tagsView", "true"), true);
                mmap.put("footer", footer);
                mmap.put("tagsView", tagsView);
                mmap.put("mainClass", contentMainClass(footer, tagsView));
            }
            catch (Exception e)
            {
                // 配置服务失败时使用默认值
                mmap.put("sideTheme", "dark");
                mmap.put("skinName", "blue");
                mmap.put("footer", true);
                mmap.put("tagsView", true);
                mmap.put("mainClass", "");
            }
            
            mmap.put("copyrightYear", RuoYiConfig.getCopyrightYear());
            mmap.put("demoEnabled", RuoYiConfig.isDemoEnabled());
            // 新表结构：不再有pwdUpdateDate字段
            mmap.put("isDefaultModifyPwd", false);
            mmap.put("isPasswordExpired", false);
            mmap.put("isMobile", ServletUtils.checkAgentIsMobile(ServletUtils.getRequest().getHeader("User-Agent")));

            // 菜单导航显示风格
            String menuStyle = getConfigValue("sys.index.menuStyle", "index");
            // 移动端，默认使左侧导航菜单，否则取默认配置
            String indexStyle = ServletUtils.checkAgentIsMobile(ServletUtils.getRequest().getHeader("User-Agent")) ? "index" : (menuStyle != null ? menuStyle : "index");

            // 优先Cookie配置导航菜单
            Cookie[] cookies = ServletUtils.getRequest().getCookies();
            if (cookies != null)
            {
                for (Cookie cookie : cookies)
                {
                    if (StringUtils.isNotEmpty(cookie.getName()) && "nav-style".equalsIgnoreCase(cookie.getName()))
                    {
                        indexStyle = cookie.getValue();
                        break;
                    }
                }
            }
            String webIndex = "topnav".equalsIgnoreCase(indexStyle) ? "index-topnav" : "index";
            // CSRF Token
            request.getSession().setAttribute(ShiroConstants.CSRF_TOKEN, ServletUtils.generateToken());
            return webIndex;
        }
        catch (Exception e)
        {
            // 记录异常并重定向到登录页
            logger.error("访问首页时发生异常", e);
            return "redirect:/login";
        }
    }
    
    /**
     * 安全地获取配置值，如果失败则返回默认值
     */
    private String getConfigValue(String key, String defaultValue)
    {
        try
        {
            String value = configService.selectConfigByKey(key);
            return StringUtils.isNotEmpty(value) ? value : defaultValue;
        }
        catch (Exception e)
        {
            logger.warn("获取配置 {} 失败，使用默认值: {}", key, defaultValue, e);
            return defaultValue;
        }
    }

    // 锁定屏幕
    @GetMapping("/lockscreen")
    public String lockscreen(ModelMap mmap)
    {
        mmap.put("user", getSysUser());
        ServletUtils.getSession().setAttribute(ShiroConstants.LOCK_SCREEN, true);
        return "lock";
    }

    // 解锁屏幕
    @PostMapping("/unlockscreen")
    @ResponseBody
    public AjaxResult unlockscreen(String password)
    {
        SysUser user = getSysUser();
        if (StringUtils.isNull(user))
        {
            return AjaxResult.error("服务器超时，请重新登录");
        }
        if (passwordService.matches(user, password))
        {
            ServletUtils.getSession().removeAttribute(ShiroConstants.LOCK_SCREEN);
            return AjaxResult.success();
        }
        return AjaxResult.error("密码不正确，请重新输入。");
    }

    // 切换主题
    @GetMapping("/system/switchSkin")
    public String switchSkin()
    {
        return "skin";
    }

    // 切换菜单
    @GetMapping("/system/menuStyle/{style}")
    public void menuStyle(@PathVariable String style, HttpServletResponse response)
    {
        CookieUtils.setCookie(response, "nav-style", style);
    }

    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap)
    {
        mmap.put("version", RuoYiConfig.getVersion());
        return "main";
    }

    // content-main class
    public String contentMainClass(Boolean footer, Boolean tagsView)
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
