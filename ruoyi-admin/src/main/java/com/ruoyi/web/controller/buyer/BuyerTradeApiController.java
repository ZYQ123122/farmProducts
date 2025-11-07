package com.ruoyi.web.controller.buyer;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
import com.ruoyi.system.domain.FarmerProduct;
import com.ruoyi.system.domain.TradeOrder;
import com.ruoyi.system.service.IFarmerProductService;
import com.ruoyi.system.service.ITradeOrderService;

/**
 * 买家交易相关接口
 */
@RestController
@RequestMapping("/buyer/api/trade")
@Validated
public class BuyerTradeApiController extends BaseController
{
    @Autowired
    private IFarmerProductService farmerProductService;

    @Autowired
    private ITradeOrderService tradeOrderService;

    /**
     * 商品列表（仅上架商品）
     */
    @GetMapping("/products")
    public AjaxResult listProducts()
    {
        List<FarmerProduct> products = farmerProductService.selectAvailableProducts();
        return AjaxResult.success(products);
    }

    /**
     * 商品详情
     */
    @GetMapping("/products/{id}")
    public AjaxResult productDetail(@PathVariable("id") Long id)
    {
        FarmerProduct product = farmerProductService.selectAvailableProductById(id);
        if (product == null)
        {
            return AjaxResult.error("商品不存在或未上架");
        }
        return AjaxResult.success(product);
    }

    /**
     * 买家订单列表
     */
    @GetMapping("/orders")
    public AjaxResult listOrders()
    {
        Long buyerId = getUserId();
        List<TradeOrder> orders = tradeOrderService.listOrdersForBuyer(buyerId);
        return AjaxResult.success(orders);
    }

    /**
     * 创建订单
     */
    @PostMapping("/orders")
    public AjaxResult createOrder(@Valid @RequestBody OrderCreateRequest request)
    {
        Long buyerId = getUserId();
        TradeOrder order = tradeOrderService.createOrder(buyerId, request.getProductId(), request.getQuantity());
        return AjaxResult.success("订单创建成功", order);
    }

    /**
     * 确认收货
     */
    @PutMapping("/orders/{id}/complete")
    public AjaxResult completeOrder(@PathVariable("id") Long id)
    {
        Long buyerId = getUserId();
        tradeOrderService.completeOrder(id, buyerId);
        return AjaxResult.success("确认收货成功");
    }

    /**
     * 订单创建请求
     */
    public static class OrderCreateRequest
    {
        @NotNull(message = "商品ID不能为空")
        private Long productId;

        @NotNull(message = "购买数量不能为空")
        @Min(value = 1, message = "购买数量必须大于0")
        private Integer quantity;

        public Long getProductId()
        {
            return productId;
        }

        public void setProductId(Long productId)
        {
            this.productId = productId;
        }

        public Integer getQuantity()
        {
            return quantity;
        }

        public void setQuantity(Integer quantity)
        {
            this.quantity = quantity;
        }
    }
}

