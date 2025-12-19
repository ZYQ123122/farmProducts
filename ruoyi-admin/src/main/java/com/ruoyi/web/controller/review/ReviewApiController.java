package com.ruoyi.web.controller.review;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.Review;
import com.ruoyi.system.domain.TradeOrder;
import com.ruoyi.system.service.IReviewService;
import com.ruoyi.system.service.ITradeOrderService;

/**
 * 评价API Controller
 */
@RestController
@RequestMapping("/api/review")
public class ReviewApiController extends BaseController
{
    @Autowired
    private IReviewService reviewService;

    @Autowired
    private ITradeOrderService tradeOrderService;

    /**
     * 获取买家的评价列表
     */
    @GetMapping("/buyer/list")
    public AjaxResult getBuyerReviewList()
    {
        try
        {
            SysUser user = getSysUser();
            if (user == null)
            {
                return AjaxResult.error("未登录");
            }
            List<Review> list = reviewService.selectReviewListByBuyer(getUserId());
            return AjaxResult.success(list);
        }
        catch (Exception e)
        {
            logger.error("获取买家评价列表失败", e);
            return AjaxResult.error("获取评价列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取农户的评价列表
     */
    @GetMapping("/farmer/list")
    public AjaxResult getFarmerReviewList()
    {
        try
        {
            SysUser user = getSysUser();
            if (user == null)
            {
                return AjaxResult.error("未登录");
            }
            List<Review> list = reviewService.selectReviewListByFarmer(getUserId());
            return AjaxResult.success(list);
        }
        catch (Exception e)
        {
            logger.error("获取农户评价列表失败", e);
            return AjaxResult.error("获取评价列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据订单ID获取评价
     */
    @GetMapping("/order/{orderId}")
    public AjaxResult getReviewByOrderId(@PathVariable("orderId") Long orderId)
    {
        try
        {
            Review review = reviewService.selectReviewByOrderId(orderId);
            // 如果没有评价，返回null而不是错误
            return AjaxResult.success(review);
        }
        catch (Exception e)
        {
            logger.error("获取评价失败", e);
            return AjaxResult.error("获取评价失败：" + e.getMessage());
        }
    }

    /**
     * 创建评价
     */
    @PostMapping("/create")
    public AjaxResult createReview(@RequestParam Long orderId,
                                   @RequestParam Integer rating,
                                   @RequestParam(required = false) String content)
    {
        try
        {
            SysUser user = getSysUser();
            if (user == null)
            {
                return AjaxResult.error("未登录");
            }

            // 验证评分范围
            if (rating == null || rating < 1 || rating > 5)
            {
                return AjaxResult.error("评分必须在1-5分之间");
            }

            // 验证订单是否存在且属于当前用户且已完成
            TradeOrder order = tradeOrderService.listOrdersForBuyer(getUserId()).stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElse(null);

            if (order == null)
            {
                return AjaxResult.error("订单不存在或不属于当前用户");
            }

            if (!"completed".equals(order.getStatus()))
            {
                return AjaxResult.error("只能对已完成的订单进行评价");
            }

            // 检查是否已有评价
            Review existing = reviewService.selectReviewByOrderId(orderId);
            if (existing != null)
            {
                return AjaxResult.error("该订单已有评价");
            }

            // 创建评价
            Review review = new Review();
            review.setOrderId(orderId);
            review.setFarmerId(order.getFarmerId());
            review.setBuyerId(getUserId());
            review.setRating(rating);
            review.setContent(content);

            int result = reviewService.insertReview(review);
            if (result > 0)
            {
                Review saved = reviewService.selectReviewById(review.getId());
                return AjaxResult.success("评价成功", saved);
            }
            else
            {
                return AjaxResult.error("评价失败");
            }
        }
        catch (Exception e)
        {
            logger.error("创建评价失败", e);
            return AjaxResult.error("创建评价失败：" + e.getMessage());
        }
    }
}

