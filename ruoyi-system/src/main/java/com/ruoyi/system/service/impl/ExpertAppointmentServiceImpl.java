package com.ruoyi.system.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.ExpertAppointment;
import com.ruoyi.system.mapper.ExpertAppointmentMapper;
import com.ruoyi.system.service.IExpertAppointmentService;

/**
 * 专家预约Service业务层处理
 */
@Service
public class ExpertAppointmentServiceImpl implements IExpertAppointmentService
{
    private static final List<String> ALLOWED_STATUS = Arrays.asList("pending", "accepted", "rejected", "cancelled");

    @Autowired
    private ExpertAppointmentMapper appointmentMapper;

    @Override
    public List<ExpertAppointment> selectAppointmentListByUserId(Long userId, String status)
    {
        Assert.notNull(userId, "用户ID不能为空");
        validateStatus(status);
        return appointmentMapper.selectAppointmentListByUserId(userId, status);
    }

    @Override
    public List<ExpertAppointment> selectAppointmentListByExpertId(Long expertId, String status)
    {
        Assert.notNull(expertId, "专家ID不能为空");
        validateStatus(status);
        return appointmentMapper.selectAppointmentListByExpertId(expertId, status);
    }

    @Override
    public ExpertAppointment selectAppointmentById(Long id)
    {
        if (id == null)
        {
            return null;
        }
        return appointmentMapper.selectAppointmentById(id);
    }

    @Override
    public int insertAppointment(ExpertAppointment appointment)
    {
        Assert.notNull(appointment.getUserId(), "用户ID不能为空");
        Assert.notNull(appointment.getExpertId(), "专家ID不能为空");
        Assert.notNull(appointment.getAppointmentTime(), "预约时间不能为空");
        if (appointment.getStatus() == null)
        {
            appointment.setStatus("pending");
        }
        validateStatus(appointment.getStatus());
        return appointmentMapper.insertAppointment(appointment);
    }

    @Override
    public int updateAppointmentStatus(Long id, String status)
    {
        Assert.notNull(id, "预约ID不能为空");
        validateStatus(status);
        return appointmentMapper.updateAppointmentStatus(id, status);
    }

    private void validateStatus(String status)
    {
        if (StringUtils.hasText(status) && !ALLOWED_STATUS.contains(status))
        {
            throw new ServiceException("非法的预约状态，允许值：pending, accepted, rejected, cancelled");
        }
    }
}


