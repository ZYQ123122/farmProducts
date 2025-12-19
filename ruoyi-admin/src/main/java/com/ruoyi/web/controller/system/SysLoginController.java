package com.ruoyi.web.controller.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.ConfigService;
import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.service.ISysLogininforService;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@Controller
public class SysLoginController extends BaseController
{
    /**
     * 是否开启记住我功能
     */
    @Value("${shiro.rememberMe.enabled: false}")
    private boolean rememberMe;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ISysLogininforService logininforService;

    /**
     * 角色到重定向路径的映射
     */
    private static final Map<String, String> ROLE_REDIRECT_MAP = new HashMap<>();
    static {
        ROLE_REDIRECT_MAP.put("farmer", "/farmer/index");
        ROLE_REDIRECT_MAP.put("expert", "/manager/index");
        ROLE_REDIRECT_MAP.put("bank", "/guest/index");
        ROLE_REDIRECT_MAP.put("buyer", "/user/index");
        ROLE_REDIRECT_MAP.put("admin", "/index");
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, ModelMap mmap)
    {
        if (ServletUtils.isAjaxRequest(request))
        {
            return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }
        mmap.put("isRemembered", rememberMe);
        mmap.put("isAllowRegister", Convert.toBool(configService.getKey("sys.account.registerUser"), true));
        mmap.put("captchaEnabled", true);
        mmap.put("captchaType", "math");
        return "login";
    }


    @PostMapping("/login")
    @ResponseBody
    public AjaxResult doLogin(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String validateCode,
            @RequestParam(required = false) Boolean rememberMe,
            HttpServletRequest request,
            ModelMap mmap)
    {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe != null && rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try
        {
            subject.login(token);
            
            // 从Shiro获取登录后的用户信息（包含数据库中的role）
            com.ruoyi.common.core.domain.entity.SysUser loggedInUser = (com.ruoyi.common.core.domain.entity.SysUser) subject.getPrincipal();
            String userRole = loggedInUser != null && loggedInUser.getRole() != null ? loggedInUser.getRole() : (role != null ? role : "admin");
            
            // 记录登录成功日志（如果失败不影响登录）
            try
            {
                SysLogininfor logininfor = new SysLogininfor();
                logininfor.setLoginName(username);
                logininfor.setIpaddr(ServletUtils.getClientIP(request));
                logininfor.setLoginLocation(getLoginLocation(ServletUtils.getClientIP(request)));
                logininfor.setBrowser(ServletUtils.getBrowser(request));
                logininfor.setOs(ServletUtils.getOs(request));
                logininfor.setStatus("0"); // 0 表示成功
                logininfor.setMsg("登录成功");
                logininfor.setRole(userRole); // 使用数据库中的角色
                logininforService.insertLogininfor(logininfor);
            }
            catch (Exception e)
            {
                // 日志记录失败不影响登录流程
                logger.warn("记录登录日志失败", e);
            }

            // 根据用户实际角色获取重定向路径
            String redirect = ROLE_REDIRECT_MAP.getOrDefault(userRole, "/index");
            return AjaxResult.success().put("redirect", redirect);
        }
        catch (AuthenticationException e)
        {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage()))
            {
                msg = e.getMessage();
            }
            // 记录登录失败日志
            try
            {
                SysLogininfor logininfor = new SysLogininfor();
                logininfor.setLoginName(username);
                logininfor.setIpaddr(ServletUtils.getClientIP(request));
                logininfor.setLoginLocation(getLoginLocation(ServletUtils.getClientIP(request)));
                logininfor.setBrowser(ServletUtils.getBrowser(request));
                logininfor.setOs(ServletUtils.getOs(request));
                logininfor.setStatus("1"); // 1 表示失败
                logininfor.setMsg(msg);
                logininfor.setRole(role != null ? role : "unknown"); // 记录登录身份
                logininforService.insertLogininfor(logininfor);
            }
            catch (Exception ex)
            {
                logger.warn("记录登录失败日志异常", ex);
            }

            return AjaxResult.error(msg);
        }
        catch (Exception e)
        {
            // 捕获其他所有异常，避免请求失败
            logger.error("登录过程中发生异常", e);
            String msg = "登录失败，请稍后重试";
            if (StringUtils.isNotEmpty(e.getMessage()))
            {
                msg = "登录失败：" + e.getMessage();
            }
            return AjaxResult.error(msg);
        }
    }

    /**
     * 获取登录地点（简化处理，实际应调用外部服务）
     *
     * @param ip IP地址
     * @return 登录地点
     */
    private String getLoginLocation(String ip)
    {
        return StringUtils.equals(ip, "127.0.0.1") ? "内网IP" : "未知地点";
    }

    @GetMapping("/unauth")
    public String unauth()
    {
        return "error/unauth";
    }
}