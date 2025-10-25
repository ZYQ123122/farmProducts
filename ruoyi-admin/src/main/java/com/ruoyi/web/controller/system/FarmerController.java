package com.ruoyi.web.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/farmer")
public class FarmerController {

    @GetMapping("/index")
    public String index() {
        return "farmer/index";
    }
}