package com.ruoyi.web.controller.farmer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.IBuyerRequirementService;

/**
 * 农户获取买家需求 API
 */
@RestController
@RequestMapping("/farmer/api/buyerRequirements")
public class FarmerBuyerRequirementApiController extends BaseController
{
    @Autowired
    private IBuyerRequirementService buyerRequirementService;

    @GetMapping("/list")
    public AjaxResult list()
    {
        return AjaxResult.success(buyerRequirementService.selectAll());
    }
}