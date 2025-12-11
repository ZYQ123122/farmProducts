package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.AfterSale;

/**
 * 售后 Mapper接口
 */
public interface AfterSaleMapper
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
    public List<AfterSale> selectAfterSaleListByFarmer(@Param("farmerId") Long farmerId);

    /**
     * 查询买家的售后列表
     * 
     * @param buyerId 买家ID
     * @return 售后集合
     */
    public List<AfterSale> selectAfterSaleListByBuyer(@Param("buyerId") Long buyerId);

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
     * 删除售后
     * 
     * @param id 售后ID
     * @return 结果
     */
    public int deleteAfterSaleById(Long id);

    /**
     * 批量删除售后
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAfterSaleByIds(Long[] ids);
}






