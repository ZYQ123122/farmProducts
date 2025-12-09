package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.AfterSale;

/**
 * 售后业务接口
 */
public interface IAfterSaleService
{
    /**
     * 查询售后
     * 
     * @param id 售后ID
     * @return 售后
     */
    public AfterSale selectAfterSaleById(Long id);

    /**
     * 查询售后列表
     * 
     * @param afterSale 售后
     * @return 售后集合
     */
    public List<AfterSale> selectAfterSaleList(AfterSale afterSale);

    /**
     * 查询农户的售后列表
     * 
     * @param farmerId 农户ID
     * @return 售后集合
     */
    public List<AfterSale> selectAfterSaleListByFarmer(Long farmerId);

    /**
     * 查询买家的售后列表
     * 
     * @param buyerId 买家ID
     * @return 售后集合
     */
    public List<AfterSale> selectAfterSaleListByBuyer(Long buyerId);

    /**
     * 新增售后
     * 
     * @param afterSale 售后
     * @return 结果
     */
    public int insertAfterSale(AfterSale afterSale);

    /**
     * 修改售后
     * 
     * @param afterSale 售后
     * @return 结果
     */
    public int updateAfterSale(AfterSale afterSale);

    /**
     * 批量删除售后
     * 
     * @param ids 需要删除的售后ID
     * @return 结果
     */
    public int deleteAfterSaleByIds(Long[] ids);

    /**
     * 删除售后信息
     * 
     * @param id 售后ID
     * @return 结果
     */
    public int deleteAfterSaleById(Long id);

    /**
     * 处理售后申请（接受或拒绝）
     * 
     * @param id 售后ID
     * @param farmerId 农户ID
     * @param accept 是否接受（true接受，false拒绝）
     * @return 结果
     */
    public int processAfterSale(Long id, Long farmerId, boolean accept);
}





