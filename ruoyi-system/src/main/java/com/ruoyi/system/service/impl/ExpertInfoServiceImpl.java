package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ExpertInfoMapper;
import com.ruoyi.system.domain.ExpertInfo;
import com.ruoyi.system.service.IExpertInfoService;

/**
 * 专家信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
@Service
public class ExpertInfoServiceImpl implements IExpertInfoService 
{
    @Autowired
    private ExpertInfoMapper expertInfoMapper;

    /**
     * 查询专家信息
     * 
     * @param id 专家信息主键
     * @return 专家信息
     */
    @Override
    public ExpertInfo selectExpertInfoById(Long id)
    {
        return expertInfoMapper.selectExpertInfoById(id);
    }

    /**
     * 根据用户ID查询专家信息
     * 
     * @param userId 用户ID
     * @return 专家信息
     */
    @Override
    public ExpertInfo selectExpertInfoByUserId(Long userId)
    {
        return expertInfoMapper.selectExpertInfoByUserId(userId);
    }

    /**
     * 查询专家信息列表
     * 
     * @param expertInfo 专家信息
     * @return 专家信息
     */
    @Override
    public List<ExpertInfo> selectExpertInfoList(ExpertInfo expertInfo)
    {
        return expertInfoMapper.selectExpertInfoList(expertInfo);
    }

    /**
     * 新增专家信息
     * 
     * @param expertInfo 专家信息
     * @return 结果
     */
    @Override
    public int insertExpertInfo(ExpertInfo expertInfo)
    {
        return expertInfoMapper.insertExpertInfo(expertInfo);
    }

    /**
     * 修改专家信息
     * 
     * @param expertInfo 专家信息
     * @return 结果
     */
    @Override
    public int updateExpertInfo(ExpertInfo expertInfo)
    {
        return expertInfoMapper.updateExpertInfo(expertInfo);
    }

    /**
     * 批量删除专家信息
     * 
     * @param ids 需要删除的专家信息主键
     * @return 结果
     */
    @Override
    public int deleteExpertInfoByIds(Long[] ids)
    {
        return expertInfoMapper.deleteExpertInfoByIds(ids);
    }

    /**
     * 删除专家信息信息
     * 
     * @param id 专家信息主键
     * @return 结果
     */
    @Override
    public int deleteExpertInfoById(Long id)
    {
        return expertInfoMapper.deleteExpertInfoById(id);
    }

    /**
     * 专家申请入驻（提交审核）
     * 
     * @param expertInfo 专家信息
     * @return 结果
     */
    @Override
    public int applyExpert(ExpertInfo expertInfo)
    {
        // 检查是否已经申请过
        ExpertInfo existing = expertInfoMapper.selectExpertInfoByUserId(expertInfo.getUserId());
        if (existing != null) {
            // 如果已存在，更新信息并重新提交审核
            expertInfo.setId(existing.getId());
            expertInfo.setAuditStatus("0"); // 待审核
            expertInfo.setStatus("0"); // 正常状态
            return expertInfoMapper.updateExpertInfo(expertInfo);
        } else {
            // 新增申请
            expertInfo.setAuditStatus("0"); // 待审核
            expertInfo.setStatus("0"); // 正常状态
            return expertInfoMapper.insertExpertInfo(expertInfo);
        }
    }

    /**
     * 审核专家申请
     * 
     * @param id 专家信息ID
     * @param auditStatus 审核状态（1已通过 2已拒绝）
     * @param auditRemark 审核备注
     * @return 结果
     */
    @Override
    public int auditExpert(Long id, String auditStatus, String auditRemark)
    {
        ExpertInfo expertInfo = expertInfoMapper.selectExpertInfoById(id);
        if (expertInfo == null) {
            return 0;
        }
        expertInfo.setAuditStatus(auditStatus);
        if (auditRemark != null && !auditRemark.trim().isEmpty()) {
            expertInfo.setRemark(auditRemark);
        }
        // 如果审核通过，确保状态为正常
        if ("1".equals(auditStatus)) {
            expertInfo.setStatus("0");
        }
        // 设置更新人
        expertInfo.setUpdateBy(com.ruoyi.common.utils.ShiroUtils.getLoginName());
        return expertInfoMapper.updateExpertInfo(expertInfo);
    }
}

