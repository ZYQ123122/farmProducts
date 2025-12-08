package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.ExpertInfo;

/**
 * 专家信息Service接口
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
public interface IExpertInfoService 
{
    /**
     * 查询专家信息
     * 
     * @param id 专家信息主键
     * @return 专家信息
     */
    public ExpertInfo selectExpertInfoById(Long id);

    /**
     * 根据用户ID查询专家信息
     * 
     * @param userId 用户ID
     * @return 专家信息
     */
    public ExpertInfo selectExpertInfoByUserId(Long userId);

    /**
     * 查询专家信息列表
     * 
     * @param expertInfo 专家信息
     * @return 专家信息集合
     */
    public List<ExpertInfo> selectExpertInfoList(ExpertInfo expertInfo);

    /**
     * 新增专家信息
     * 
     * @param expertInfo 专家信息
     * @return 结果
     */
    public int insertExpertInfo(ExpertInfo expertInfo);

    /**
     * 修改专家信息
     * 
     * @param expertInfo 专家信息
     * @return 结果
     */
    public int updateExpertInfo(ExpertInfo expertInfo);

    /**
     * 批量删除专家信息
     * 
     * @param ids 需要删除的专家信息主键集合
     * @return 结果
     */
    public int deleteExpertInfoByIds(Long[] ids);

    /**
     * 删除专家信息信息
     * 
     * @param id 专家信息主键
     * @return 结果
     */
    public int deleteExpertInfoById(Long id);

    /**
     * 专家申请入驻（提交审核）
     * 
     * @param expertInfo 专家信息
     * @return 结果
     */
    public int applyExpert(ExpertInfo expertInfo);

    /**
     * 审核专家申请
     * 
     * @param id 专家信息ID
     * @param auditStatus 审核状态（1已通过 2已拒绝）
     * @param auditRemark 审核备注
     * @return 结果
     */
    public int auditExpert(Long id, String auditStatus, String auditRemark);
}

