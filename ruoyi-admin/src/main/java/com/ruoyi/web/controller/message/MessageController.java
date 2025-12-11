package com.ruoyi.web.controller.message;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysUser;

/**
 * 消息Controller
 */
@Controller
@RequestMapping("/message")
public class MessageController extends BaseController
{
    @GetMapping("/contact")
    public String contact(ModelMap mmap, HttpServletRequest request)
    {
        SysUser user = getSysUser();
        if (user == null)
        {
            return "redirect:/login";
        }
        mmap.put("user", user);
        return "message/contact";
    }
}

