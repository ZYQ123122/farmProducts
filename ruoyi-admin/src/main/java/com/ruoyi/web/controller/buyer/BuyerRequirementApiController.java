package com.ruoyi.web.controller.buyer;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BuyerRequirement;
import com.ruoyi.system.service.IBuyerRequirementService;

/**
 * 买家需求API
 */
@RestController
@RequestMapping("/buyer/api/requirements")
@Validated
public class BuyerRequirementApiController extends BaseController
{
    @Autowired
    private IBuyerRequirementService buyerRequirementService;

    @GetMapping("/list")
    public AjaxResult list()
    {
        Long buyerId = getUserId();
        return AjaxResult.success(buyerRequirementService.selectByBuyerId(buyerId));
    }

    @PostMapping
    public AjaxResult add(@Valid @RequestBody BuyerRequirement requirement)
    {
        Long buyerId = getUserId();
        requirement.setBuyerId(buyerId);
        requirement.setId(null);
        requirement.setSpecs(StringUtils.trimToNull(requirement.getSpecs()));
        BuyerRequirement created = buyerRequirementService.createRequirement(requirement);
        if (created == null)
        {
            return AjaxResult.error("创建失败");
        }
        return AjaxResult.success("创建成功", created);
    }

    @PutMapping("/{id}/status")
    public AjaxResult changeStatus(@PathVariable("id") Long id, @RequestBody Map<String, String> body)
    {
        String status = body != null ? body.get("status") : null;
        if (StringUtils.isEmpty(status))
        {
            return AjaxResult.error("状态不能为空");
        }
        Long buyerId = getUserId();
        int rows = buyerRequirementService.updateStatus(id, buyerId, status);
        if (rows <= 0)
        {
            return AjaxResult.error("状态更新失败");
        }
        BuyerRequirement updated = buyerRequirementService.selectByIdAndBuyerId(id, buyerId);
        return AjaxResult.success("状态更新成功", updated);
    }
}

