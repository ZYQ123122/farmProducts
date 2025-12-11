package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.Review;
import com.ruoyi.system.mapper.ReviewMapper;
import com.ruoyi.system.service.IReviewService;

/**
 * 评价业务层处理
 */
@Service
public class ReviewServiceImpl implements IReviewService
{
    @Autowired
    private ReviewMapper reviewMapper;

    /**
     * 查询评价
     * 
     * @param id 评价ID
     * @return 评价
     */
    @Override
    public Review selectReviewById(Long id)
    {
        return reviewMapper.selectReviewById(id);
    }

    /**
     * 查询评价列表
     * 
     * @param review 评价
     * @return 评价集合
     */
    @Override
    public List<Review> selectReviewList(Review review)
    {
        return reviewMapper.selectReviewList(review);
    }

    /**
     * 查询农户的评价列表
     * 
     * @param farmerId 农户ID
     * @return 评价集合
     */
    @Override
    public List<Review> selectReviewListByFarmer(Long farmerId)
    {
        return reviewMapper.selectReviewListByFarmer(farmerId);
    }

    /**
     * 查询买家的评价列表
     * 
     * @param buyerId 买家ID
     * @return 评价集合
     */
    @Override
    public List<Review> selectReviewListByBuyer(Long buyerId)
    {
        return reviewMapper.selectReviewListByBuyer(buyerId);
    }

    /**
     * 根据订单ID查询评价
     * 
     * @param orderId 订单ID
     * @return 评价
     */
    @Override
    public Review selectReviewByOrderId(Long orderId)
    {
        return reviewMapper.selectReviewByOrderId(orderId);
    }

    /**
     * 新增评价
     * 
     * @param review 评价
     * @return 结果
     */
    @Override
    public int insertReview(Review review)
    {
        return reviewMapper.insertReview(review);
    }

    /**
     * 修改评价
     * 
     * @param review 评价
     * @return 结果
     */
    @Override
    public int updateReview(Review review)
    {
        return reviewMapper.updateReview(review);
    }

    /**
     * 批量删除评价
     * 
     * @param ids 需要删除的评价ID
     * @return 结果
     */
    @Override
    public int deleteReviewByIds(Long[] ids)
    {
        return reviewMapper.deleteReviewByIds(ids);
    }

    /**
     * 删除评价信息
     * 
     * @param id 评价ID
     * @return 结果
     */
    @Override
    public int deleteReviewById(Long id)
    {
        return reviewMapper.deleteReviewById(id);
    }
}






