package com.ruoyi.web.controller.farmer;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.FarmerProduct;
import com.ruoyi.system.service.IFarmerProductService;

/**
 * 农户商品管理接口
 */
@RestController
@RequestMapping("/farmer/api/product")
@Validated
public class FarmerProductApiController extends BaseController
{
    @Autowired
    private IFarmerProductService farmerProductService;

    @GetMapping("/list")
    public AjaxResult list(@RequestParam(value = "status", required = false) String status)
    {
        Long farmerId = getUserId();
        return AjaxResult.success(farmerProductService.selectFarmerProductList(farmerId, status));
    }

    @GetMapping("/{id}")
    public AjaxResult detail(@PathVariable("id") Long id)
    {
        Long farmerId = getUserId();
        FarmerProduct product = farmerProductService.selectFarmerProductById(id, farmerId);
        if (product == null)
        {
            return AjaxResult.error("商品不存在或已删除");
        }
        return AjaxResult.success(product);
    }

    @PostMapping
    public AjaxResult add(@Valid @RequestBody FarmerProduct farmerProduct)
    {
        Long farmerId = getUserId();
        farmerProduct.setFarmerId(farmerId);
        farmerProduct.setId(null);
        int rows = farmerProductService.insertFarmerProduct(farmerProduct);
        if (rows > 0)
        {
            FarmerProduct created = farmerProductService.selectFarmerProductById(farmerProduct.getId(), farmerId);
            return AjaxResult.success("新增成功", created);
        }
        return AjaxResult.error("新增失败");
    }

    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable("id") Long id, @Valid @RequestBody FarmerProduct farmerProduct)
    {
        Long farmerId = getUserId();
        FarmerProduct current = farmerProductService.selectFarmerProductById(id, farmerId);
        if (current == null)
        {
            return AjaxResult.error("商品不存在或已删除");
        }
        farmerProduct.setId(id);
        farmerProduct.setFarmerId(farmerId);
        if (farmerProduct.getStatus() == null)
        {
            farmerProduct.setStatus(current.getStatus());
        }
        int rows = farmerProductService.updateFarmerProduct(farmerProduct);
        if (rows > 0)
        {
            FarmerProduct updated = farmerProductService.selectFarmerProductById(id, farmerId);
            return AjaxResult.success("更新成功", updated);
        }
        return AjaxResult.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable("id") Long id)
    {
        Long farmerId = getUserId();
        return toAjax(farmerProductService.softDeleteFarmerProduct(id, farmerId));
    }

    @PutMapping("/{id}/status")
    public AjaxResult changeStatus(@PathVariable("id") Long id, @RequestBody Map<String, String> body)
    {
        String status = body != null ? body.get("status") : null;
        if (status == null)
        {
            return AjaxResult.error("状态不能为空");
        }
        Long farmerId = getUserId();
        int rows = farmerProductService.changeProductStatus(id, farmerId, status);
        if (rows > 0)
        {
            FarmerProduct updated = farmerProductService.selectFarmerProductById(id, farmerId);
            return AjaxResult.success("状态更新成功", updated);
        }
        return AjaxResult.error("状态更新失败");
    }
}
