package com.ruoyi.web.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class ExpertController {

    /**
     * 专家工作台首页
     */
    @GetMapping("/index")
    public String index() {
        return "manager/index";
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
}