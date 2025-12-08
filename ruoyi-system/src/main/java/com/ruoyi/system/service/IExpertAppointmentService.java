package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.system.domain.ExpertAppointment;

/**
 * 专家预约Service接口
 */
public interface IExpertAppointmentService
{
    List<ExpertAppointment> selectAppointmentListByUserId(Long userId, String status);

    List<ExpertAppointment> selectAppointmentListByExpertId(Long expertId, String status);

    ExpertAppointment selectAppointmentById(Long id);

    int insertAppointment(ExpertAppointment appointment);

    int updateAppointmentStatus(Long id, String status);
}


