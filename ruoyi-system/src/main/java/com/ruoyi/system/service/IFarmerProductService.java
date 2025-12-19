package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.system.domain.FarmerProduct;

/**
 * 农户商品业务接口
 */
public interface IFarmerProductService
{
    /**
     * 查询农户商品列表
     */
    List<FarmerProduct> selectFarmerProductList(Long farmerId, String status);

    /**
     * 查询所有上架商品（买家视角）
     */
    List<FarmerProduct> selectAvailableProducts();

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
    int softDeleteFarmerProduct(Long id, Long farmerId);

    /**
     *修改商品状态
     */
    int changeProductStatus(Long id, Long farmerId, String status);

    /**
     * 根据ID查询指定商品（校验农户权限）
     */
    FarmerProduct selectFarmerProductById(Long id, Long farmerId);

    /**
     * 根据ID查询上架商品（买家视角）
     */
    FarmerProduct selectAvailableProductById(Long id);

    /**
     * 扣减库存
     */
    boolean decreaseProductStock(Long productId, int quantity);
}
