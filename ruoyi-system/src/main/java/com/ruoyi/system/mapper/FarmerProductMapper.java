package com.ruoyi.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.system.domain.FarmerProduct;

/**
 * 农户商品 Mapper
 */
public interface FarmerProductMapper
{
    /**
     * 根据农户查询商品列表
     */
    List<FarmerProduct> selectFarmerProductList(@Param("farmerId") Long farmerId,
                                                @Param("status") String status);

    /**
     * 根据ID和农户查询商品
     */
    FarmerProduct selectFarmerProductById(@Param("id") Long id, @Param("farmerId") Long farmerId);

    /**
     * 新增商品
     */
    int insertFarmerProduct(FarmerProduct farmerProduct);

    /**
     * 修改商品
     */
    int updateFarmerProduct(FarmerProduct farmerProduct);

    /**
     * 软删除商品
     */
    int softDeleteFarmerProduct(@Param("id") Long id, @Param("farmerId") Long farmerId);

    /**
     * 修改商品状态
     */
    int changeProductStatus(@Param("id") Long id, @Param("farmerId") Long farmerId,
                            @Param("status") String status);
}

