package com.ruoyi.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.system.domain.FarmerProduct;

/**
 * ũ����Ʒ Mapper
 */
public interface FarmerProductMapper
{
    /**
     * ����ũ����ѯ��Ʒ�б�
     */
    List<FarmerProduct> selectFarmerProductList(@Param("farmerId") Long farmerId,
            @Param("status") String status);

    /**
     * ����ID��ũ����ѯ��Ʒ
     */
    FarmerProduct selectFarmerProductById(@Param("id") Long id, @Param("farmerId") Long farmerId);

    /**
     * ������Ʒ
     */
    int insertFarmerProduct(FarmerProduct farmerProduct);

    /**
     * ������Ʒ
     */
    int updateFarmerProduct(FarmerProduct farmerProduct);

    /**
     * ��ɾ����Ʒ
     */
    int softDeleteFarmerProduct(@Param("id") Long id, @Param("farmerId") Long farmerId);

    /**
     * �޸���Ʒ״̬
     */
    int changeProductStatus(@Param("id") Long id, @Param("farmerId") Long farmerId,
            @Param("status") String status);
}
