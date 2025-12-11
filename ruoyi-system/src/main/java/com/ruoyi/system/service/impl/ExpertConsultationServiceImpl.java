package com.ruoyi.system.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.ExpertConsultation;
import com.ruoyi.system.mapper.ExpertConsultationMapper;
import com.ruoyi.system.service.IExpertConsultationService;

/**
 * 专家咨询业务实现
 */
@Service
public class ExpertConsultationServiceImpl implements IExpertConsultationService
{
    private static final List<String> ALLOWED_STATUS = Arrays.asList("pending", "answered", "closed");

    @Autowired
    private ExpertConsultationMapper expertConsultationMapper;

    @Override
    public List<ExpertConsultation> selectExpertConsultationList(ExpertConsultation consultation)
    {
        return expertConsultationMapper.selectExpertConsultationList(consultation);
    }

    @Override
    public List<ExpertConsultation> selectConsultationListByUserId(Long userId, String status)
    {
        if (StringUtils.hasText(status) && !ALLOWED_STATUS.contains(status))
        {
            throw new ServiceException("非法的咨询状态");
        }
        return expertConsultationMapper.selectConsultationListByUserId(userId, status);
    }

    @Override
    public List<ExpertConsultation> selectConsultationListByExpertId(Long expertId, String status)
    {
        if (StringUtils.hasText(status) && !ALLOWED_STATUS.contains(status))
        {
            throw new ServiceException("非法的咨询状态");
        }
        return expertConsultationMapper.selectConsultationListByExpertId(expertId, status);
    }

    @Override
    public ExpertConsultation selectExpertConsultationById(Long id)
    {
        if (id == null)
        {
            return null;
        }
        return expertConsultationMapper.selectExpertConsultationById(id);
    }

    @Override
    public ExpertConsultation selectExpertConsultationByIdAndUserId(Long id, Long userId)
    {
        if (id == null || userId == null)
        {
            return null;
        }
        return expertConsultationMapper.selectExpertConsultationByIdAndUserId(id, userId);
    }

    @Override
    public int insertExpertConsultation(ExpertConsultation consultation)
    {
        Assert.notNull(consultation.getUserId(), "用户ID不能为空");
        Assert.notNull(consultation.getExpertId(), "专家ID不能为空");

        if (consultation.getStatus() == null)
        {
            consultation.setStatus("pending");
        }
        validateStatus(consultation.getStatus());
        consultation.setIsDeleted(0);
        return expertConsultationMapper.insertExpertConsultation(consultation);
    }

    @Override
    public int updateExpertConsultation(ExpertConsultation consultation)
    {
        Assert.notNull(consultation.getId(), "咨询ID不能为空");
        if (consultation.getStatus() != null)
        {
            validateStatus(consultation.getStatus());
        }
        return expertConsultationMapper.updateExpertConsultation(consultation);
    }

    @Override
    public int replyConsultation(Long id, Long expertId, String reply)
    {
        Assert.notNull(id, "咨询ID不能为空");
        Assert.notNull(expertId, "专家ID不能为空");
        Assert.hasText(reply, "回复内容不能为空");

        ExpertConsultation consultation = expertConsultationMapper.selectExpertConsultationById(id);
        if (consultation == null)
        {
            throw new ServiceException("咨询记录不存在");
        }
        if (!expertId.equals(consultation.getExpertId()))
        {
            throw new ServiceException("无权回复此咨询");
        }
        if ("closed".equals(consultation.getStatus()))
        {
            throw new ServiceException("该咨询已关闭，无法回复");
        }

        return expertConsultationMapper.replyConsultation(id, expertId, reply);
    }

    @Override
    public int softDeleteExpertConsultation(Long id, Long userId)
    {
        Assert.notNull(id, "咨询ID不能为空");
        Assert.notNull(userId, "用户ID不能为空");
        return expertConsultationMapper.softDeleteExpertConsultation(id, userId);
    }

    @Override
    public int changeConsultationStatus(Long id, String status)
    {
        Assert.notNull(id, "咨询ID不能为空");
        validateStatus(status);
        return expertConsultationMapper.changeConsultationStatus(id, status);
    }

    /**
     * 验证状态值
     */
    private void validateStatus(String status)
    {
        if (StringUtils.hasText(status) && !ALLOWED_STATUS.contains(status))
        {
            throw new ServiceException("非法的咨询状态，允许的状态：pending, answered, closed");
        }
    }
}
