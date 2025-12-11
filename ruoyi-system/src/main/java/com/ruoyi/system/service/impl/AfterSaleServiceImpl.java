package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.AfterSale;
import com.ruoyi.system.mapper.AfterSaleMapper;
import com.ruoyi.system.service.IAfterSaleService;

/**
 * 售后业务层处理
 */
@Service
public class AfterSaleServiceImpl implements IAfterSaleService
{
    @Autowired
    private AfterSaleMapper afterSaleMapper;

    /**
     * 查询售后
     * 
     * @param id 售后ID
     * @return 售后
     */
    @Override
    public AfterSale selectAfterSaleById(Long id)
    {
        return afterSaleMapper.selectAfterSaleById(id);
    }

    /**
     * 查询售后列表
     * 
     * @param afterSale 售后
     * @return 售后
     */
    @Override
    public List<AfterSale> selectAfterSaleList(AfterSale afterSale)
    {
        return afterSaleMapper.selectAfterSaleList(afterSale);
    }

    /**
     * 查询农户的售后列表
     * 
     * @param farmerId 农户ID
     * @return 售后集合
     */
    @Override
    public List<AfterSale> selectAfterSaleListByFarmer(Long farmerId)
    {
        return afterSaleMapper.selectAfterSaleListByFarmer(farmerId);
    }

    /**
     * 查询买家的售后列表
     * 
     * @param buyerId 买家ID
     * @return 售后集合
     */
    @Override
    public List<AfterSale> selectAfterSaleListByBuyer(Long buyerId)
    {
        return afterSaleMapper.selectAfterSaleListByBuyer(buyerId);
    }

    /**
     * 新增售后
     * 
     * @param afterSale 售后
     * @return 结果
     */
    @Override
    public int insertAfterSale(AfterSale afterSale)
    {
        if (afterSale.getStatus() == null || afterSale.getStatus().isEmpty())
        {
            afterSale.setStatus("pending");
        }
        return afterSaleMapper.insertAfterSale(afterSale);
    }

    /**
     * 修改售后
     * 
     * @param afterSale 售后
     * @return 结果
     */
    @Override
    public int updateAfterSale(AfterSale afterSale)
    {
        return afterSaleMapper.updateAfterSale(afterSale);
    }

    /**
     * 批量删除售后
     * 
     * @param ids 需要删除的售后ID
     * @return 结果
     */
    @Override
    public int deleteAfterSaleByIds(Long[] ids)
    {
        return afterSaleMapper.deleteAfterSaleByIds(ids);
    }

    /**
     * 删除售后信息
     * 
     * @param id 售后ID
     * @return 结果
     */
    @Override
    public int deleteAfterSaleById(Long id)
    {
        return afterSaleMapper.deleteAfterSaleById(id);
    }

    /**
     * 处理售后申请（接受或拒绝）
     * 
     * @param id 售后ID
     * @param farmerId 农户ID
     * @param accept 是否接受（true接受，false拒绝）
     * @return 结果
     */
    @Override
    public int processAfterSale(Long id, Long farmerId, boolean accept)
    {
        AfterSale afterSale = afterSaleMapper.selectAfterSaleById(id);
        if (afterSale == null)
        {
            return 0;
        }
        // 验证农户ID
        if (!afterSale.getFarmerId().equals(farmerId))
        {
            return 0;
        }
        // 只有待处理状态才能处理
        if (!"pending".equals(afterSale.getStatus()))
        {
            return 0;
        }
        // 更新状态为已解决
        afterSale.setStatus("resolved");
        return afterSaleMapper.updateAfterSale(afterSale);
    }
}






