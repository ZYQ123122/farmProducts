package com.ruoyi.web.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 银行端页面路由
 */
@Controller
@RequestMapping("/guest")
public class BankController
{
    @GetMapping("/index")
    public String index()
    {
        return "guest/index";
    }

    @GetMapping("/finance/list")
    public String financeList()
    {
        return "guest/finance_list";
    }

    @GetMapping("/finance/review")
    public String financeReview()
    {
        return "guest/finance_review";
    }

    @GetMapping("/product/manage")
    public String productManage()
    {
        return "guest/product/manage";
    }

    @GetMapping("/product/add")
    public String productAdd()
    {
        return "guest/product/add";
    }

    @GetMapping("/product/edit")
    public String productEdit()
    {
        return "guest/product/edit";
    }
}