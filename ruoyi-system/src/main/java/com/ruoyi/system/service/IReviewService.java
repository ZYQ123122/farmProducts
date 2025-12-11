package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Review;

/**
 * 评价业务接口
 */
public interface IReviewService
{
    /**
     * 查询评价
     * 
     * @param id 评价ID
     * @return 评价
     */
    public Review selectReviewById(Long id);

    /**
     * 查询评价列表
     * 
     * @param review 评价
     * @return 评价集合
     */
    public List<Review> selectReviewList(Review review);

    /**
     * 查询农户的评价列表
     * 
     * @param farmerId 农户ID
     * @return 评价集合
     */
    public List<Review> selectReviewListByFarmer(Long farmerId);

    /**
     * 查询买家的评价列表
     * 
     * @param buyerId 买家ID
     * @return 评价集合
     */
    public List<Review> selectReviewListByBuyer(Long buyerId);

    /**
     * 根据订单ID查询评价
     * 
     * @param orderId 订单ID
     * @return 评价
     */
    public Review selectReviewByOrderId(Long orderId);

    /**
     * 新增评价
     * 
     * @param review 评价
     * @return 结果
     */
    public int insertReview(Review review);

    /**
     * 修改评价
     * 
     * @param review 评价
     * @return 结果
     */
    public int updateReview(Review review);

    /**
     * 批量删除评价
     * 
     * @param ids 需要删除的评价ID
     * @return 结果
     */
    public int deleteReviewByIds(Long[] ids);

    /**
     * 删除评价信息
     * 
     * @param id 评价ID
     * @return 结果
     */
    public int deleteReviewById(Long id);
}






