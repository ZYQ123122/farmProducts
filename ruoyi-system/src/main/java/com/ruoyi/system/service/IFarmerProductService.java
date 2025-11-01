package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.system.domain.FarmerProduct;

/**
 * ũ����Ʒҵ��ӿ�
 */
public interface IFarmerProductService
{
    /**
     * ��ѯũ����Ʒ�б�
     */
    List<FarmerProduct> selectFarmerProductList(Long farmerId, String status);

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
    int softDeleteFarmerProduct(Long id, Long farmerId);

    /**
     * �޸���Ʒ״̬
     */
    int changeProductStatus(Long id, Long farmerId, String status);

    /**
     * ����ID��ѯָ����Ʒ������ũ����
     */
    FarmerProduct selectFarmerProductById(Long id, Long farmerId);
}
