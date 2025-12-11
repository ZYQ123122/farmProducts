package com.ruoyi.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.system.domain.ExpertAppointment;

/**
 * 专家预约Mapper接口
 */
public interface ExpertAppointmentMapper
{
    List<ExpertAppointment> selectAppointmentListByUserId(@Param("userId") Long userId, @Param("status") String status);

    List<ExpertAppointment> selectAppointmentListByExpertId(@Param("expertId") Long expertId, @Param("status") String status);

    ExpertAppointment selectAppointmentById(Long id);

    int insertAppointment(ExpertAppointment appointment);

    int updateAppointmentStatus(@Param("id") Long id, @Param("status") String status);
}
