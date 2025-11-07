package com.ruoyi.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.system.domain.TradeOrder;

/**
 * 订单 Mapper
 */
public interface TradeOrderMapper
{
    /**
     * 新建订单
     */
    int insertOrder(TradeOrder order);

    /**
     * 根据ID查询订单
     */
    TradeOrder selectOrderById(@Param("id") Long id);

    /**
     * 查询买家订单列表
     */
    List<TradeOrder> selectOrdersByBuyer(@Param("buyerId") Long buyerId);

    /**
     * 查询农户订单列表
     */
    List<TradeOrder> selectOrdersByFarmer(@Param("farmerId") Long farmerId);

    /**
     *更新订单状态（带原状态校验）
     */
    int updateOrderStatus(@Param("id") Long id, @Param("fromStatus") String fromStatus,
                          @Param("toStatus") String toStatus);
}

