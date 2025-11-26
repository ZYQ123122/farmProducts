package com.ruoyi.web.controller.system;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Arrays;
import java.util.List;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.shiro.service.SysRegisterService;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 注册验证
 *
 * @author ruoyi
 */
@Controller
public class SysRegisterController extends BaseController
{
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    // 合法的用户角色类型（新表结构使用role字段）
    private static final List<String> VALID_ROLES = Arrays.asList("farmer", "expert", "bank", "buyer");

    @GetMapping("/register")
    public String register()
    {
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public AjaxResult ajaxRegister(@Valid SysUser user)
    {
        // 1. 检查系统是否开启注册功能
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return error("当前系统没有开启注册功能！");
        }

        // 2. 校验 role 是否存在且合法（新表结构使用role字段）
        String role = user.getRole();
        if (StringUtils.isEmpty(role))
        {
            return error("请选择用户身份");
        }
        if (!VALID_ROLES.contains(role))
        {
            return error("用户身份不合法，请选择正确的身份类型");
        }

        // 3. 设置username（新表结构使用username字段）
        if (StringUtils.isEmpty(user.getUsername()))
        {
            user.setUsername(user.getLoginName());
        }

        // 4. 调用注册服务（传递包含 role 的 user 对象）
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success("注册成功") : error(msg);
    }
}