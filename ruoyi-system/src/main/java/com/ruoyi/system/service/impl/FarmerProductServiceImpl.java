package com.ruoyi.system.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.FarmerProduct;
import com.ruoyi.system.mapper.FarmerProductMapper;
import com.ruoyi.system.service.IFarmerProductService;

/**
 * 农户商品业务实现
 */
@Service
public class FarmerProductServiceImpl implements IFarmerProductService
{
    private static final List<String> ALLOWED_STATUS = Arrays.asList("on_shelf", "off_shelf");

    @Autowired
    private FarmerProductMapper farmerProductMapper;

    @Override
    public List<FarmerProduct> selectFarmerProductList(Long farmerId, String status)
    {
        if (StringUtils.hasText(status) && !ALLOWED_STATUS.contains(status))
        {
            throw new ServiceException("非法的商品状态");
        }
        return farmerProductMapper.selectFarmerProductList(farmerId, status);
    }

    @Override
    public FarmerProduct selectFarmerProductById(Long id, Long farmerId)
    {
        if (id == null)
        {
            return null;
        }
        return farmerProductMapper.selectFarmerProductById(id, farmerId);
    }

    @Override
    public int insertFarmerProduct(FarmerProduct farmerProduct)
    {
        if (farmerProduct.getStatus() == null)
        {
            farmerProduct.setStatus("off_shelf");
        }
        validateStatus(farmerProduct.getStatus());
        farmerProduct.setIsDeleted(0);
        return farmerProductMapper.insertFarmerProduct(farmerProduct);
    }

    @Override
    public int updateFarmerProduct(FarmerProduct farmerProduct)
    {
        Assert.notNull(farmerProduct.getId(), "商品ID不能为空");
        validateStatus(farmerProduct.getStatus());
        return farmerProductMapper.updateFarmerProduct(farmerProduct);
    }

    @Override
    public int softDeleteFarmerProduct(Long id, Long farmerId)
    {
        Assert.notNull(id, "商品ID不能为空");
        return farmerProductMapper.softDeleteFarmerProduct(id, farmerId);
    }

    @Override
    public int changeProductStatus(Long id, Long farmerId, String status)
    {
        Assert.notNull(id, "商品ID不能为空");
        validateStatus(status);
        return farmerProductMapper.changeProductStatus(id, farmerId, status);
    }

    private void validateStatus(String status)
    {
        if (StringUtils.hasText(status) && !ALLOWED_STATUS.contains(status))
        {
            throw new ServiceException("非法的商品状态");
        }
    }
}
