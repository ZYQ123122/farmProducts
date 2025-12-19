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
@RequestMapping("/manager")
public class ExpertController extends BaseController {

    @Autowired
    private ISysConfigService configService;

    /**
     * 专家工作台首页
     */
    @GetMapping("/index")
    public String index(ModelMap mmap, HttpServletRequest request) {
        try
        {
            SysUser user = getSysUser();
            if (user == null)
            {
                return "redirect:/login";
            }
            mmap.put("user", user);
            
            // 安全地获取配置值
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
            boolean isMobile = ServletUtils.checkAgentIsMobile(ServletUtils.getRequest().getHeader("User-Agent"));
            mmap.put("isMobile", isMobile);
            request.getSession().setAttribute(ShiroConstants.CSRF_TOKEN, ServletUtils.generateToken());
            return "manager/index";
        }
        catch (Exception e)
        {
            logger.error("访问专家首页时发生异常", e);
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

    /**
     * 专家主内容页面
     */
    @GetMapping("/main")
    public String main(ModelMap mmap) {
        mmap.put("version", RuoYiConfig.getVersion());
        return "manager/main";
    }

    /**
     * 专家咨询回复页面
     */
    @GetMapping("/consultation")
    public String consultation() {
        return "manager/consultation";
    }

    /**
     * 知识库管理页面
     */
    @GetMapping("/knowledge")
    public String knowledge() {
        return "manager/knowledge";
    }

    /**
     * 我的专家资料页面
     */
    @GetMapping("/profile")
    public String profile() {
        return "manager/profile";
    }

    /**
     * 预约处理页面
     */
    @GetMapping("/appointment")
    public String appointment() {
        return "manager/appointment";
    }

    private String contentMainClass(Boolean footer, Boolean tagsView) {
        if (!footer && !tagsView) {
            return "tagsview-footer-hide";
        } else if (!footer) {
            return "footer-hide";
        } else if (!tagsView) {
            return "tagsview-hide";
        }
        return StringUtils.EMPTY;
    }
}